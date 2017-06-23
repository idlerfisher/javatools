import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class UdpClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket sock = new DatagramSocket();
			byte[] buf = new byte[1024];
			DatagramPacket pack = new DatagramPacket(buf, buf.length, new InetSocketAddress("127.0.0.1", 12345));
			
			String str = "hello world";
			pack.setData(str.getBytes());

			Thread.currentThread();
			
			int nCount = 0;
			while (++nCount <= 20) {
				sock.send(pack);
				Thread.sleep(1000);
			}
			
			sock.close();
						
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
