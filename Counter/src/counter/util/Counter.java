package counter.util;

public class Counter {
	
	public int count (int x){
	
		// make shure that 1<= x <= 255
		if((x<=0) | (x>255)) 
			throw new RuntimeException("x should be between 1 and 255");
		
		// add integers from 1 to x
		int sum = 0;
		for (int j=1;j<=x;j++) sum +=j;
		
		return sum;
	}

}