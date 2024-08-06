package com.ameda.kisevu.calculator.server;
/*
*
@author ameda
@project gRPC
*
*/

import com.ameda.kisevu.calculator.CalculatorServiceGrpc;
import com.ameda.kisevu.calculator.SumRequest;
import com.ameda.kisevu.calculator.SumResponse;
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
}
