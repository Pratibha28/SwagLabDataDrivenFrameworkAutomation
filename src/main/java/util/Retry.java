package util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    private int count = 0;
    private static final int maxRetryCount = 2; // 🔹 run 1 normal + 2 retries

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxRetryCount) {
            count++;
            return true;
        }
        return false;
    }

    // Helper to check if retries are still available
    public boolean isRetryAvailable() {
        return count < maxRetryCount;
    }
}
