package parallelTesting;

import tests.AppiumParallelExecutionTest;

public class AppiumParallelExecution implements Runnable{

    static String configFileName;

    //NOT GOOD
    public AppiumParallelExecution() {
    }

    public static void setConfigFileName(String configFileName) {
        AppiumParallelExecution.configFileName = configFileName;
    }

    public static String getConfigFileName() {
        return configFileName;
    }

    @Override
    public void run() {
        AppiumParallelExecutionTest appiumParallelExecutionTest = new AppiumParallelExecutionTest();
    }

}
