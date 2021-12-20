package com.grpc.deadLine;

import com.google.common.util.concurrent.Uninterruptibles;
import com.grpc.fakeDB.AccountDatabase;
import com.grpc.models.Balance;
import com.grpc.models.BalanceCheckRequest;
import com.grpc.models.BankServiceGrpc.BankServiceImplBase;
import com.grpc.models.DepositRequest;
import com.grpc.models.Money;
import com.grpc.models.WithdrawRequest;
import com.grpc.rpcTypes.CashStreamingRequest;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class DeadlineService extends BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDatabase.getBalance(accountNumber))
                .build();
        //simulate time-consuming call
        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount(); //10, 20, 30..
        int balance = AccountDatabase.getBalance(accountNumber);

        if(balance < amount){
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only " + balance);
            responseObserver.onError(status.asRuntimeException());
            return;
        }
        // all the validations passed
        for (int i = 0; i < (amount/10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            //simulate time-consuming call
            Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
            if(!Context.current().isCancelled()){
                responseObserver.onNext(money);
                System.out.println("Delivered $10");
                AccountDatabase.deductBalance(accountNumber, 10);
            }else{
                break;
            }
        }
        System.out.println("Completed");
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }



}
