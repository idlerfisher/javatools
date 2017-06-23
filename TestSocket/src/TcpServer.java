import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.CharBuffer;


public class TcpServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket srvSock = new ServerSocket(10000);
			while (true) {
				System.out.println("start listen:");
				Socket sock = srvSock.accept();
				sock.setKeepAlive(true);

				OutputStreamWriter writer = new OutputStreamWriter(sock.getOutputStream());
				InputStreamReader reader = new InputStreamReader(sock.getInputStream());
				while (true) {
					int len = 0;
					String strRecv = null;
					char[] recv = new char[100];
					if (reader.ready()) {
						len = reader.read(recv);
						if (len != -1) {
							strRecv = new String(recv, 0, len);
						}
						System.out.println("recv[" + sock.getInetAddress().getHostAddress() + ":" + sock.getPort() + "]=>" + strRecv);
						if (strRecv.equalsIgnoreCase("exit")) {
							writer.write("bye!");
							writer.flush();
							sock.close();
							break;
						}
					}
				}
				
				//System.out.println(sock.getInetAddress().getHostAddress() + ":" + sock.getPort() + " connected.");
				//sock.close();
			}
			
			
//			InetAddress addr = sock.getInetAddress();
//			String ip = addr.getHostAddress();
//			System.out.println(ip + " " + addr.getHostName() + " " + sock.getPort());
//			System.out.println(sock.getRemoteSocketAddress());
//			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
