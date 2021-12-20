package com.grpc.metadata;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.Uninterruptibles;
import com.grpc.fakeDB.AccountDatabase;
import com.grpc.models.Balance;
import com.grpc.models.BalanceCheckRequest;
import com.grpc.models.BankServiceGrpc.BankServiceImplBase;
import com.grpc.models.DepositRequest;
import com.grpc.models.ErrorMessage;
import com.grpc.models.Money;
import com.grpc.models.WithdrawRequest;
import com.grpc.models.WithdrawalError;
import com.grpc.rpcTypes.CashStreamingRequest;

import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class MetadataService extends BankServiceImplBase {

	 @Override
	    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
	        int accountNumber = request.getAccountNumber();
	        int amount = AccountDatabase.getBalance(accountNumber);

	        UserRole userRole = ServerConstants.CTX_USER_ROLE.get();
	        UserRole userRole1 = ServerConstants.CTX_USER_ROLE1.get();
	        amount = UserRole.PRIME.equals(userRole) ? amount : (amount - 15);

	        System.out.println( userRole + " : " + userRole1  );

	        Balance balance = Balance.newBuilder()
	                .setAmount(amount)
	                .build();
	        responseObserver.onNext(balance);
	        responseObserver.onCompleted();
	    }

	 @Override
	    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
	        int accountNumber = request.getAccountNumber();
	        int amount = request.getAmount(); //10, 20, 30..
	        int balance = AccountDatabase.getBalance(accountNumber);

	        if(amount < 10 || (amount % 10) != 0){
	            Metadata metadata = new Metadata();
	            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
	            WithdrawalError withdrawalError = WithdrawalError.newBuilder().setAmount(balance).setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLES).build();
	            metadata.put(errorKey, withdrawalError);
	            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
	            return;
	        }

	        if(balance < amount){
	            Metadata metadata = new Metadata();
	            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
	            WithdrawalError withdrawalError = WithdrawalError.newBuilder().setAmount(balance).setErrorMessage(ErrorMessage.INSUFFICIENT_BALANCE).build();
	            metadata.put(errorKey, withdrawalError);
	            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
	            return;
	        }

	        // all the validations passed
	        for (int i = 0; i < (amount/10); i++) {
	            Money money = Money.newBuilder().setValue(10).build();
	            responseObserver.onNext(money);
	            AccountDatabase.deductBalance(accountNumber, 10);
	        }
	        responseObserver.onCompleted();
	    }

	    @Override
	    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
	        return new CashStreamingRequest(responseObserver);
	    }



}
