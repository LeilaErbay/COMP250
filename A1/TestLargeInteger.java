import java.lang.Math;

/*********************************************************/
/* NAME: Leila Erbay*/
/* STUDENT ID: 260672158*/
/*********************************************************/

/*
 * This class stores and manipulates very large non-negative integer numbers The
 * digits of the number are stored in an array of bytes.
 */
class LargeInteger {

	/*
	 * The digits of the number are stored in an array of bytes. Each element of
	 * the array contains a value between 0 and 9. By convention,
	 * digits[digits.length-1] correspond to units, digits[digits.length-2]
	 * corresponds to tens, digits[digits.length-3] corresponds to hundreds,
	 * etc.
	 */

	byte digits[];

	/* Constructor that creates a new LargeInteger with n digits */
	public LargeInteger(int n) {
		digits = new byte[n];
	}

	/*
	 * Constructor that creates a new LargeInteger whose digits are those of the
	 * string provided
	 */
	public LargeInteger(String s) {
		digits = new byte[s
				.length()]; /*
							 * Note on "length" of arrays and strings: Arrays
							 * can be seen as a class having a member called
							 * length. Thus we can access the length of digits
							 * by writing digits.length However, in the class
							 * String, length is a method, so to access it we
							 * need to write s.length()
							 */

		for (int i = 0; i < s.length(); i++)
			digits[i] = (byte) Character.digit(s.charAt(i), 10);
		/*
		 * Here, we are using a static method of the Character class, called
		 * digit, which translates a character into an integer (in base 10).
		 * This integer needs to be cast into a byte.
		 ****/
	}

	/*
	 * Constructor that creates a LargeInteger from an array of bytes. Only the
	 * bytes between start and up to but not including stop are copied.
	 */
	public LargeInteger(byte[] array, int start, int stop) {
		digits = new byte[stop - start];
		for (int i = 0; i < stop - start; i++)
			digits[i] = array[i + start];
	}

	/*
	 * This method returns a LargeInteger where eventual leading zeros are
	 * removed. For example, it turns 000123 into 123. Special case: it turns
	 * 0000 into 0.
	 */
	public LargeInteger removeLeadingZeros() {
		if (digits[0] != 0)
			return this;
		int i = 1;
		while (i < digits.length && digits[i] == 0)
			i++;
		if (i == digits.length)
			return new LargeInteger("0");
		else
			return new LargeInteger(digits, i, digits.length);
	} // end of removeLeadingZeros

	/*
	 * This methods multiplies a given LargeInteger by 10^nbDigits, simply by
	 * shifting the digits to the left and adding nbDigits zeros at the end
	 */
	public LargeInteger shiftLeft(int nbDigits) {
		LargeInteger ret = new LargeInteger(digits.length + nbDigits);
		for (int i = 0; i < digits.length; i++)
			ret.digits[i] = digits[i];
		for (int i = 0; i < nbDigits; i++)
			ret.digits[digits.length + i] = 0;
		return ret;
	} // end of shiftLeft

