package sdc.a2;

public interface MemAppenderMBean {
    public int getMaxSize();
    public long getDiscardedLogCount();
    public long getCachedLogSize();
}
