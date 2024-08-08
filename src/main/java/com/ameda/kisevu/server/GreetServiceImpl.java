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
}
