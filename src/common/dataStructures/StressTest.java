package common.dataStructures;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class StressTest {

	private static final int BIG_VAL = 300000;
	
	@Test
	public void testArrayListsPrepend(){
		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		DeArrList<Integer> arr2 = new DeArrList<Integer>();
		
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < BIG_VAL; i++){
			arr1.add(0, i);
		}
		long arr1Time = System.currentTimeMillis() - startTime;
		System.out.println("Standard Implementation Prepend " + arr1Time +"ms");
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < BIG_VAL; i++){
			arr2.add(0, i);
		}
		long arr2Time = System.currentTimeMillis() - startTime;
		System.out.println("My Implementation Prepend " + arr2Time +"ms");
		
		assertTrue(arr2Time < arr1Time);
	}
	
	@Test
	public void testRemove(){
		ArrayList<Integer> arr1 = new ArrayList<Integer>();
		DeArrList<Integer> arr2 = new DeArrList<Integer>();
		
		for(int i = 0; i < BIG_VAL; i++){
			arr1.add(0, i);
			arr2.add(0, i);

		}
		
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < BIG_VAL; i++){
			arr1.remove(arr1.size()/4);
		}
		long arr1Time = System.currentTimeMillis() - startTime;
		System.out.println("Standard Implementation Remove " + arr1Time +"ms");
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < BIG_VAL; i++){
			arr2.remove(arr2.size()/4);
		}
		long arr2Time = System.currentTimeMillis() - startTime;
		System.out.println("My Implementation Remove " + arr2Time +"ms");
		
		assertTrue(arr2Time < arr1Time);
	}

}
