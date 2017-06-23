import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParse {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ParseHtml("c:/top_100_music.html");
	}
	
	private static void ParseHtml(String filePath) throws Exception {
		StringBuilder builder = new StringBuilder();
		String strLine = null;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while ((strLine = br.readLine()) != null) {
			builder.append(strLine + "\r\n");
//			System.out.println(strLine);
		}
		br.close();
		
		String strInput = builder.toString();
		
		String strRegex = "alt=\"(.*?)\"/>";
		Pattern p = Pattern.compile(strRegex);
		Matcher m = p.matcher(strInput);
//		if (m.find()) {
//			System.out.println(m.group(1));
//		}
		int nCount = 0;
		while (m.find()) {
			++nCount;
			System.out.println(nCount + "." + m.group(1));
		}
//		System.out.println(strInput);
//		if (m.find()) {
//			System.out.println(m.groupCount());
//		} else {
//			System.out.println("asdf");
//		}
	}

}
