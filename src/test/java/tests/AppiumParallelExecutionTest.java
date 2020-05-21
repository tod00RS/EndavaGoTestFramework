package tests;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import parallelTesting.AppiumParallelExecution;

public class AppiumParallelExecutionTest {

    //NOT GOOD

    @Parameters({"configFileJson"})
    @BeforeSuite
    public void setEnvParams(String configFileJson) {
        System.setProperty("parallelTesting", "true");
        AppiumParallelExecution.setConfigFileName(configFileJson);
    }

    @Test
    public void parallelExecutionTest() {
//        AppiumParallelExecution appiumParallelExecution = new AppiumParallelExecution();
//        appiumParallelExecution.setConfigFileName("configJsonSamsungS8.json");
//        AppiumParallelExecution appiumParallelExecution2 = new AppiumParallelExecution();
//        appiumParallelExecution.setConfigFileName("configJsonSamsungA50.json");
        new Thread(new AppiumParallelExecution()).start();
//        new Thread(appiumParallelExecution2).start();
    }


}
