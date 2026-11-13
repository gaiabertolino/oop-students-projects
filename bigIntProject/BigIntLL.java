/** @author Gaia Bertolino */

package Bertolino.math;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class BigIntLL extends AbstractBigInt{

	private LinkedList<Integer> digits = new LinkedList<>();
	
	/**
	 * Default constructor
	 */
	public BigIntLL() {} 
	
	/** Constructor that builds a BigIntLL from a string
	 * @param s String the BigIntLL is built from
	 * @throws NumberFormatException if the string contains non-numeric characters
	 */
	public BigIntLL(String s) throws NumberFormatException {
		for (int i=0; i<s.length(); i++ ) {
			this.digits.add(Integer.parseInt(s, i, i+1, 10));
		}
		this.removeLeadingZeros();
	}//BigIntLL(String s)
	
	/** Constructor that builds a BigIntLL from an int
	 * @param i int the BigIntLL is built from
	 * @throws IllegalArgumentException if the number is negative
	 */
	public BigIntLL( int i ) throws IllegalArgumentException {
		if (i<0)
			throw new IllegalArgumentException();
		int exp = (int) Math.pow(10, (int) Math.log10(i));
		int n = 0;
		while ( exp>1 ) {
			n = i/exp;
			i = i - n * exp;
			digits.add(n);
			exp /= 10;
		}
		digits.add(i);
	}//BigIntLL(int i)

	@Override
	public BigIntLL factory(int i) throws IllegalArgumentException {
		BigIntLL number = new BigIntLL(i);
		return number;
	}//factory(int i)

	@Override
	public BigIntLL add(BigInt a) {
		BigIntLL result = new BigIntLL();
		BigIntLL addend = (BigIntLL) a;
		ListIterator<Integer> i1 = listIterator(digits.size());
		ListIterator<Integer> i2 = addend.listIterator(addend.digits.size());
		int i = ((this.compareTo(addend) > 0) ? this.digits.size() : addend.digits.size());
		int carry = 0;
		int val = 0;
		while (i>0) {
			if (i1.hasPrevious() && i2.hasPrevious()) {
				val = i1.previous() + i2.previous() + carry;
			}
			else if (i1.hasPrevious()) {
				val = i1.previous() + carry;
			}
			else if (i2.hasPrevious()) {
				val = i2.previous() + carry;
			}
			carry = val/10;
			result.digits.add(0,val - carry*10);
			i--;
		}
		if (carry > 0)
			result.digits.add(0,carry);
		return result;
	}//add(BigInt a)
	
	
	@Override
	public BigIntLL sub(BigInt s) throws IllegalArgumentException {
		BigIntLL subtrahend = (BigIntLL) s;
		if (this.compareTo(subtrahend) < 0) 
			throw new IllegalArgumentException();
		BigIntLL result = new BigIntLL();
		ListIterator<Integer> i1 = listIterator(digits.size());
		ListIterator<Integer> i2 = subtrahend.listIterator(subtrahend.digits.size());
		int i = ((this.compareTo(subtrahend) > 0) ? this.digits.size() : subtrahend.digits.size());
		int val = 0;
		int carry = 0;
		while (i>0) {
			if (i1.hasPrevious() && i2.hasPrevious())
				val = i1.previous() - i2.previous() - carry;
			else if (i1.hasPrevious())
				val = i1.previous() - carry;
			else if (i2.hasPrevious())
				val = i2.previous() - carry;
			if (val<0) {
				val += 10;
				carry = 1;
			}
			else
				carry = 0;
			result.digits.add(0, val);
			i--;
		}	
		return result.removeLeadingZeros();
	}//sub(BigInt s)

	/** Removes leading zeros (the most significant nodes whose value is 0)
	 * @return this without leading zeros
	 */
	private BigIntLL removeLeadingZeros() {
		for ( int i=0; i<digits.size(); ) {
			if (digits.size() == 1)
				return this;
			if (digits.get(i).equals(0))
				digits.remove(i);
			else
				return this;
		}
		return this;
	}//removeLeadingZeros()

	@Override
	public int compareTo(BigInt o) {
		BigIntLL b = (BigIntLL) o;
		if (digits.size() > b.digits.size())
			return 1;
		if (digits.size() < b.digits.size())
			return -1;
		for (Iterator<Integer> i1 = this.iterator(), i2 = b.iterator(); i1.hasNext();) {
			int one = i1.next();
			int two = i2.next();
			if (one > two )
				return 1;
			else if (one < two)
				return -1;
		}
		return 0;
	}//compareTo(BigInt o)

	@Override
	public Iterator<Integer> iterator() {
		return this.digits.iterator();
	}//iterator()
	
	/** Returns a listIterator() over the instance variable digits, which holds the digits of the BigIntLL
	 * @return an iterator over the instance variable digits
	 */
	public ListIterator<Integer> listIterator() {
		return this.digits.listIterator();
	}//listIterator()
	
	/** Returns a listIterator() over the instance variable digits, which holds the digits of the BigIntLL, starting at position i
	 * @param i int used as the starting index
	 * @return an iterator over the instance variable digits
	 */
	public ListIterator<Integer> listIterator(int i) {
		return this.digits.listIterator(i);
	}//listIterator(int i)
	
	/** Computes the product of this and the int passed as argument
	 * @param i int acting as the multiplier
	 * @return the product of this and i
	 */
	private BigIntLL mul(int i ) {
		BigIntLL result = new BigIntLL();
		ListIterator<Integer> it = this.listIterator(this.digits.size());
		while (it.hasPrevious()) {
			result.digits.add(0, i * it.previous());
		}
		return result;
	}//mul(int i)
	
	@Override
	public BigIntLL mul(BigInt m) {
		BigIntLL result = new BigIntLL();
		int exp = 1;
		ListIterator<Integer> i = ((BigIntLL) m).listIterator(((BigIntLL)m).digits.size());
		while (i.hasPrevious()) {
			result = result.add(this.mul(i.previous() * exp));
			exp *= 10;
		}
		return result;
	}//mul(BigInt m)
	
	public static void main (String[] args) {
		BigInteger b = new BigInteger("2");
		BigInteger p = b.pow(128);
		System.out.println("BIGINTEGER 2^128 = " + p);
		BigIntLL l = new BigIntLL("2");
		BigIntLL pp = (BigIntLL) l.pow(128);
		System.out.println("BIGINT     2^128 = " + pp);
	}//main

}//BigIntLL
