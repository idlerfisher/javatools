import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.*;


public class Csv2Lua {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.out.println("Please input *.csv file.");
			return;
		}

		String strInDir = args[0];//"C:\\CSV\\csv";//
		String strOutDir = args[1];//"C:\\CSV\\lua";//
//		String strInDir = "C:\\bbbb\\csv";
//		String strOutDir = "C:\\bbbb\\lua";
		
		File dir = new File(strOutDir);
		dir.mkdir();
		
		List<String> listFile = new ArrayList<>();
		searchFile(strInDir, listFile);
		
		for (String path : listFile) {
			System.out.println("start=>" + path);
			Csv csv = new Csv(path);
			csv.setOutDir(strOutDir);
			csv.parse();
			
			Lua lua = new Lua(csv);
			lua.build();
			lua.write(csv.getOutputFileName());
			System.out.println("OK=>" + csv.getOutputFileName());
		}
		
		System.out.println("csv to lua ok");
	}
	
	public static void searchFile(String filePath, List<String> listFile) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] arrFile = file.listFiles();
			for (File f : arrFile) {
				listFile.add(f.getAbsolutePath());
			}
		}
	}
}

/**
 * @author guozs
 * 解析csv
 */
class Csv {
	private String filePath;
	private String outDir;
	private boolean beginData = false; //开始数据
	private StringBuilder builder = null;
	private Map<String, CsvKeyValue> mapData = null;
	
	Csv(String filePath) {
		this.filePath = filePath;
		mapData = new HashMap<>();
		builder = new StringBuilder();
	}
	
	public String getFileName() {
		return filePath;
	}
	public void setFileName(String fileName) {
		this.filePath = fileName;
	}

	public boolean isBeginData() {
		return beginData;
	}

	public void setBeginData(boolean beginData) {
		this.beginData = beginData;
	}

