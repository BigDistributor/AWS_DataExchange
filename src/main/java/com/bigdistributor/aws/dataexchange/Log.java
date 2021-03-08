package com.bigdistributor.aws.dataexchange;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log implements Serializable {

    private Logger logger;

    public Log(){
        this("");
    }

    public Log(String name) {
        Logger logger = Logger.getLogger(name);
        this.logger = logger;
    }

    public Log(Logger logger) {
        this.logger = logger;
    }

    public static Log getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        return new Log(logger);
    }

    public static Logger getRoot() {
        return LogManager.getLogManager().getLogger("");
    }

    public static void setLevel(Level level) {
        Log.getRoot().setLevel(level);
    }

     public void info(String string) {
        logger.log(Level.INFO, string);
    }

    public void debug(String string) {
        logger.log(Level.FINE, string);
    }

    public void error(String string) {
        logger.log(Level.SEVERE, string);
    }

}
