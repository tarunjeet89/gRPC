package com.protobuf.protobuffer;

import com.protobuf.models.Person;

public class DefaultDemo {
	
	public static void main(String[] args) {
		Person person = Person.newBuilder().build();
		System.out.println(person);
		System.out.println("Address=> "+person.getAddress().getCity());
		System.out.println(person.hasAddress());
	}

}
