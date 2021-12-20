package com.protobuf.protobuffer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.protobuf.models.Address;
import com.protobuf.models.Car;
import com.protobuf.models.PersonCollection;

public class CollectionDemo {
	
	public static void main(String[] args) {
		Address address = Address.newBuilder().setCity("kurali").setPostbox(140103).setStreet("Sabji Mandi").build();
		Car car1 = Car.newBuilder().setMake("Honda").setModel("Sedan").setYear(2021).build();
		Car car2 = Car.newBuilder().setMake("Tesla").setModel("SUV").setYear(2020).build();
		Car car3 = Car.newBuilder().setMake("Lamborghini").setModel("Sedan").setYear(2020).build();
		List<Car> list = Stream.of(car1, car2, car3).collect(Collectors.toList());
		
		PersonCollection person = PersonCollection.newBuilder().setAge(31).setName("Tarun").setAddress(address).addAllCar(list).build();
		System.out.println(person);
	}

}
