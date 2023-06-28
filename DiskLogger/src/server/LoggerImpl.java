package server;

import service.ILogger;

public class LoggerImpl implements ILogger {

    public LoggerImpl() {}

    public void registraDato(int dato) {
        LoggerThread thread = new LoggerThread(dato);
        thread.start();
    }
    
}
