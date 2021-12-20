package com.grpc.client.rpcTypes;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.grpc.models.Balance;
import com.grpc.models.BalanceCheckRequest;
import com.grpc.models.BankServiceGrpc;
import com.grpc.models.DepositRequest;
import com.grpc.models.WithdrawRequest;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup()  {

        ManagedChannel managedChannel = NettyChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();
        Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
        System.out.println( "Received : " + balance);
    }
    
    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build();
        this.blockingStub.withdraw(withdrawRequest)
                .forEachRemaining(money -> System.out.println("Received : " + money.getValue()));
    }
    
    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(60).build();
        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }
    
    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder().setAccountNumber(3).setAmount(10).build();
            streamObserver.onNext(depositRequest);
        }
        streamObserver.onCompleted();
        latch.await();
    }

}
