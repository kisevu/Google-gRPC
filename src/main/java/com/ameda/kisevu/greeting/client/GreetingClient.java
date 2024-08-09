package com.ameda.kisevu.greeting.client;

/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    private  void run(){
        ManagedChannel  channel = ManagedChannelBuilder
                .forAddress("localhost",50051)
                .usePlaintext()
                .build();
//        doUnaryCall(channel);
//        doServerStreamingCall(channel);
        doClientStreamingCall(channel);

        System.out.println("shutting down the channel");
        channel.shutdown();
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        //created an async client
        GreetServiceGrpc.GreetServiceStub asyncClient =
                GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1); // blocking with a latch, esp on async programming.

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse longGreetResponse) {
                //we get response from server
                System.out.println("Received a response from the server");
                System.out.println(longGreetResponse.getResult());
                //onNext will be called only once
            }

            @Override
            public void onError(Throwable throwable) {
                //we get an error from server
            }

            @Override
            public void onCompleted() {
                //server done sending us data
                //onCompleted called right after onNext
                System.out.println("Server has completed sending us our result.");
                latch.countDown();
            }
        });
        //streaming message #1
        System.out.println("sending message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Pers Lindoe").build())
                .build());

        //streaming message #2
        System.out.println("sending message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Emma").build())
                .build());

        //streaming message #3
        System.out.println("sending message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Torunn").build())
                .build());
        //we're basically telling the server, a client is done sending data
        requestObserver.onCompleted();
        try{
            /*
            *  here, the latch gives time for the server to send a response
            * failure the response has not time to be retrieved from the server.
             * */
            latch.await(3L, TimeUnit.SECONDS);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient =
                GreetServiceGrpc.newBlockingStub(channel);

        //server streaming
        //we prepare request
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Pers Lindoe"))
                .build();
        // we stream the responses (in a blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });
    }

    private void doUnaryCall(ManagedChannel channel){
        GreetServiceGrpc.GreetServiceBlockingStub greetClient =
                GreetServiceGrpc.newBlockingStub(channel);

        //created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Pers Lindoe")
                .setLastName("Kisevu")
                .build();

        //same for GreetRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //call the RPC and get back a GreetResponse (protocol buffers)
        GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());
    }

    public static void main(String[] args) {
        System.out.println("Hello I am gRPC client: ");
        GreetingClient main = new GreetingClient();
        main.run();
    }
}
