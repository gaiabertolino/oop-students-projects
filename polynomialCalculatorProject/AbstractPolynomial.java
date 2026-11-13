package Bertolino.polynomials;
import java.util.Iterator;

public abstract class AbstractPolynomial implements Polynomial{
	
	public String toString() {
		StringBuilder sb = new StringBuilder(300);
		Iterator<Monomial> it = this.iterator();
		boolean flag = true; //true while we are on the first monomial
		while( it.hasNext() ) {
			Monomial m = it.next();
			if( m.getCoeff()>0 && !flag ) 
				sb.append('+');
			sb.append(m);
			if( flag ) 
				flag = !flag;
		}
		return sb.toString();
	}//toString
	
	public boolean equals( Object x ) {
		if( !(x instanceof Polynomial) ) 
			return false;
		if( x==this ) 
			return true;
		Polynomial p = (Polynomial)x;
		if( this.size() != p.size() ) 
			return false;
		Iterator<Monomial> i1 = this.iterator(), i2 = p.iterator();
		while( i1.hasNext() ) {
			Monomial m1 = i1.next();
			Monomial m2 = i2.next();
			if( m1.getCoeff() != m2.getCoeff() || m1.getDegree() != m2.getDegree() ) 
				return false;
		}
		return true;
	}//equals
	
	public int hashCode() {
		final int M = 83;
		int h = 0;
		for( Monomial m: this )
			h = h * M + (String.valueOf(m.getCoeff())+String.valueOf(m.getDegree())).hashCode();
		return h;
	}//hashCode
	
}//AbstractPolynomial
