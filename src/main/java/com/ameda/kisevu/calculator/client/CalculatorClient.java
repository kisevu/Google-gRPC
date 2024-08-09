package com.ameda.kisevu.calculator.client;

/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import com.ameda.kisevu.calculator.CalculatorServiceGrpc;
import com.ameda.kisevu.calculator.ComputeAverageRequest;
import com.ameda.kisevu.calculator.ComputeAverageResponse;
import com.ameda.kisevu.calculator.PrimeNumberDecompositionRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost",50052)
                .usePlaintext()
                .build();
//        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient =
//                CalculatorServiceGrpc.newBlockingStub(channel);
        doUnaryCall(channel);
        doServerStreamingCall(channel);
        doClientStreamingCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
    private void doUnaryCall(ManagedChannel channel){}
    private void doServerStreamingCall(ManagedChannel channel){}

    private void doClientStreamingCall(ManagedChannel channel){
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient =
                CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

         StreamObserver<ComputeAverageRequest> requestObserver =
                 asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse computeAverageResponse) {
                System.out.println("Received a response from the server.");
                System.out.println(computeAverageResponse.getAverage());
            }

            @Override
            public void onError(Throwable throwable) {
                //ignored for now
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending data...");
                latch.countDown();
            }
        });

//        (client streaming at its best)
         for(int x = 0; x< 1000; x++ ){
             requestObserver.onNext(ComputeAverageRequest.newBuilder()
                     .setNumber(x)
                     .build());
         }
         requestObserver.onCompleted();
         try{
             latch.await(3L,TimeUnit.SECONDS);
         }catch (InterruptedException ex){
             ex.printStackTrace();
         }

    }


    public static void main(String[] args) {
        System.out.println("Hello I am a gRPC client...");
        CalculatorClient main = new CalculatorClient();
        main.run();

    }
}
