package com.ameda.kisevu.server;


/*
*
@author ameda
@project gRPC
*
*/
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class GreetingServer {
    public static void main(String[] args) throws IOException,
            InterruptedException {
        System.out.println("Hello gRPC");
        Server server = ServerBuilder
                .forPort(50051)
                .addService(new GreetServiceImpl())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( ()->{
            System.out.println("Received shutdown request.");
            server.shutdown();
            System.out.println("Successfully stopped the server.");
        } ));
        server.awaitTermination();
    }
}