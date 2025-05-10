package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

	// Log4j2 logger
	private static final Logger log = LogManager.getLogger(Log.class);

	// Static block runs once when the class is loaded

	public static void startTestCase(String sTestCaseName) {
		log.info("======= " + sTestCaseName + " TEST START =======");
	}

	public static void endTestCase(String sTestCaseName) {
		log.info("======= " + sTestCaseName + " TEST END =======");
	}

	public static void info(String message) {
		log.info(message);
	}

	public static void warn(String message) {
		log.warn(message);
	}

	public static void error(String message) {
		log.error(message);
	}

	public static void fatal(String message) {
		log.fatal(message);
	}

	public static void debug(String message) {
		log.debug(message);
	}

	public static void clearLogs() {
		try {
			new FileWriter("logs/test.log", false).close(); // 'false' = overwrite mode
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
