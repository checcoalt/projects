package server;

import service.ILogger;

public class LoggerServer {

    public static void main(String[] args) {

        if (args.length < 1){
            System.err.println("Usage: java server.LoggerServer <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        
        // Run dello skeleton
        ILogger logger = new LoggerImpl();
        LoggerSkeleton skeleton = new LoggerSkeleton(logger, port);

        System.out.println("[LOGGER]: server started ...");

        skeleton.runSkeleton();

    }
    
}
