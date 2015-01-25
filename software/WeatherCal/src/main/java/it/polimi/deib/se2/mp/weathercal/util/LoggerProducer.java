/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.se2.mp.weathercal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author paolo
 * @author miglie
 */
public class LoggerProducer {

    Formatter f;

    public LoggerProducer() {
        this.f = new SimpleFormatter(){
            private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            @Override
            public String format(LogRecord record) {
                StringBuilder builder = new StringBuilder(1000);
                builder.append(df.format(new Date(record.getMillis()))).append(" - ");
                builder.append("[").append(record.getLevel().getLocalizedName().toLowerCase()).append("] - ");
                builder.append("[").append(record.getLoggerName()).append("] - ");
                builder.append(formatMessage(record));
                builder.append("\n");
                String trace = "";
                if(record.getThrown() != null)
                    for(StackTraceElement el :record.getThrown().getStackTrace())
                        trace += el.toString() + "\n";
                return builder.toString() + trace;
            }
 
            @Override
            public String getHead(Handler h) {
                return super.getHead(h);
            }

            @Override
            public String getTail(Handler h) {
                return super.getTail(h);
            }
        };
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Logger l = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        for(Handler h: l.getParent().getHandlers()){
            //h.setFormatter(f);
        }
        return l;
    }

}