	/*
	 * Returns true if the value of this is the same as the value of other
	 */
	public boolean equals(LargeInteger other) {
		if (digits.length != other.digits.length)
			return false;
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] != other.digits[i])
				return false;
		}
		return true;
	} // end of equals

	/*
	 * Returns true if the value of this is less than the value of other
	 ****/
	public boolean isSmaller(LargeInteger other) {
		if (digits.length > other.digits.length)
			return false;
		if (digits.length < other.digits.length)
			return true;
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] < other.digits[i])
				return true;
			if (digits[i] > other.digits[i])
				return false;
		}
		return false;
	} // end of isSmaller

	/*
	 * This method adds two LargeIntegers: the one on which the method is called
	 * and the one given as argument. The sum is returned. The algorithms
	 * implemented is the normal digit-by-digit addition with carry.
	 */

	LargeInteger add(LargeInteger other) {
		int size = Math.max(digits.length, other.digits.length);

		/* The sum can have at most one more digit than the two operands */
		LargeInteger sum = new LargeInteger(size + 1);
		byte carry = 0;

		for (int i = 0; i < size + 1; i++) {
			// sumColumn will contain the sum of the two digits at position i
			// plus the carry
			byte sumColumn = carry;
			if (digits.length - i - 1 >= 0)
				sumColumn += digits[digits.length - i - 1];
			if (other.digits.length - i - 1 >= 0)
				sumColumn += other.digits[other.digits.length - i - 1];
			sum.digits[sum.digits.length - 1 - i] = (byte) (sumColumn
					% 10); /*
							 * The i-th digit in the summ is sumColumn mod 10
							 */
			carry = (byte) (sumColumn / 10); // The carry for the next
			// iteration
			// is sumColumn/10
		}
		return sum.removeLeadingZeros();
	} // end of add

	/*
	 * This method subtracts the LargeInteger other from that from where the
	 * method is called. Assumption: the argument other contains a number that
	 * is not larger than the current number. The algorithm is quite interesting
	 * as it makes use of the addition code. Suppose numbers X and Y have six
	 * digits each. Then X - Y = X + (999999 - Y) - 1000000 + 1. It turns out
	 * that computing 999999 - Y is easy as each digit d is simply changed to
	 * 9-d. Moreover, subtracting 1000000 is easy too, because we just have to
	 * ignore the '1' at the first position of X + (999999 - Y). Finally, adding
	 * one can be done with the add code we already have. This tricks is the
	 * equivalent of the method used by most computers to do subtractions on
	 * binary numbers.
	 ***/

	public LargeInteger subtract(LargeInteger other) {
		// if other is larger than this number, simply return 0;
		if (this.isSmaller(other) || this.equals(other))
			return new LargeInteger("0");

		LargeInteger complement = new LargeInteger(
				digits.length); /*
								 * complement will be 99999999 - other.digits
								 */
		for (int i = 0; i < digits.length; i++)
			complement.digits[i] = 9;
		for (int i = 0; i < other.digits.length; i++)
			complement.digits[digits.length - i - 1] -= other.digits[other.digits.length - i - 1];

		LargeInteger temp = this.add(complement); // add (999999-
		// other.digits)
		// to this
		temp = temp.add(new LargeInteger("1")); // add one

		// return the value of temp, but skipping the first digit (i.e.
		// subtracting 1000000)
		// also making sure to remove leading zeros that might have
		// appeared.
		return new LargeInteger(temp.digits, 1, temp.digits.length).removeLeadingZeros();
	} // end of subtract

	/* Returns a randomly generated LargeInteger of n digits */
	public static LargeInteger getRandom(int n) {
		LargeInteger ret = new LargeInteger(n);
		for (int i = 0; i < n; i++) {
			// Math.random() return a random number x such that 0<= x <1
			ret.digits[i] = (byte) (Math.floor(Math.random() * 10));
			// if we generated a zero for first digit, regenerate a draw
			if (i == 0 && ret.digits[i] == 0)
				i--;
		}
		return ret;
	} // end of getRandom

	/* Returns a string describing a LargeInteger 17 */
	public String toString() {

		/* We first write the digits to an array of characters ****/
		char[] out = new char[digits.length];
		for (int i = 0; i < digits.length; i++)
			out[i] = (char) ('0' + digits[i]);

		/*
		 * We then call a String constructor that takes an array of characters
		 * to create the string
		 */
		return new String(out);
	} // end of toString

	/*
	 * This function returns the product of this and other by iterative addition
	 */
	public LargeInteger iterativeAddition(LargeInteger other) {
		LargeInteger sum = new LargeInteger(other.digits.length); // sum will
		// hold the
		// values of
		// adding
		// other to
		// itself
		// this many
		// times
		LargeInteger increment = new LargeInteger("1"); // increment must be a
		// LargeInteger to
		// increase the counter
		// by one

		/*
		 * initialization: counter has an empty that is the size of this
		 * condition: checks if counter is not equal to this; once counter does
		 * equal this, meaning it has added "other" to itself "this" many times,
		 * it will stop iteration: counter is incremented by a value of 1 since
		 * increment is also a LargeInteger; must update counter thus assign
		 * counter.add(increment) to counter
		 */
		for (LargeInteger counter = new LargeInteger(this.digits.length); !(counter.equals(this)); counter = counter
				.add(increment)) {
			sum = sum.add(other);
		}
		// return the sum of adding other to itself "this" many times; sum is
		// thus the product of this and other
		return sum;

	}

	/*
	 * for (LargeInteger counter = new LargeInteger(this.digits.length);
	 * !counter.equals(this); counter = counter.add(increment)) //for
	 * (sum.digits[0] = 1; sum.digits.length<this.digits.length; sum.digits(1){
	 * if (other.equals(zero)) return zero; else{ sum = other.add(other); } sum
	 * = sum.removeLeadingZeros(); return sum; }
	 */
	// end of iterativeAddition

	/*
	 * This function returns the product of this and other by using the standard
	 * multiplication algorithm
	 */

	public LargeInteger standardMultiplication(LargeInteger other) {
		// create a LargeInteger total that will return the product of this and
		// other
		LargeInteger total = new LargeInteger("0");

		/*
		 * This method represents long multiplication With each digit of the
		 * "other", you will multiply that initial digit by every digit of
		 * "this" After multiplying every digit of this with that single digit
		 * of other, you break out of the inner loop and decrement the other
		 * loop.
		 * 
		 * Decrement because you only need to multiply the values of this and
		 * other by the number of digits they contain.
		 *
		 * 
		 */

		for (int i = other.digits.length - 1; i >= 0; i--) {
			int carry = 0;
			LargeInteger tmpAdd = new LargeInteger(this.digits.length + 1);
			for (int j = this.digits.length - 1; j >= 0; j--) {

				// for each digit of other, you multiply the current digit of
				// this and add the carry
				int c = other.digits[i] * this.digits[j] + carry;

				// the tmpAdd adds the remainder of c / 10 starting form the
				// last place of the array
				tmpAdd.digits[j + 1] = (byte) (c % 10);
				carry = c / 10;
			}
			tmpAdd.digits[0] = (byte) carry; // at the first index of tempAdd,
			// add the the carry

			/*
			 * update total with the current total and the temporary additions
			 * of the digits from this after having shifted the temporary
			 * LargeInteger to the left by 10 ^ length of other minus the
			 * current value of i minus 1
			 */
			total = total.add(tmpAdd.shiftLeft((other.digits.length - i - 1)));

		}
		// return the final product of long multiplication
		return total;

	} // end of standardMultiplication

	/*
	 * This function returns the product of this and other by using the basic
	 * recursive approach described in the homework. Only use the built-in "*"
	 * operator to multiply single-digit numbers
	 */
	public LargeInteger recursiveMultiplication(LargeInteger other) {

		// left and right halves of this and number2
		LargeInteger leftThis, rightThis, leftOther, rightOther;
		LargeInteger term1, term2, term3, term4, sum; // temporary terms

		if (digits.length == 1 && other.digits.length == 1) {
			int product = digits[0] * other.digits[0];
			return new LargeInteger(String.valueOf(product));
		}

		int k = digits.length;
		int n = other.digits.length;
		leftThis = new LargeInteger(digits, 0, k - k / 2);
		rightThis = new LargeInteger(digits, k - k / 2, k);
		leftOther = new LargeInteger(other.digits, 0, n - n / 2);
		rightOther = new LargeInteger(other.digits, n - n / 2, n);

		/*
		 * now recursively call recursiveMultiplication to compute the four
		 * products with smaller operands
		 */

		if (n > 1 && k > 1)
			term1 = rightThis.recursiveMultiplication(rightOther);
		else
			term1 = new LargeInteger("0");

		if (k > 1)
			term2 = (rightThis.recursiveMultiplication(leftOther)).shiftLeft(n / 2);
		else
			term2 = new LargeInteger("0");

		if (n > 1)
			term3 = (leftThis.recursiveMultiplication(rightOther)).shiftLeft(k / 2);
		else
			term3 = new LargeInteger("0");

		term4 = (leftThis.recursiveMultiplication(leftOther)).shiftLeft(k / 2 + n / 2);

		sum = new LargeInteger("0");
		sum = sum.add(term1);
		sum = sum.add(term2);
		sum = sum.add(term3);
		sum = sum.add(term4);

		return sum;
	} // end of recursiveMultiplication

	/*
	 * This method returns the product of this and other by using the faster
	 * recursive approach described in the homework. It only uses the built-in
	 * "*" operator to multiply single-digit numbers
	 */
	public LargeInteger recursiveFastMultiplication(LargeInteger other) {
		LargeInteger term1, term2, term3, sum1, sum2, sumTotal;
		LargeInteger leftThis, rightThis, leftOther, rightOther;

		int k = this.digits.length;
		int n = other.digits.length;

		// if the size of other is greater than the size of this, use
		// recursiveFastMultiplication
		if (n < k)
			return other.recursiveFastMultiplication(this);
		// BASE CASE:
		// if the the size of other equals 1, use standard multiplication
		if (k == 1)
			return this.standardMultiplication(other);

		// initializing the left and right halves of "this" and "other"
		leftThis = new LargeInteger(this.digits, 0, k - k / 2);
		rightThis = new LargeInteger(this.digits, k - k / 2, k);
		leftOther = new LargeInteger(other.digits, 0, n - n / 2);
		rightOther = new LargeInteger(other.digits, n - n / 2, n);

		/*
		 * term 1, term 2, and term 3 recursively call the right halves, left
		 * halves, and sum of the this on the sum of other respectively term 3
		 * also includes subtractions of term2 and term1.
		 */
		term1 = rightThis.recursiveFastMultiplication(rightOther);
		term2 = leftThis.recursiveFastMultiplication(leftOther);
		term3 = ((leftThis.add(rightThis))
				.recursiveFastMultiplication((leftOther.shiftLeft(n / 2 - k / 2)).add(rightOther)))
						.subtract(term2.shiftLeft(n / 2 - k / 2)).subtract(term1);

		// temporary variables sum1 and sum2 shift term 2 and term 3
		// respectively
		sum1 = term2.shiftLeft(k / 2 + n / 2);
		sum2 = term3.shiftLeft(k / 2);

		// sumTotal is the addition of sum1, sum 2, and term 1
		sumTotal = sum1.add(sum2).add(term1);

		return sumTotal;
	}
}

	// end of the LargeInteger class
	public class TestLargeInteger {
		public static void main (String[] args) {
			/*
			 * THIS CODE IS NOT GOING TO BE GRADED. IT'S JUST FOR YOU TO TEST YOUR
			 * PROGRAM
			 */

			/*
			 * LargeInteger a = new LargeInteger("12"); LargeInteger b = new
			 * LargeInteger("788"); LargeInteger newInt =
			 * b.recursiveFastMultiplication(a); System.out.println(newInt);
			 */
			// METHODS:
			// 1. iterativeAddition
			// 2. standardMultiplication
			// 3. recursiveMultiplication
			// 4. recursiveFastMultiplication

			//n is the number of digits getRandom will generate
			int n = 2;
			//x is the number of loops you want the program to run
			int x = 1000;
			//start time
			long start = System.nanoTime();
			for (int i = 0; i < x; i++) {
				LargeInteger randomA = LargeInteger.getRandom(n);
				LargeInteger randomB = LargeInteger.getRandom(n);
				//change the method call to fit what value you desire for which method
				randomA.recursiveMultiplication(randomB);
			}
			long end = System.nanoTime();
			//returns the duration divided by the number of loops to determine the runtime per loop
			long duration = (end - start) / x;
			

			System.out.println("number of digits: " + n);
			System.out.println("The time to execute " + x + " multiplications = " + duration);

		}

	}

