package com.ameda.kisevu.client;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("I am grpc client.");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost",50051)
                .usePlaintext()  // deactivates ssl during DEV
                .build();
        System.out.println("Creating a stub.");

        //created a greet service client (blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient =
                GreetServiceGrpc.newBlockingStub(channel);
        //created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Kevin")
                .setLastName("Ameda")
                .build();

        //same for greet request
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //call RPC and get back a GreetResponse (protocol buffers)
        GreetResponse greetResponse  = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());
        System.out.println("shutting down channel.");
        channel.shutdown();
    }
    //Unary RPC calls are basic request/response architecture common with REST
    //Unary RPC calls are very well suited when your data is small.
    // We should start with unary RPC calls when writing APIs and use streaming if
    //performance is an issue.
    //Unary RPC calls are defined using Protocol buffers

}
