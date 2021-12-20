package com.grpc.loadbalancing;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer3 {
	
	 public static void main(String[] args) throws IOException, InterruptedException {

	        Server server = ServerBuilder.forPort(8585)
	                .addService(new BankService())
	                .build();

	        server.start();
	        server.awaitTermination();
	    }

}
