package com.protobuf.protobuffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.protobuf.models.Person;
import com.protobuf.models.PersonWrapper;

public class PersonSerialDeserial {
	
	public void writeFile(Person person) throws IOException {
		Path path = Paths.get("tarun.ser");
        Files.write(path, person.toByteArray());
	}
	
	public void readFile() throws IOException {
		Path path = Paths.get("tarun.ser");
        byte[] bytes = Files.readAllBytes(path);
        Person person = Person.parseFrom(bytes);
        System.out.println(person);
	}
	
	public static void main(String[] args) throws IOException {
		Person person = Person.newBuilder().setAge(31).setName("Tarunjeet").build();
		PersonSerialDeserial obj= new PersonSerialDeserial();
		obj.writeFile(person);
		obj.readFile();
		
	}

}
