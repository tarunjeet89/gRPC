package com.grpc.client.rpcTypes;

import java.util.concurrent.CountDownLatch;

import com.grpc.models.Balance;

import io.grpc.stub.StreamObserver;

public class BalanceStreamObserver implements StreamObserver<Balance> {

    private CountDownLatch latch;

    public BalanceStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println("Final Balance : " + balance.getAmount() );
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println( "Server is done!");
        this.latch.countDown();
    }
}
