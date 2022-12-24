package test.sdc.a2;

import org.apache.log4j.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sdc.a2.MemAppender;
import sdc.a2.VelocityLayout;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.LinkedList;


public class StressTest {

    private int logNums = 1000000;

    private Logger stressTestConsoleAppenderLoggger = null;
    private Logger stressTestFileAppenderLogger = null;
    private Logger stressTestMemAppenderLogger = null;

    private MemAppender memAppender = null;
    private File stressTestFileAppenderFile = null;
    private String fileAppenderFileName = "stressTestFileAppenderOutputLogs.log";

    private String velocityLayoutTemplate = "$t [$p] $c $d: $m$n";
    private String patternLayoutTemplate = "%t [%p] %c %d: %m%n";


    @Before
    public void before() throws Exception {
        //init memAppenderLogger
        stressTestMemAppenderLogger = Logger.getLogger("TestMemAppenderLogger");
        //this need adding appender in test methods

        // Init File Logger
        stressTestFileAppenderFile = new File(fileAppenderFileName);
        stressTestFileAppenderLogger = Logger.getLogger("TestFileAppenderLogger");

        //Init Console Logger
        stressTestConsoleAppenderLoggger = Logger.getLogger(" TestConsoleAppenderLogger");
        Thread.sleep(2000);
    }

    @After
    public void after() throws Exception {
        // clear the log
        PrintWriter writer = new PrintWriter(stressTestFileAppenderFile);
        writer.print("");
        writer.close();
        //reset the Logger and MemAppender binded on it
        stressTestConsoleAppenderLoggger = null;
        stressTestFileAppenderLogger = null;
        stressTestMemAppenderLogger = null;
        stressTestFileAppenderFile = null;
        LogManager.shutdown(); //this will call the Appender's close() methods

    }

