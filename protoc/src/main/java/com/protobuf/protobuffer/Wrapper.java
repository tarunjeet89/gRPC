package com.protobuf.protobuffer;

import com.google.protobuf.Int32Value;
import com.protobuf.models.Address;
import com.protobuf.models.Car;
import com.protobuf.models.PersonWrapper;

public class Wrapper {
	
	public static void main(String[] args) {
		Address address = Address.newBuilder().setCity("kurali").setPostbox(140103).setStreet("Sabji Mandi").build();
		Car car = Car.newBuilder().setMake("Tesla").setModel("SUV").setYear(2020).build();
		
		PersonWrapper person = PersonWrapper.newBuilder()
				.setAge(Int32Value.newBuilder().setValue(45))
				.setAddress(address)
				.setCar(car)
				.setName("Tarun")
				.build();
		
		System.out.println(person);
		System.out.println(person.hasAge());
	}

}
