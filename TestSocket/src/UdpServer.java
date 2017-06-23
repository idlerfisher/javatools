import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DatagramSocket sock = new DatagramSocket(12345);
			byte[] buf = new byte[1024];
			DatagramPacket pack = new DatagramPacket(buf, buf.length);
			
			while (true) {
				sock.receive(pack);
				
				System.out.println(pack.getAddress().getHostAddress() + ":" + pack.getPort() + "=>");
				String str = new String(pack.getData(), 0, pack.getLength());
				System.out.println(str);
				
				if (str.equals("exit")) {
					break;
				}
			}
			sock.close();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
