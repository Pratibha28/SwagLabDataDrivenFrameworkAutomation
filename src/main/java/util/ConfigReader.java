package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static Properties prop;

	public static Properties loadProperties(String env) {
System.out.println("i am executed");
		prop = new Properties();
		try {
			FileInputStream file = new FileInputStream("G:/eclipse workplace/SwagLabs/src/test/resources/config." + env + ".properties");
			//FileInputStream file = new FileInputStream("G:\\eclipse-workplace2025\\NewSwagLabsCucumbers\\src\\test\\resources\\config.qa.properties");
             prop.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("file not found");
			e.printStackTrace();
		}
		return prop;
	}

	public static String getProperties(String key) {
		 System.out.println("Key requested: " + key);

		    if (prop == null) {
		        System.err.println("ERROR: Properties object 'prop' is null. Make sure to load properties before calling getProperties.");
		        return null;  // or throw an exception if you want
		    }

		    String value = prop.getProperty(key);
		    System.out.println("Value: " + value);
		    return value;
	}}