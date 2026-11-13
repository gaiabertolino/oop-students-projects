/** @author Gaia Bertolino */

package Bertolino.math;
import java.util.Iterator;

public interface BigInt extends Comparable<BigInt>,Iterable<Integer> {
	
	/** Converts this into a string. The number is built digit by digit using an iterator
	 * @return this as a String
	 */
	default String value()  {
		StringBuilder s = new StringBuilder();
		for (Iterator<Integer> i = iterator(); i.hasNext();)
			s.append(i.next());
		return s.toString();
	}//value()
	
	/** Returns how many digits this is made of
	 * @return the number of digits of this
	 */
	default int length() {
		int len = 0;
		for (Iterator<Integer> i = iterator(); i.hasNext(); i.next(), len++);
		return len;
	}//length()
	
	/** Increments this by one
	 * @return this+1
	 */
	default BigInt incr() {
		return this.add(factory(1));
	}//incr()
	
	/** Decrements this by one
	 * @return this-1
	 * @throws IllegalArgumentException if this is zero, since the decrement would produce a negative number
	 */
	default BigInt decr() throws IllegalArgumentException {
		if (this.equals(factory(0)))
			throw new IllegalArgumentException();
		return this.sub(factory(1));
	}//decr()
	
	/** Computes the product of this and the BigInt passed as argument
	 * @param m BigInt acting as the multiplier
	 * @return the product of this and m
	 */
	default BigInt mul( BigInt m ) {
		BigInt zero = factory(0);
		BigInt result = factory(0);
		while (!m.equals(zero)) {
			result = result.add(this);
			m = m.decr();
		}
		return result;
	}//mul(BigInt m)
	
	/** Computes the integer division of this by the BigInt passed as argument
	 * @param d BigInt acting as the divisor
	 * @return the quotient of this divided by d
	 * @throws IllegalArgumentException if the argument is zero or greater than this
	 */
	default BigInt div( BigInt d ) throws IllegalArgumentException {
		if (this.compareTo(d) < 0 || d.compareTo(factory(0)) <= 0)
			throw new IllegalArgumentException();
		int result = 0;
		BigInt tmp = factory(0);
		while (tmp.add((BigInt) d).compareTo(this) <= 0) {
			result++;
			tmp = tmp.add((BigInt) d);
		}
		return factory(result);
	}//div(BigInt d)
	
	/** Computes the remainder of the division of this by the BigInt passed as argument
	 * @param d BigInt acting as the divisor
	 * @return the remainder of this divided by d
	 */
	default BigInt rem( BigInt d ) {
		if (this.compareTo(d) < 0 || d.compareTo(factory(0)) == 0)
			throw new IllegalArgumentException();
		BigInt result = factory(0);
		while (result.add((BigInt) d).compareTo(this) <= 0) {
			result = result.add((BigInt) d);
		}
		return this.sub(result);
	}//rem(BigInt d)
	
	/** Raises this to the power given by the int passed as argument
	 * @param exp int acting as the exponent
	 * @return this raised to the power exp
	 */
	default BigInt pow( int exp ) {
		BigInt result = factory(1);
		while (exp>0) {
			result = result.mul(this);
			exp--;
		}
		return result;
	}//pow(int exp)

	/** Factory method: creates a BigInt from the int passed as argument
	 * @param i int
	 * @return a BigInt with the value of i
	 * @throws IllegalArgumentException if the argument is less than zero
	 */
		BigInt factory( int i ) throws IllegalArgumentException; 
		
	/** Computes the sum of this and the BigInt passed as argument
	 * @param a BigInt acting as the addend
	 * @return the sum of this and a
	 */
		BigInt add( BigInt a );
		
	/** Computes the difference between this and the BigInt passed as argument
	 * @param s BigInt acting as the subtrahend
	 * @return the difference this - s
	 * @throws IllegalArgumentException if the subtrahend is greater than this (the result would be negative)
	 */
		BigInt sub( BigInt s ) throws IllegalArgumentException;
		
}//BigInt