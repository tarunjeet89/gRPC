package com.protobuf.protobuffer;

import com.protobuf.models.Person;

public class PersonDemo {
	
	public static void main(String[] args) {
		Person person1 = Person.newBuilder().setAge(31).setName("Tarunjeet").build();
		
		Person person2 = Person.newBuilder().setAge(31).setName("tarunjeet").build();
		
		Person person3 = Person.newBuilder().setAge(31).setName("Tarunjeet").build();
		
		System.out.println(person1.equals(person2));
		System.out.println(person1.equals(person3));
	}
	
}
