package com.ameda.kisevu.client;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.DummyServiceGrpc;
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
        DummyServiceGrpc.DummyServiceBlockingStub syncClient =
                DummyServiceGrpc.newBlockingStub(channel);

        System.out.println("shutting down channel.");
        channel.shutdown();
    }
    //Unary RPC calls are basic request/response architecture common with REST
    //Unary RPC calls are very well suited when your data is small.
    // We should start with unary RPC calls when writing APIs and use streaming if
    //performance is an issue.
    //Unary RPC calls are defined using Protocol buffers

}
