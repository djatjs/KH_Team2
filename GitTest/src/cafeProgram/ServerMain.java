package cafeProgram;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

    public static void main(String[] args) {
        String fileName = "src/cafeProgram/data.txt";
        List<Customer2> list = load(fileName);

        // 데이터 파일이 없거나 로드 실패 시, 새로운 리스트 생성
        if (list == null) {
            System.out.println("[데이터 파일이 없거나 로드 실패. 새 리스트 생성]");
            list = new ArrayList<>();
        }

        int port = 9999;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[서버가 실행 중 입니다.]");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[새 클라이언트 연결됨]");

                // 새로운 클라이언트 연결 시 데이터 저장
                save(fileName, list);

                Server server = new Server(list, serverSocket);
                server.handleMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            save(fileName, list); // 서버 종료 시 데이터 저장
        }
    }

   
    private static void save(String fileName, List<Customer2> obj) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(obj);
            System.out.println("[저장 완료]");
        } catch (IOException e) {
            System.out.println("[저장 실패]");
        }
    }

   
    private static List<Customer2> load(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            return (List<Customer2>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[불러오기 실패]");
        }
        return null;
    }
}
