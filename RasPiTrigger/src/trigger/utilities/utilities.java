package trigger.utilities;

public class utilities {
	
    // isDigit
    // check if a types character is a digit
    public static Boolean isDigit (char d) {
    	return (d=='1' | d=='2' | d=='3' | d=='4' | d=='5' | d=='6' | d=='7' | d=='8' | d=='9' | d=='0');
    }   
    
	public static String extend (String s, int len) {
		s = s.trim();
		while (s.length() <len) s = s + " ";
		return s;
	}
	
		
}
