package com.ameda.kisevu.server;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    //unary
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extracted the fields we need.
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        //create the response
        String result = "Hello "+firstName;

        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();
        //send the response
         responseObserver.onNext(response);
         //complete the RPC call
        responseObserver.onCompleted();
    }
    //server streaming

    @Override
    public void greetManyTimes(GreetManyTimesRequest request,
                               StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        try{
            for(int x =0; x<10;x++){
                String result = "Hi "+firstName+", sent response number: "+x;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(response);
                Thread.sleep(1000);
            }
        }catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }finally {
            responseObserver.onCompleted();
        }

    }
}
