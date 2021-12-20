package com.protobuf.protobuffer;

import com.protobuf.models.Address;
import com.protobuf.models.Car;
import com.protobuf.models.Person;

public class Composition {
	
	public static void main(String[] args) {
		Address address = Address.newBuilder().setCity("kurali").setPostbox(140103).setStreet("Sabji Mandi").build();
		Car car = Car.newBuilder().setMake("Honda").setModel("Sedan").setYear(2021).build();
		Person person = Person.newBuilder().setAge(31).setName("Tarun").setAddress(address).setCar(car).build();
		
		System.out.println(person);
	}

}
