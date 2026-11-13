package Bertolino.expression;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
	
	protected static String lastResult;

	public static String evaluate(StringTokenizer s) {
		Stack<String> operands = new Stack<>();
		Stack<Character> operators = new Stack<>();

		while (s.hasMoreTokens()) {
			String token = s.nextToken();
			
			if (Regex.isNumber(token))
				operands.add(token);
			else if (token.equals("(")) {
				operands.push(evaluate(s));
			}
			else if (token.equals(")")) {
				while (!operators.isEmpty()) {
					String o1 = operands.pop();
					String o2 = operands.pop();
					char op = operators.pop();
					operands.push(compute(o2,o1,op));
				}
				return operands.pop();
			}
			else if (s.hasMoreTokens()) { //here the token is certainly an operator
				char op1 = token.charAt(0);

				if(operators.isEmpty())
					operators.push(op1);
				else {
					char op2 = operators.pop();
					
					if (hasPriority(op1,op2)) {
						operators.push(op2);
						operators.push(op1);
					}
					else {
						String o2 = operands.pop();
						String o1 = operands.pop();
						operands.push(compute(o1,o2,op2));
						
						if (!operators.isEmpty()) {
							op2 = operators.pop();
							
							if (!hasPriority(op1,op2)) {
								o2 = operands.pop();
								o1 = operands.pop();
								operands.push(compute(o1,o2,op2));
								operators.push(op1);
							}
						}
						else
							operators.push(op1);
					}
				}	

			}
		}
		while (!operators.isEmpty()) {
			String o1 = operands.pop();
			String o2 = operands.pop();
			char op = operators.pop();
			operands.push(compute(o2,o1,op));
		}
		if (operands.size() == 1)
			lastResult = operands.pop();
		else
			throw new IllegalArgumentException();
		
		return lastResult;
	}//evaluate

	
	private static String compute (String o1, String o2, char op) {
		String result = "";
		
		if (op == '^')
			result += (Integer.parseInt(o1)^Integer.parseInt(o2));
		
		else if (op == '*')
			result += (Integer.parseInt(o1)*Integer.parseInt(o2));
		else if (op == '/')
			result += (Integer.parseInt(o1)/Integer.parseInt(o2));
		if (op == '%')
			result += (Integer.parseInt(o1)%Integer.parseInt(o2));
		
		if (op == '+')
			result += (Integer.parseInt(o1)+Integer.parseInt(o2));
		if (op == '-')
			result += (Integer.parseInt(o1)-Integer.parseInt(o2));
		return result;
	}//compute

	
	private static boolean hasPriority(char op1, char op2) {
		if (op1 == op2)
			return false;
		if (op1 == '^')
			return true;
		if (op1 == '+' && op2 == '-')
			return false;
		if (op1 == '*' || op1 == '/' || op1 == '%') {
			if (op2 == '+' || op2 == '-')
				return true;
			else
				return false;
		}
		return false;
	}//hasPriority

}//Evaluator
