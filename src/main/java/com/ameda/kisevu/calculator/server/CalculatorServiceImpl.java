package com.ameda.kisevu.calculator.server;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.*;
import com.ameda.kisevu.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        SumResponse sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber()+request.getSecondNumber())
                .build();
        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request,
                                         StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        Long number = request.getNumber();
        Long divisor = 2L;
        while(number > 1){
            if(number % divisor==0){
                number = number / divisor;
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(divisor)
                        .build());
            }else{
                divisor++;
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
   StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {
       int sum = 0, count = 0;

       @Override
       public void onNext(ComputeAverageRequest computeAverageRequest) {
           sum+=computeAverageRequest.getNumber();
           count+=1;
       }

       @Override
       public void onError(Throwable throwable) {
           //ignore for now
       }

       @Override
       public void onCompleted() {
           //compute average
           double avg = (double) sum /count;
           responseObserver.onNext(ComputeAverageResponse.newBuilder()
                   .setAverage(avg)
                   .build());
           responseObserver.onCompleted();
       }
   };
        return requestObserver;
    }
}
