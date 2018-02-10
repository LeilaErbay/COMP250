/*STUDENT: LEILA ERBAY
 * ID: 260672158
 * Assignment: A4 Q1
 * 
 */

import java.lang.Math.*;


class ExpressionTree {
	private String value;
	private ExpressionTree leftChild, rightChild, parent;

	ExpressionTree() {
		value = null;
		leftChild = rightChild = parent = null;
	}

	// Constructor
	/*
	 * Arguments: String s: Value to be stored in the node ExpressionTree l, r,
	 * p: the left child, right child, and parent of the node to created
	 * Returns: the newly created ExpressionTree
	 */
	ExpressionTree(String s, ExpressionTree l, ExpressionTree r, ExpressionTree p) {
		value = s;
		leftChild = l;
		rightChild = r;
		parent = p;
	}

	/* Basic access methods */
	String getValue() {
		return value;
	}

	ExpressionTree getLeftChild() {
		return leftChild;
	}

	ExpressionTree getRightChild() {
		return rightChild;
	}

	ExpressionTree getParent() {
		return parent;
	}

	/* Basic setting methods */
	void setValue(String o) {
		value = o;
	}

	// sets the left child of this node to n
	void setLeftChild(ExpressionTree n) {
		leftChild = n;
		n.parent = this;
	}

	// sets the right child of this node to n
	void setRightChild(ExpressionTree n) {
		rightChild = n;
		n.parent = this;
	}

	// Returns the root of the tree describing the expression s
	// Watch out: it makes no validity checks whatsoever!
	ExpressionTree(String s) {
		// check if s contains parentheses. If it doesn't, then it's a leaf
		if (s.indexOf("(") == -1)
			setValue(s);
		else { // it's not a leaf

			/*
			 * break the string into three parts: the operator, the left
			 * operand, and the right operand.
			 ***/
			setValue(s.substring(0, s.indexOf("(")));
			// delimit the left operand 2008
			int left = s.indexOf("(") + 1;
			int i = left;
			int parCount = 0;
			// find the comma separating the two operands
			while (parCount >= 0 && !(s.charAt(i) == ',' && parCount == 0)) {
				if (s.charAt(i) == '(')
					parCount++;
				if (s.charAt(i) == ')')
					parCount--;
				i++;
			}
			int mid = i;
			if (parCount < 0)
				mid--;

			// recursively build the left subtree
			setLeftChild(new ExpressionTree(s.substring(left, mid)));

			if (parCount == 0) {
				// it is a binary operator
				// find the end of the second operand.F13
				while (!(s.charAt(i) == ')' && parCount == 0)) {
					if (s.charAt(i) == '(')
						parCount++;
					if (s.charAt(i) == ')')
						parCount--;
					i++;
				}
				int right = i;
				setRightChild(new ExpressionTree(s.substring(mid + 1, right)));
			}
		}
	}

	// Returns a copy of the subtree rooted at this node... 2014
	ExpressionTree deepCopy() {
		ExpressionTree n = new ExpressionTree();
		n.setValue(getValue());
		if (getLeftChild() != null)
			n.setLeftChild(getLeftChild().deepCopy());
		if (getRightChild() != null)
			n.setRightChild(getRightChild().deepCopy());
		return n;
	}

	// Returns a String describing the subtree rooted at a certain node.
	public String toString() {
		String ret = value;
		if (getLeftChild() == null)
			return ret;
		else
			ret = ret + "(" + getLeftChild().toString();
		if (getRightChild() == null)
			return ret + ")";
		else
			ret = ret + "," + getRightChild().toString();
		ret = ret + ")";
		return ret;
	}

	// Returns the value of the expression rooted at a given node
	// when x has a certain value
	double evaluate(double x) {
		double result = x;
		// base case:
		// If node has no children, it is a leaf thus it is either a number or
		// variable x
		if ((getLeftChild() == null) && (getRightChild() == null)) {
			if (getValue().equals("x")) {
				return result = x;
			}
			else{
				return result = Double.parseDouble(getValue());
			}
		}
			//double result = 0;
			// if the operator is addition then recursively add the leftChild
			// and rightChild
			if (getValue().toString().equals("add")) {
				return getLeftChild().evaluate(result)+ getRightChild().evaluate(result);
			}
			//// if the operator is multiplication then recursively multiply the leftChild and rightChild
			if (getValue().toString().equals("mult")) {
				return getLeftChild().evaluate(result)* getRightChild().evaluate(result);
			}

			// if the operator is subtraction then recursively subtract the leftChild and rightChild
			if (getValue().toString().equals("minus")) {
				return getLeftChild().evaluate(result)- getRightChild().evaluate(result);
			}

			// if the operator is sin
			if (getValue().toString().equals("sin")) {
				// recursively execute sin on the leftChild if the rightChild is empty
					return Math.sin(getLeftChild().evaluate(result));
				}
				// if the operator is cos
			if (getValue().toString().equals("cos")) {
					// recursively execute cos on the leftChild if the rightChild is empty
					return Math.cos(getLeftChild().evaluate(result));
						 //return Math.cos(result);
				}
				//// if the operator is exponential
			if (this.getValue().toString().equals("exp")) {
					// recursively execute exponential on the leftChild if the rightChild is empty
					return Math.exp(getLeftChild().evaluate(result));
					}
		return result;
	}

