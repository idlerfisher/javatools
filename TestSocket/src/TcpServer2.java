import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;


public class TcpServer2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			ServerSocket srvSock = ServerSocketFactory.getDefault().createServerSocket(12345);
			
			char[] recv = new char[1024];
			String strRecv = null;
			while (true) {
				System.out.println("listen:");
				Socket sock = srvSock.accept();
				sock.setKeepAlive(true);
				InputStreamReader reader = new InputStreamReader(sock.getInputStream());
				OutputStreamWriter writer = new OutputStreamWriter(sock.getOutputStream());
				
				while (true) {
					if (reader.ready()) {
						int len = reader.read(recv);
						if (len != -1) {
							strRecv = new String(recv, 0, len);
							System.out.println("recv:" + strRecv);
							
							if (strRecv.equals("exit")) {
								writer.write("bye!");
								writer.flush();
								writer.close();
								reader.close();
								sock.close();
								break;
							} else if (strRecv.equals("quit")) {
								writer.write("system quit!");
								writer.flush();
								writer.close();
								reader.close();
								sock.close();
								srvSock.close();
								System.out.println("system quit.");
								System.exit(0);
							}
						}
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
