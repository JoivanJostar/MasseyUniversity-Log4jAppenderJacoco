package sdc.a2;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MemAppender extends AppenderSkeleton  implements MemAppenderMBean{
    private final int DEFAULT_MAX_SIZE=50;
    private int maxSize=DEFAULT_MAX_SIZE;
    private long discardedLogCount =0;
    private static MemAppender appenderInstance=null;
    private List<LoggingEvent> loggingEventList;
    private static int ID=0; //MBean ID


    private MemAppender(List<LoggingEvent> loggingEventList,Layout layout) {
        this.loggingEventList = loggingEventList;
        setLayout(layout);
        this.maxSize=DEFAULT_MAX_SIZE;
        try{
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("sdc.a2:type=MemAppender"+ID++);
            mbs.registerMBean(this, name);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //for JMX MBean
    @Override
    public int getMaxSize() {

        return this.maxSize;
    }

    @Override
    public long getDiscardedLogCount(){
        return this.discardedLogCount;
    }

    @Override
    public long getCachedLogSize() {
        long size=0;
        for (LoggingEvent loggingEvent : loggingEventList) {
            size+=((String)(loggingEvent.getMessage())).length();
        }
        return size;
    }

    public void setMaxSize(int maxSize) {
        if(maxSize<0)
            this.maxSize=0;
        else
            this.maxSize = maxSize;
    }

    @Override
    public void setLayout(Layout layout) {
        this.layout=layout;
    }

    /**
     * getInstance with no Layout
     * @param list
     * @return
     */
    public static  MemAppender getInstance(List<LoggingEvent>list){
        return getInstance(list,null);
    }

    /**
     * getInstance with Layout
     * @param list
     * @param layout
     * @return
     */
    public static synchronized MemAppender getInstance(List<LoggingEvent>list,Layout layout){
        //use class synchronied. To avoid the race conditions in multiThreads environment
        //when executing the following if statement.
        if(appenderInstance == null){
            appenderInstance = new MemAppender(list,layout);
        }
        return appenderInstance;
    }

    /**
     * get the current logs
     * @return an unmodifiableList of the Logging Events
     */
    public List<LoggingEvent> getCurrentLogs(){
        return Collections.unmodifiableList(this.loggingEventList);
    }

    /**
     * get the strings of events in the MemAppender
     * @return an unmodifiableList of the Logging Events Strings
     * formatted by layout.
     */
    public List<String> getEventStrings(){
        LinkedList<String> stringLinkedList = new LinkedList<>();
        if(layout!=null){
            for (LoggingEvent loggingEvent : loggingEventList) {
                stringLinkedList.add(layout.format(loggingEvent));
            }
            return Collections.unmodifiableList(stringLinkedList);
        }else{
            return Collections.unmodifiableList(stringLinkedList);
        }

    }

    /**
     * print the logging events to the console
     * using the layout and then clear the logs.
     */
    public void printLogs(){
        List<String> eventStrings = getEventStrings();
        for (String eventString : eventStrings) {
            System.out.println(eventString);
        }
        this.loggingEventList.clear();
    }

    @Override
    protected void append(LoggingEvent event) {
        if(this.maxSize==0) return;
        if(this.loggingEventList.size()>=this.maxSize){
            this.loggingEventList.remove(0);
            this.discardedLogCount++;
            this.loggingEventList.add(event);
        }else{
            this.loggingEventList.add(event);
        }
    }

    /**
     * clear the instance
     */
    @Override
    public void close() {
        this.loggingEventList.clear();
        this.loggingEventList=null;
        this.layout=null;
        this.maxSize=0;
        this.discardedLogCount=0;
        appenderInstance=null;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

}
