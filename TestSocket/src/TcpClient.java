import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class TcpClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket sock = new Socket("127.0.0.1", 60000);
			
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
			InputStreamReader sockReader = new InputStreamReader(sock.getInputStream());
			OutputStreamWriter sockWriter = new OutputStreamWriter(sock.getOutputStream());
			
			
			
			Thread.currentThread();
			Thread.sleep(1000);
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
