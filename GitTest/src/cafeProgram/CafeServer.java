package cafeProgram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CafeServer {
	private Socket socket;
	private List<Customer2> list;
	


	public void run() {
		Thread t = new Thread(()->{
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		});
		t.start();
	}
	

}
