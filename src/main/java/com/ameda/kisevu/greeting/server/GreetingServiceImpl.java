package com.ameda.kisevu.greeting.server;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.GreetServiceGrpc;
import com.ameda.kisevu.LongGreetRequest;
import com.ameda.kisevu.LongGreetResponse;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    //the request are flowing in a stream form, and in async manner
    //We're sending a stream of requests to the server.
    //unlike the rest of the gRPCs, this one returns something instead of returning void or non
    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestObserver =  new StreamObserver<LongGreetRequest>() {
            
            String result = "";

            @Override
            public void onNext(LongGreetRequest longGreetRequest) {
                //client sends a message
                result+="Hello: "+longGreetRequest.getGreeting().getFirstName();
            }

            @Override
            public void onError(Throwable throwable) {
                //client sends an error
            }

            @Override
            public void onCompleted() {
                //client sends is done
                responseObserver.onNext(LongGreetResponse.newBuilder()
                        .setResult(result)
                        .build());
                responseObserver.onCompleted();
                // here we want to return a response through (streamObserver)
            }
        };
        return requestObserver;
    }
}
