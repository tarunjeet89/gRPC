package com.grpc.metadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

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
import io.grpc.stub.MetadataUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientTest {

	 private BankServiceGrpc.BankServiceBlockingStub blockingStub;
	    private BankServiceGrpc.BankServiceStub bankServiceStub;

	    @BeforeAll
	    public void setup(){
	        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
	                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
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
	        for (int i = 0; i < 20; i++) {
	            try{
	                int random = ThreadLocalRandom.current().nextInt(1, 4);
	                System.out.println("Random : " + random);
	                Balance balance = this.blockingStub
	                		   .withCallCredentials(new UserSessionToken("user-secret-" + random + ":standard"))
	                        .getBalance(balanceCheckRequest);
	                System.out.println("Received : " + balance.getAmount());
	            }catch (StatusRuntimeException e){
	                e.printStackTrace();
	            }
	        }
	    }

	    @Test
	    public void withdrawTest(){
	        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
	                .setAccountNumber(6)
	                .setAmount(50).build();
	        try{
	            this.blockingStub
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