	/* returns the root of a new expression tree representing the derivative of
	 * the original expression
	 */
	ExpressionTree differentiate() {
		//base case: if at a leaf and the value is "x", it's derivative is 1, else if the child is a constant its derivative is 0
		if ((getLeftChild() == null)) {
			if (getValue().equals("x")){
				return new ExpressionTree("1");
			}
		else {
			return new ExpressionTree("0");
			}
		}
		
		//if current node is add or minus, differentiate left and right children and return a new tree of the derivatives
		if (getValue().equals("add") || getValue().equals("minus")) {
			ExpressionTree df = getLeftChild().differentiate();
			ExpressionTree dg = getRightChild().differentiate();
			return new ExpressionTree(getValue(), df, dg, getParent());	
		}
		
		/* if current node is mult differentiate left and right child
		* create new trees: one with the derivative of the left child, the original leftChild and 
		* the other is the derivative of the right child and the original rightChild
		* the returned tree will have the two new children and add as the parent 
		*/
		
		if (getValue().equals("mult")){
			ExpressionTree df = getLeftChild().differentiate();
			ExpressionTree dg = getRightChild().differentiate();

			ExpressionTree F = new ExpressionTree("mult", leftChild.deepCopy(), dg, getParent());
			ExpressionTree G = new ExpressionTree("mult", rightChild.deepCopy(), df, getParent());
			return new ExpressionTree("add",F,G, getParent());
				
			}
			
		/* if current node is sin, differentiate its leftChild
		 * create the other part of the derivative with cosine
		 * return a tree that multiplies the tree containing cosine with the derivative of the tree containing sin
		 */
		 if (getValue().equals("sin")){
				ExpressionTree df = getLeftChild().differentiate();
				ExpressionTree F = new ExpressionTree ("cos",getLeftChild() , df, null);
				return new ExpressionTree("mult", df, F, null);	
			}
		 
		 /* if current node is cos
		  * create the derivative of the leftChild
		  * create a new tree that contains sine and original leftChild
		  * create a another tree that combines the tree of sine with the derivative of the leftChild of the cos tree
		  * return a tree that is negative sin thus minus 0 and the tree containing sin
		  */
		 if (getValue().equals("cos")){
				ExpressionTree df = getLeftChild().differentiate();
				ExpressionTree F = new ExpressionTree("sin", getLeftChild().deepCopy(), null, null);
				ExpressionTree FF = new ExpressionTree("mult", F, df, null);
				ExpressionTree r = new ExpressionTree("0");
				return new ExpressionTree("minus", r, FF, null);
				
		}
		 /*if the current node is exponential
		  * differentiate the leftChild and copy the current tree
		  * return a tree that multiplies the derivative with the current tree
		  */
		 if (this.getValue().equals("exp")){
				ExpressionTree df = getLeftChild().differentiate();
				ExpressionTree F = deepCopy(); 
				return new ExpressionTree("mult", F, df, null);
			}	
		
		 //throw exception if input is invalid and nothing can be returned
		throw new RuntimeException ("invalid inputs"); 
	
	}

	public static void main(String args[]) {
		 ExpressionTree e = new ExpressionTree("mult(add(2,x),cos(x))");
		 //System.out.println(e.differentiate());
		 
		ExpressionTree ex = new ExpressionTree("mult(cos(x,4),sin(x))");
		//System.out.println(ex.differentiate());
		ExpressionTree st = new ExpressionTree("exp(cos(mult(x,x)))");
		//ExpressionTree blah = new ExpressionTree("cos(add(mult(3.14,x),exp(sin(minus(x,1)))))");
		System.out.println(st.differentiate());
		//System.out.println(e);
		//System.out.println(e.evaluate(1));
		

		//System.out.println(st);
		 //System.out.println(st.evaluate(1));

		 //System.out.println(st.differentiate());
		//System.out.println(blah.evaluate(1));*/
		
		

	}
}