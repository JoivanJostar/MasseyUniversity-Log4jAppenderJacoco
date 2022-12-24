package sdc.a2;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Date;

public class VelocityLayout extends Layout {
    private String template;

    public VelocityLayout() {
        setTemplate("$t | [$p] $c $d:  $m $n");
    }

    public VelocityLayout(String template) {
        setTemplate(template);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String format(LoggingEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLoggerName());//category
        context.put("d", new Date(event.getTimeStamp()).toString());//date
        context.put("m", event.getMessage()); //msg
        context.put("p", event.getLevel()); //priority
        context.put("t", event.getThreadName());//thread
        context.put("n", System.lineSeparator()); //new line
        StringWriter stringWriter = new StringWriter();
        try {
            Velocity.evaluate(context, stringWriter, "VelocityLayoutTemplate", template);
        }catch (Exception e){
            throw e;
        }

        return stringWriter.toString();
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {

    }
}
