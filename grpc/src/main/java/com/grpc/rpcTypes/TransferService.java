package com.grpc.rpcTypes;


import com.grpc.models.TransferRequest;
import com.grpc.models.TransferResponse;
import com.grpc.models.TransferServiceGrpc.TransferServiceImplBase;

import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
