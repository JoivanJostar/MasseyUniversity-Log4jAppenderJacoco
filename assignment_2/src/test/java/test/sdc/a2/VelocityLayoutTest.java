package test.sdc.a2;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.app.Velocity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sdc.a2.MemAppender;
import sdc.a2.VelocityLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VelocityLayoutTest {
    Logger logger;
    @Before
    public void before(){
        logger= Logger.getLogger("VelocityLayoutTestLogger");
    }
    @After
    public void after(){
        LogManager.shutdown();
    }

    @Test
    public void testVelocityLayoutWithFileAppender() throws IOException {
        logger.addAppender(new FileAppender(new VelocityLayout(),"VelocityLayoutTestLogs.txt",true));
        logger.info("this is a test msg");
    }

    @Test
    public void testVelocityLayoutWithTemplate() throws IOException {
        logger.addAppender(new FileAppender(new VelocityLayout("[thread:$t] [$p] $d $c $m $n"),"VelocityLayoutTestLogs.txt",true));
        logger.info("this is a test msg");
        logger.debug("this is a debug msg");
    }
    @Test
    public void testFormat(){
        LoggingEvent loggingEvent = new LoggingEvent(null, logger, Level.INFO, "this is a msg", null);
        VelocityLayout layout=new VelocityLayout("$p $m$n");
        String format = layout.format(loggingEvent);
        String expected="INFO this is a msg"+System.lineSeparator();
        Assert.assertEquals(expected,format);
    }
    @Test
    public void testWithMemAppender(){
        MemAppender memAppender=MemAppender.getInstance(new ArrayList<>(),new VelocityLayout("[thread:$t] [$p] $c $m $n"));
        logger.addAppender(memAppender);
        logger.info("this is a info");
        logger.debug("this is a debug");
        String expected="[thread:main] [INFO] VelocityLayoutTestLogger this is a info "+System.lineSeparator();
        List<String> eventStrings = memAppender.getEventStrings();
        Assert.assertEquals(expected,eventStrings.get(0));
        memAppender.printLogs();
    }
}
