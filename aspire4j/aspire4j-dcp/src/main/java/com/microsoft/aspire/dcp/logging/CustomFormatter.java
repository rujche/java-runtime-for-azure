package com.microsoft.aspire.dcp.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        
        // Get the short class name
        String className = record.getSourceClassName();
        String shortClassName = className.substring(className.lastIndexOf('.') + 1);
        
        // Format the log message
        builder.append("[")
               .append(shortClassName)
               .append("] ")
               .append(record.getLevel())
               .append(": ")
               .append(formatMessage(record))
               .append(System.lineSeparator());
        
        return builder.toString();
    }
}