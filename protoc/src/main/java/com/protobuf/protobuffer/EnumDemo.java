package com.protobuf.protobuffer;

import com.protobuf.models.Bike;
import com.protobuf.models.Bodystyle;

public class EnumDemo {
	
	public static void main(String[] args) {
		Bike bike1 = Bike.newBuilder().setMake("Honda").setModel("Sedan").setYear(2021).build();
		System.out.println(bike1);
		System.out.println("BodyStyle=> "+bike1.getBodystyle1()+"\r\n");
		
		Bike bike2 = Bike.newBuilder().setMake("Honda").setModel("Sedan").setYear(2021).setBodystyle1(Bodystyle.MOUNTAIN).build();
		System.out.println(bike2);
	}
	

}
