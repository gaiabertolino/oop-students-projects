package Bertolino.polynomials;

public class Regex {

	static String POLYNOMIAL = "\\-?\\d*((x\\^){1}\\d+|x)?(([\\-\\+]{1})\\d*((x\\^){1}\\d+|x)?)*";
	
	static boolean isPolynomial (String s){
		if (s.matches(POLYNOMIAL))
			return true;
		return false;
	}//isPolynomial
	
	private static Monomial parseMonomial(String s, int i) {
		String coefficient = "";
		String degree = "";
		boolean x = false;
		if (s.charAt(i) == '-' || s.charAt(i) == '+') {
			coefficient += s.charAt(i);
			i++;
		}
		while (i<s.length()) {
			if (s.charAt(i) == '-' || s.charAt(i) == '+')
				break;
			else if (s.charAt(i) == 'x')
				x = true;
			else if (!x)
				coefficient += s.charAt(i);
			else if (x && s.charAt(i) != '^') 
				degree += s.charAt(i);
			
			i++;
		}
		if (degree.equals("") && !x)
			 degree += 0;
		else if (degree.equals("") && x)
			 degree += 1;
		if (coefficient.equals("+") || coefficient.equals("-"))
			coefficient += 1;
		return new Monomial(Integer.parseInt(coefficient), Integer.parseInt(degree));
	}//parseMonomial

	static PolynomialSet parse (String s) {
		PolynomialSet polynomial = new PolynomialSet();
		int i = 0;
		while (i<s.length()) {
			if (s.charAt(i) == '-' || s.charAt(i) == '+')
				polynomial.add(parseMonomial(s,i));
			else if (i==0)
				polynomial.add(parseMonomial(s,i));
			i++;
		}
		return polynomial;
	}//parse

}//Regex
