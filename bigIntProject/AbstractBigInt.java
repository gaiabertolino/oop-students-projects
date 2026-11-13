/** @author Gaia Bertolino */

package Bertolino.math;

import java.util.Iterator;

public abstract class AbstractBigInt implements BigInt {
	
	@Override
	public boolean equals( Object o ) {
		if( !(o instanceof BigInt) ) 
			return false;
		if( o==this ) 
			return true;
		BigInt b = (BigInt) o;
		if (b.length() != this.length())
			return false;
		Iterator<Integer> i1 = iterator();
		Iterator<Integer> i2 = b.iterator();
		while (i1.hasNext()) {
			if (i1.next() != i2.next())
				return false;
		}
		return true;
	}//equals(Object o)
	
	
	@Override
	public String toString() {
		StringBuilder number = new StringBuilder();
		for (Integer digit: this)
			number.append(digit);
		return number.toString();
	}//toString()
	
	
	@Override
	public int hashCode() {
		final int M = 83;
		int h = 0;
		for (Integer digit : this)
			h = h * M + digit;
		return h;
	}//hashCode()

}//AbstractBigInt