	public Map<String, CsvKeyValue> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, CsvKeyValue> mapData) {
		this.mapData = mapData;
	}

	public String getOutDir() {
		return outDir;
	}

	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}

	public boolean parse()  {
		try {
			if (filePath.endsWith("*.csv")) {
				System.out.println("error:" + filePath + " is not a csv file.");
				return false;
			}
			
			String encode = Common.codeString(filePath);
			InputStreamReader ss = new InputStreamReader(new FileInputStream(filePath), encode);
			BufferedReader buffReader = new BufferedReader(ss);//new FileReader(filePath)
			String strLine = null;
			while ((strLine = buffReader.readLine()) != null) {
				if (isBeginData()) {
					builder.append(strLine + "\r\n");
				} else {
//					String str = new String(strLine.getBytes("CP936"));
//					System.out.println(str);
//					System.out.println(strLine);
					if (strLine.startsWith("auto") || strLine.startsWith("\"auto")) {
						setBeginData(true);
					}
				}
			}
			buffReader.close();
			
			//解析csv
			parseCsv();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private String dealString(String info) {

		if (info.contains("CLASS_ID")) {
			Pattern p = Pattern.compile("@CLASS_ID\\((.*?)\\)");
			Matcher m = p.matcher(info);
			if (m.find()) {
				return m.group(1);
			}
		} else if (info.contains("\"")) {
			return info.replace('"', '\'');
		}
		
		return info;
	}

	private void parseCsv() {
		try {
//			CsvReader csv = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
			String strInput = builder.toString();
//			String encode = Common.getEncoding(strInput);
//			System.out.println(encode);
//			if (encode.equals("GB2312")) {
//				strInput = Common.gb2312ToUtf8(strInput);
//			}
			strInput = Common.gb2312ToUtf8(strInput);
			CsvReader csv = CsvReader.parse(builder.toString());
			csv.readHeaders();
			
			String[] arrHeader = csv.getHeaders();
			String key, val, mainKey;
			
			while (csv.readRecord()) {
				CsvKeyValue kv = new CsvKeyValue();
				mainKey = dealString(csv.get(0));
				for (int i = 0; i < arrHeader.length; i++) {
					key = arrHeader[i];
					val = csv.get(key);
					val = dealString(val);
					if (!key.startsWith("#")) {
						kv.put(key, val);
					}
				}
				mapData.put(mainKey, kv);
			}
			
			csv.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
		
	public String getOutputFileName() {
		String strOutput = "";
		int index1 = filePath.lastIndexOf(File.separatorChar);
		int index2 = filePath.lastIndexOf('.');
		String name = filePath.substring(index1+1, index2);
		strOutput = getOutDir() + File.separator + name + ".lua";
		return strOutput;
	}
}

/**
 * @author guozs
 * csv内部数据
 */
class CsvKeyValue {
	
	private Map<String, String> mapData = null;
	
	CsvKeyValue() {
		mapData = new HashMap<>();
	}
	
	String getValue(String key) {
		return mapData.get(key);
	}
	
	void put(String key, String value) {
		mapData.put(key, value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		
		String value;
		for (Map.Entry<String, String> entry : mapData.entrySet()) {
			value = entry.getValue();
			if (!value.isEmpty()) {
				if (isDigit(value)) {
					builder.append("\t\t[\"" + entry.getKey() + "\"] = " + entry.getValue() + ",\r\n");	
				} else {
					builder.append("\t\t[\"" + entry.getKey() + "\"] = \"" + entry.getValue() + "\",\r\n");
				}
			}
		}
		
		builder.append("\t},\r\n");
		return builder.toString();
	}
	
	static boolean isDigit(String key) {
		boolean digit = true;
		char[] arr = key.toCharArray();
		for (Character ch : arr) {
			if (!Character.isDigit(ch)) {
				digit = false;
				break;
			}
		}
		return digit;
	}
}

/**
 * @author guozs
 * 导出lua格式
 */
class Lua {
	
	private Csv csv = null;
	private StringBuilder builder = null;
	
	Lua(Csv csv) {
		this.csv = csv;
		builder = new StringBuilder();
	}

	public void build() {
		Map<String, CsvKeyValue> map = csv.getMapData();
		
		builder.append("local tableInfo = {\r\n");
		
		CsvKeyValue kv;
		for (Map.Entry<String, CsvKeyValue> entry : map.entrySet()) {
			kv = entry.getValue();
			if (CsvKeyValue.isDigit(entry.getKey())) {
				builder.append("\t[" + entry.getKey() + "] = " + kv.toString());	
			} else {
				builder.append("\t[\"" + entry.getKey() + "\"] = " + kv.toString());
			}
		}
		
		builder.append("};\r\n\r\n\r\nreturn tableInfo");
	}
	
	public void write(String filePath) {
		try {
			FileWriter write = new FileWriter(filePath);
			write.write(builder.toString());
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class Common {
	 public static String getEncoding(String str) {    
         String encode = "GB2312";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s = encode;    
                return s;    
             }    
         } catch (Exception exception) {    
         }    
         encode = "ISO-8859-1";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s1 = encode;    
                return s1;    
             }    
         } catch (Exception exception1) {    
         }    
         encode = "UTF-8";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s2 = encode;    
                return s2;    
             }    
         } catch (Exception exception2) {    
         }    
         encode = "GBK";    
        try {    
            if (str.equals(new String(str.getBytes(encode), encode))) {    
                 String s3 = encode;    
                return s3;    
             }    
         } catch (Exception exception3) {    
         }    
        return "";    
     }    
	 
	 public static String gb2312ToUtf8(String str) {

	        String urlEncode = "" ;

	        try {

	            urlEncode = URLEncoder.encode (str, "UTF-8" );

	        } catch (UnsupportedEncodingException e) {

	            e.printStackTrace();

	        }

	        return urlEncode;

	}

		/**
		 * 判断文件的编码格式
		 * @param fileName :file
		 * @return 文件编码格式
		 * @throws Exception
		 */
		public static String codeString(String fileName) throws Exception{
			BufferedInputStream bin = new BufferedInputStream(
			new FileInputStream(fileName));
			int p = (bin.read() << 8) + bin.read();
			String code = null;
			
			switch (p) {
				case 0xefbb:
					code = "UTF-8";
					break;
				case 0xfffe:
					code = "Unicode";
					break;
				case 0xfeff:
					code = "UTF-16BE";
					break;
				default:
					code = "GBK";
			}
			
			return code;
		}

}




