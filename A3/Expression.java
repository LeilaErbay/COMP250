/*STUDENT: LEILA ERBAY
 * STUDENT ID: 260672158
 */

import java.io.*;
import java.util.*;

public class Expression {
	static String delimiters = "+-*%()";

	// Returns the value of the arithmetic Expression described by expr
	// Throws an exception if the Expression is malformed
	static Integer evaluate(String expr) throws Exception {
		//Create a string tokenizer to break up the string 
		StringTokenizer str = new StringTokenizer(expr, delimiters, true);
		
		//create two stacks one for Operators and one for Integers
		Stack<String> OpStack = new Stack<String>();
		Stack<Integer> IntStack = new Stack<Integer>();

		//counter used to determine if the number of closing and opening brackets is equivalent
		int counter = 0;

		while (str.hasMoreTokens()) {
			String currentStr = str.nextToken();
			
			//if current token is a  number, convert to an Integer and push into onto the Integer stack 
			if (Character.isDigit(currentStr.charAt(0))) {
				Integer intStr = Integer.valueOf(currentStr);
				IntStack.push(intStr);
			}
			//if the current token is not an Integer and is not a closing bracket, push to Operator Stack
			if ((!currentStr.contains(")")) && (!Character.isDigit(currentStr.charAt(0)))) {
				if (currentStr.equals("(")) {
					counter++;
				}
				OpStack.push(currentStr);
			}
			//condition if current token is a closing bracket
			if (currentStr.contains(")")) {
				counter--;
				//error if the expression contains an opening bracket and an operator without any other operators inside
				if ((OpStack.size()==1) && OpStack.get(0).equals("(")){
					throw new Exception("error in your mathematical syntax");
				}
				while (!OpStack.peek().equals("(")){
				//if operator is +, add top two integers of Integer stack and push sum onto Integer stack
				if (OpStack.peek().equals("+")) {
					Integer value1 = IntStack.pop();
					Integer value2 = IntStack.pop();
					Integer addition = value1 + value2;
					IntStack.push(addition);
				}
				//if operator is *, multiply top two integers of Integer stack  and push product onto Integer stack
				if (OpStack.peek().equals("*")) {
					Integer value1 = IntStack.pop();
					Integer value2 = IntStack.pop();
					IntStack.push(value1 * value2);
				}
				//if operator is -, subtract top two integers of Integer stack  and push result onto Integer stack
				if (OpStack.peek().equals("-")) {
					Integer value1 = IntStack.pop();
					Integer value2 = IntStack.pop();
					Integer sub = value2 - value1;
					IntStack.push(sub);
				}
				//if operator is %, divide top two integers of Integer stack  and push quotient onto Integer stack
				if (OpStack.peek().equals("%")) {
					Integer value1 = IntStack.pop();
					Integer value2 = IntStack.pop();
					Integer divide = value2 / value1;
					IntStack.push(divide);
					
				}
				//pop the current operator since it has been evaluated
				OpStack.pop();
				}
			}
		}	
		
		//after parentheses have been evaluated, continue evaluating rest of mathematical expression
		while (IntStack.empty() == false && OpStack.empty()== false) {
			//check if there are any remaining opening brackets
			while (OpStack.peek().equals("(")){  
				OpStack.pop();
			}
			//if operator is +, add bottom-most of Integer stack and push sum onto Integer stack
			if (OpStack.peek().equals("+")) {
				Integer val1 = IntStack.get(0);
				Integer val2 = IntStack.get(0);
				Integer sum = val1 + val2;
				IntStack.push(sum);
			}
			//if operator is -, subtract bottom-most integers of Integer stack  and push result onto Integer stack
			if (OpStack.peek().equals("-")) {
				Integer val1 = IntStack.get(0);
				Integer val2 = IntStack.get(1);
				Integer sub = val1 - val2;
				IntStack.push(sub);
			}
			//if operator is *, multiply bottom-most integers of Integer stack  and push product onto Integer stack
			if (OpStack.peek().equals("*")) {
				Integer val1 = IntStack.get(0);
				Integer val2 = IntStack.get(1);
				Integer product = val1 * val2;
				IntStack.push(product);
			}
			//if operator %, divide bottom-most integers of Integer stack and push product onto Integer Stack
			if (OpStack.peek().equals("%")) {
				Integer val1 = IntStack.get(1);
				Integer val2 = IntStack.get(0);
				Integer divide = val2 / val1;
				IntStack.push(divide);
			}
			//remove the two evaluated Integers
			IntStack.remove(IntStack.get(0));
			IntStack.remove(IntStack.get(0));
			//pop off the operator since it has been evaluated
			OpStack.pop();

		}	 
		//if number of opening and closing parentheses are not the same, throw an exception
		if (counter != 0) {
			throw new Exception("the number of closing brackets and opening brackets must be the same");
		}
		
		//last remaining value of Integer stack should be the answer
		return IntStack.pop();

	}

	public static void main(String args[]) throws Exception {
		String line;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		do {
			line = stdin.readLine();
			if (line.length() > 0) {
				try {
					Integer x = evaluate(line);
					System.out.println(" = " + x);
				} catch (Exception e) {
					System.out.println("Malformed Expression: " + e);
				}
			}
		} while (line.length() > 0);
	}
}
