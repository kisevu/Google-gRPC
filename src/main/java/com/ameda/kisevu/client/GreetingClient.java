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

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Kevin"))
                .build();
         //streaming responses in a blocking manner
        greetClient.greetManyTimes(greetManyTimesRequest)
                        .forEachRemaining(greetManyTimesResponse -> {
                            System.out.println(greetManyTimesResponse.getResult());
                        });
        System.out.println("shutting down channel.");
        channel.shutdown();
    }
    /*
    *  Streaming server API client implementation and this testable with the other
    *  codes for the Streaming Server API which was implemented earlier on.
    *  The server sends a stream of messages on a single request, so the client sends a single
    *  request.
    * */

}
