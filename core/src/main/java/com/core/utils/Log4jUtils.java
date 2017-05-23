package com.core.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.IOException;

public class Log4jUtils {
    public static final String HEART_LOG_FILE = Constants.SD_CARD + "/log/heart.txt";
    public static final Logger HEART_LOGGER = Logger.getLogger("HEART");

    static {
        initHeartLogger();
    }

    private static void initHeartLogger() {
        try {
            PatternLayout patternLayout = new PatternLayout();
            patternLayout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %m %n");
            RollingFileAppender fileAppender = new RollingFileAppender(patternLayout, HEART_LOG_FILE);
            fileAppender.setMaxBackupIndex(20);
            fileAppender.setMaximumFileSize(1024 * 1024);
            HEART_LOGGER.addAppender(fileAppender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
