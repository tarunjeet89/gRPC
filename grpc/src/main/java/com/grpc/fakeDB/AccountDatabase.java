package com.grpc.fakeDB;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {
/*
    This is a DB
    1 => 10
    2 => 20
    ...
    10 => 100
 */

	private static final Map<Integer, Integer> MAP = initializeDB();
    
    public static Map<Integer, Integer> initializeDB(){
    	Map<Integer, Integer> intialMap = new HashMap<>();
    	for (int i = 1; i <= 100; i++) {
    		intialMap.put(i, i*10);
		}
        return intialMap;
    }
    public static int getBalance(int accountId){
        return MAP.get(accountId);
    }

    public static Integer addBalance(int accountId, int amount){
        return MAP.computeIfPresent(accountId, (k, v) -> v + amount);
    }

    public static Integer deductBalance(int accountId, int amount){
        return MAP.computeIfPresent(accountId, (k, v) -> v - amount);
    }

    public static void printAccountDetails(){
        System.out.println(
                MAP
        );
    }

}
