package com.example.CloudStorageDiploma;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {
    public static String getStackTraceAsString(Throwable throwable) throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            return stringWriter.toString();
        }
    }
}
