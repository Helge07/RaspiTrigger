package counter.main;

import counter.util.Counter;

public class Tester {
	
	public static void main(String[] args) {
		
		Counter counter = new Counter();
		
		// add integers; expected result 1+2+3+4+5+6 = 21
		int result = counter.count(6);
		
		if (result == 21) {
		System.out.println("Correct");
		} else {
		System.out.println("False");
		}
		try {
		counter.count(256);
		} catch (RuntimeException e) {
		System.out.println("Works as exepected");
		}

	}
}