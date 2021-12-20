package com.grpc.deadLine;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.grpc.client.rpcTypes.MoneyStreamingResponse;
import com.grpc.models.Balance;
import com.grpc.models.BalanceCheckRequest;
import com.grpc.models.BankServiceGrpc;
import com.grpc.models.WithdrawRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeadlineClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .intercept(new DeadlineInterceptor())
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
        try{
            Balance balance = this.blockingStub
            		//.withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getBalance(balanceCheckRequest);
            System.out.println( "Received : " + balance.getAmount());
        }catch (StatusRuntimeException e){
            // go with default values
        }

    }

    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setAccountNumber(6)
                .setAmount(50).build();
        try{
            this.blockingStub
                    //.withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(withdrawRequest)
                    .forEachRemaining(money -> System.out.println("Received : " + money.getValue()));
        }catch (StatusRuntimeException e){
            //
        }

    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build();
        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }

}