    public void testAppenderPerformance(Logger logger,int maxSize){
        long begin = System.currentTimeMillis();
        for(int i=0;i<maxSize;i++){
            logger.info("Test INFO " + i);
        }
        long beforeMaxSize = System.currentTimeMillis();
        for (int i = maxSize; i < logNums; i++) {
            logger.info("Test INFO " + i);
        }
        long afterMaxSize = System.currentTimeMillis();
        System.out.println("Insert "+logNums+" "+"Before reach the MaxSize "+maxSize +" Time Consuming: "+(beforeMaxSize-begin));
        System.out.println("Insert "+logNums+" "+"After reach the MaxSize "+maxSize +" Time Consuming: "+(afterMaxSize-beforeMaxSize));
        System.out.println("Insert "+logNums+" "+"TotalTime: "+(afterMaxSize-begin));
    }
    @Test
    public void testAppenderPerformance1XWithArrayListMemAppenderPatternLayout() throws Exception {
        //Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance1XWithArrayListMemAppenderPatternLayout() -------");
        int maxSize=100000;
        memAppender = MemAppender.getInstance(new ArrayList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
        //Thread.sleep(10000);
    }
    @Test
    public void testAppenderPerformance2XWithArrayListMemAppenderPatternLayout() throws Exception {
       // Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance2XWithArrayListMemAppenderPatternLayout() -------");
        int maxSize=200000;
        memAppender = MemAppender.getInstance(new ArrayList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
        //Thread.sleep(20000);
    }
    @Test
    public void testAppenderPerformance5XWithArrayListMemAppenderPatternLayout() throws Exception {
        //Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance5XWithArrayListMemAppenderPatternLayout() -------");
        int maxSize=500000;
        memAppender = MemAppender.getInstance(new ArrayList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
        //Thread.sleep(20000);
    }

    @Test
    public void testAppenderPerformance1XWithLinkedListMemAppenderPatternLayout() throws Exception {
        //Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance1XWithLinkedListMemAppenderPatternLayout() -------");
        int maxSize=100000;
        memAppender = MemAppender.getInstance(new LinkedList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
        //Thread.sleep(10000);
    }
    @Test
    public void testAppenderPerformance2XWithLinkedListMemAppenderPatternLayout() throws Exception {
       // Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance2XWithLinkedListMemAppenderPatternLayout() -------");
        int maxSize=200000;
        memAppender = MemAppender.getInstance(new LinkedList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
       // Thread.sleep(10000);
    }
    @Test
    public void testAppenderPerformance5XWithLinkedListMemAppenderPatternLayout() throws Exception {
       // Thread.sleep(20000); // 15s : wait JConsole connect
        System.out.println("-------testAppenderPerformance5XWithLinkedListMemAppenderPatternLayout() -------");
        int maxSize=500000;
        memAppender = MemAppender.getInstance(new LinkedList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testAppenderPerformance(stressTestMemAppenderLogger,maxSize);
      //  Thread.sleep(10000);
    }



    public void testLayoutPerformance(Logger logger){
        long begin = System.currentTimeMillis();
        for(int i=0;i<logNums;i++){
            logger.info("Test INFO " + i);
        }
        if(logger==stressTestMemAppenderLogger){
            memAppender.printLogs();
        }
        long end = System.currentTimeMillis();
        System.out.println("output "+logNums+" logs "+"TotalTime: "+(end-begin));
    }

    ///test Velocity layout performance
    @Test
    public void testLayoutPerformanceWithArrayListMemAppenderPatternLayout() throws Exception {
        //Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithArrayListMemAppenderPatternLayout() -------");
        int maxSize=logNums;
        memAppender = MemAppender.getInstance(new ArrayList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testLayoutPerformance(stressTestMemAppenderLogger);
       // Thread.sleep(10000);
    }

    @Test
    public void testLayoutPerformanceWithArrayListMemAppenderVelocityLayout() throws Exception {
       // Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithArrayListMemAppenderVelocityLayout() -------");
        int maxSize=logNums;
        memAppender = MemAppender.getInstance(new ArrayList<>(),new VelocityLayout(velocityLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testLayoutPerformance(stressTestMemAppenderLogger);
       // Thread.sleep(10000);
    }


    @Test
    public void testLayoutPerformanceWithLinkedListMemAppenderPatternLayout() throws Exception {
       // Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithLinkedListMemAppenderPatternLayout() -------");
        int maxSize=logNums;
        memAppender = MemAppender.getInstance(new LinkedList<>(),new PatternLayout(patternLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testLayoutPerformance(stressTestMemAppenderLogger);
       // Thread.sleep(10000);
    }
    @Test
    public void testLayoutPerformanceWithLinkedListMemAppenderVelocityLayout() throws Exception {
       // Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithLinkedListMemAppenderVelocityLayout() -------");
        int maxSize=logNums;
        memAppender = MemAppender.getInstance(new LinkedList<>(),new VelocityLayout(velocityLayoutTemplate));
        stressTestMemAppenderLogger.addAppender(memAppender);
        memAppender.setMaxSize(maxSize);
        testLayoutPerformance(stressTestMemAppenderLogger);
        //Thread.sleep(10000);
    }


    @Test
    public void testLayoutPerformanceWithFileAppenderPatternLayout() throws Exception {
        //Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithConsoleAppenderPatternLayout()() -------");
        stressTestFileAppenderLogger.addAppender(new FileAppender(new PatternLayout(patternLayoutTemplate), fileAppenderFileName));
        testLayoutPerformance(stressTestFileAppenderLogger);
      // Thread.sleep(10000);
    }
    @Test
    public void testLayoutPerformanceWithFileAppenderVelocityLayout() throws Exception {
       // Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithFileAppenderVelocityLayout() -------");
        stressTestFileAppenderLogger.addAppender(new FileAppender(new VelocityLayout(velocityLayoutTemplate), fileAppenderFileName));
        testLayoutPerformance(stressTestFileAppenderLogger);
        //Thread.sleep(10000);
    }

    @Test
    public void testLayoutPerformanceWithConsoleAppenderPatternLayout() throws Exception {
      //  Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithConsoleAppenderPatternLayout() -------");
        stressTestConsoleAppenderLoggger.addAppender(new ConsoleAppender(new PatternLayout(patternLayoutTemplate)));
       testLayoutPerformance(stressTestConsoleAppenderLoggger);
        //Thread.sleep(10000);
    }

    @Test
    public void testLayoutPerformanceWithConsoleAppenderVelocityLayout() throws Exception {
      //  Thread.sleep(20000);
        System.out.println("-------testLayoutPerformanceWithConsoleAppenderVelocityLayout() -------");
        stressTestConsoleAppenderLoggger.addAppender(new ConsoleAppender(new VelocityLayout(velocityLayoutTemplate)));
        testLayoutPerformance(stressTestConsoleAppenderLoggger);
       // Thread.sleep(10000);
    }



}