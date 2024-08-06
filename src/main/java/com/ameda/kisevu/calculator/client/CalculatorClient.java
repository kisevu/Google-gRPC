package com.ameda.kisevu.calculator.client;/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.calculator.CalculatorServiceGrpc;
import com.ameda.kisevu.calculator.SumRequest;
import com.ameda.kisevu.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost",50052)
                .usePlaintext()
                .build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient =
                CalculatorServiceGrpc.newBlockingStub(channel);
        SumRequest request = SumRequest.newBuilder()
                        .setFirstNumber(10)
                                .setSecondNumber(30)
                                        .build();
        SumResponse response = calculatorClient.sum(request);
        System.out.println(request.getFirstNumber()+" "+" "+ request.getSecondNumber()+" = "+response.getSumResult());
        channel.shutdown();
    }
}
