import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseLog {
	public static void main(String[] args) throws Exception {
		String strFile = "D:\\ITS\\内网中控测试log\\centercontrol_info.log";
		
		long beginMsec = System.currentTimeMillis();
		
		//CostTime
		beginMsec = System.currentTimeMillis();
		parseFile(strFile, "D:\\ITS\\内网中控测试log\\zz_costtime.xlsx", "##", "##(.*?)\\(", "(\\d+)ms");
		System.out.println("耗时：" + (System.currentTimeMillis() - beginMsec) + "毫秒");
		
		//login_Async
		beginMsec = System.currentTimeMillis();
		parseFile(strFile, "D:\\ITS\\内网中控测试log\\zz_threadsync.xlsx", "login_threadsync", "\\((.*?)\\)", "(\\d+)ms");
		System.out.println("耗时：" + (System.currentTimeMillis() - beginMsec) + "毫秒");
		
		//login_sync
		beginMsec = System.currentTimeMillis();
		parseFile(strFile, "D:\\ITS\\内网中控测试log\\zz_sync.xlsx", "login_sync", "\\((.*?)\\)", "(\\d+)ms");
		System.out.println("耗时：" + (System.currentTimeMillis() - beginMsec) + "毫秒");
	}
		
	public static void parseFile(String strFile, String strOutFile, String strStart, String strP1, String strP2) throws Exception {
		FileInputStream fis = new FileInputStream(strFile);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader bfr = new BufferedReader(isr);
		
		File of1 = new File(strOutFile);
		if (of1.exists()) {
			of1.delete();
		}
		of1.createNewFile();
		FileOutputStream fos1 = new FileOutputStream(of1);
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);

		String strLine = bfr.readLine();
		Pattern p = Pattern.compile(strP1);
		Pattern p2 = Pattern.compile(strP2);
		int cnt1 = 0, cnt2 = 0;
		osw1.write("数据\t时间\r\n");
		while (strLine != null) {
			if (strLine.startsWith(strStart)) {
				String val1 = "", val2 = "";
				Matcher m = p.matcher(strLine);
				if (m.find()) {
					val1 = m.group(1);
					++cnt1;
				}
				
				Matcher m2 = p2.matcher(strLine);
				if (m2.find()) {
					val2 = m2.group(1);
					++cnt2;
				}
				
				osw1.write(val1 + "\t" + val2 + "\r\n");
			}
			strLine = bfr.readLine();
		}
		osw1.close();
		bfr.close();
		
		System.out.println("处理成功:cnt1=" + cnt1 + " cnt2=" + cnt2);
	}
}
