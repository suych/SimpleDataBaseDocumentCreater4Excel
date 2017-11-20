package org.suych.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {

	private static String file = "./src/jdbc.properties";

	public static Map<String, String> getProperties() {
		Map<String, String> result = new HashMap<String, String>();
		InputStream in = null;
		Properties prop = new Properties();
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			prop.load(new InputStreamReader(in, "UTF-8"));
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = prop.getProperty(key);
				result.put(key, value);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void setProperty(String key, String value) {
		Properties prop = new Properties();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true); // true表示追加打开
			prop.setProperty(key, value);
			prop.store(fos, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void main(String[] args) {
	// setProperty(file, "123", "456");
	// Map<String, String> map =
	// getProperties("./src/main/resources/jdbc.properties");
	// for (Entry<String, String> entry : map.entrySet()) {
	// System.out.println(entry.getKey());
	// System.out.println(entry.getValue());
	// }
	// }
}
