package com.ameda.kisevu.greeting.server;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {



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


    //client streaming

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
                result+="Hello: "+longGreetRequest.getGreeting().getFirstName()+" \n";
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
