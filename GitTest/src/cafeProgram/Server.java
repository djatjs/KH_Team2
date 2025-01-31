package cafeProgram;

import java.io.*;
import java.net.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;


public class Server {
    
	private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private static List<Customer2> list = new ArrayList<Customer2>();;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
    	ServerSocket serverSocket;
    	
    	//불러오기
    	
		try {
			serverSocket = new ServerSocket(9999);
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					System.out.println("[연결 성공]" );
					
					CafeServer server = new CafeServer(socket, list);
					server.run();
				} catch (IOException e) {
					System.out.println("[연결 실패]");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//저장하기
		}
    }
    
    
    
    
    public void startServer() {
    	try {
    		while (true) {
    			Socket socket = serverSocket.accept();
    			System.out.println("[연결 성공]" );
    			
    			new ClientHandler(socket, list).start();
    		}
    	} catch (IOException e) {
    		System.out.println("[오류 발생]");
    		e.printStackTrace();
    	}
    }
}                   


class ClientHandler extends Thread {
    private Socket socket;
    private List<Customer2> list;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientHandler(Socket socket, List<Customer2> list) {
        this.socket = socket;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            int menu;
            do {
                menu = ois.readInt(); 
                runMenu(menu);
            } while (menu != 3); 

            System.out.println("[클라이언트 연결 종료]");
            socket.close();
        } catch (EOFException e) {
            System.out.println("[클라이언트 연결이 끊어졌습니다.]");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

   
    private void runMenu(int menu) throws IOException, ClassNotFoundException {
        switch (menu) {
            case 1:
                Login();
                break;
            case 2:
                SignUp();
                break;
            case 3:
               
                break;
            default:
                System.out.println("[잘못된 메뉴입니다.");
        }
    }

 
    private void Login() throws IOException, ClassNotFoundException {
        oos.writeInt(1); 
        oos.flush();

        Customer2 loginAttempt = (Customer2) ois.readObject();
        Optional<Customer2> user = list.stream()
            .filter(c -> c.getName().equals(loginAttempt.getName()) &&
                         c.getPassWord().equals(loginAttempt.getPassWord()))
            .findFirst();

        if (user.isPresent()) {
            oos.writeObject(user.get());
            System.out.println("[로그인 하셨습니다]");
        } else {
            oos.writeObject(null);
            System.out.println("[아이디 또는 비밀번호 오류입니다.]");
        }
        oos.flush();
    }

   
    private void SignUp() throws IOException, ClassNotFoundException {
        int requestType = ois.readInt(); 
        if (requestType != 2) {
            System.out.println("잘못입력 하셨습니다.");
            return;
        }

        Customer2 newUser = (Customer2) ois.readObject(); 
        boolean isDuplicate = list.stream()
            .anyMatch(c -> c.getName().equals(newUser.getName()));

        if (isDuplicate) {
            oos.writeBoolean(false); 
            System.out.println("[중복된 아이디입니다.]");
        } else {
        	list.add(newUser); 
            oos.writeBoolean(true); 
            System.out.println("[회원가입이 완료됐습니다.]");
        }
        oos.flush();
    }
}
