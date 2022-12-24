package test.sdc.a2;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sdc.a2.MemAppender;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MemAppenderTest {
    private MemAppender memAppender;
    private Logger logger;
    @Before
    public void before(){
        memAppender=MemAppender.getInstance(new ArrayList<>(),new SimpleLayout());
        logger= Logger.getLogger("testLogger");
        logger.addAppender(memAppender);
    }
    @Test
    public void testSingleInstance(){
        MemAppender memAppender2=MemAppender.getInstance(new ArrayList<>());
        assertEquals("fail to implement the singleton pattern",memAppender,memAppender2);
    }

    @Test
    public void testSupplyLayout(){
        LogManager.shutdown();
        memAppender=MemAppender.getInstance(new LinkedList<>(),new SimpleLayout());
    }
    @Test
    public void testGetSetMaxSize(){
        memAppender.setMaxSize(1000);
        assertEquals(1000,memAppender.getMaxSize());
        memAppender.setMaxSize(-1);
        assertEquals(0,memAppender.getMaxSize());
        memAppender.setMaxSize(0);
        assertEquals(0,memAppender.getMaxSize());
    }
    @Test
    public void testAppendInZeroMaxSize(){
        memAppender.setMaxSize(0);
        logger.info("123");
    }
    @Test
    public void testGetDiscardedLogsCount(){
        assertEquals(0,memAppender.getDiscardedLogCount());
        memAppender.setMaxSize(10);
        for(int i=0;i<20;++i)
            logger.info("info");
        assertEquals(10,memAppender.getDiscardedLogCount());
    }
    @Test
    public void testGetCurrentLogs(){
        List<LoggingEvent> currentLogs = memAppender.getCurrentLogs();
        try {
            currentLogs.add(new LoggingEvent(null,logger,null,"this message should not be there",null));
            assertTrue("fail to implement unmodifiable ",false);
        }catch (UnsupportedOperationException e){
        }
    }
    @Test
    public void testGetEventStrings(){
        String lineSeparator=System.lineSeparator();
        logger.info("ABCDE");
        logger.info("12345");
        String[] expected={"INFO - ABCDE"+lineSeparator,"INFO - 12345"+lineSeparator};
        List<String> eventStrings = memAppender.getEventStrings();
        try {
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i],eventStrings.get(i));
            }
            eventStrings.add("123");
            assertTrue("fail to implement unmodifiable ",false);
        }catch (UnsupportedOperationException e){

        }
    }
    @Test
    public void testPrintLogs(){
        logger.setLevel(Level.ALL);
        logger.debug("this is a debug message");
        logger.info("this is a info message");
        logger.error("this is an error message");
        logger.fatal("this is a fatal message");
        logger.warn("this is a warn message");
        memAppender.printLogs();
        assertEquals(0,memAppender.getCurrentLogs().size());
        memAppender.setLayout(null);
        logger.info("this is a info message");
        memAppender.printLogs();
    }

    @Test
    public void testgetCachedSize(){
        String msg="this is a message";
        logger.debug(msg);
        logger.debug(msg);
        assertEquals(msg.length()*2,memAppender.getCachedLogSize());
    }
    @After
    public void after(){
        LogManager.shutdown(); //close the appender after each test
    }
}
