package com.protobuf.protobuffer;

import com.protobuf.models.Car;
import com.protobuf.models.Dealer;

public class MapDemo {
	
	public static void main(String[] args) {
		Car car = Car.newBuilder().setMake("Tesla").setModel("SUV").setYear(2020).build();
		
		Dealer dealer = Dealer.newBuilder().putCars(100, car).build();
		System.out.println(dealer);
		
		System.out.println("Cars count "+dealer.getCarsCount());
		System.out.println("Car => "+dealer.getCarsMap().get(100));
	}

}
