package Bertolino.expression;

public class Regex {

	private static String EXPRESSION = "(\\(?(\\d*([\\-\\+\\*\\%\\/\\^]{1}\\d*)*)+\\)?)*";
	
	protected static boolean isWellFormed(String s) {
		try {
			if (s.substring(s.length()-1).matches(("[\\+\\-\\*\\+\\/\\%]")))
				return false;
			int openParentheses = 0;
			for (int i=0; i<s.length(); i++) {
				if (s.charAt(i) == '(')
					openParentheses++;
				if (s.charAt(i) == ')')
					openParentheses--;
			}
			if (openParentheses != 0) 
				return false;
			if (s.matches(EXPRESSION))
				return true;
			return false;
		}
		catch (Exception e) { return false; }
	}//isWellFormed
	
	protected static boolean isNumber(String s) {
		if (s.matches("\\d+"))
			return true;
		return false;
	}//isNumber
}//Regex
