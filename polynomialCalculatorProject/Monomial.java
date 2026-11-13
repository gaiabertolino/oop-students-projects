package Bertolino.polynomials;

public class Monomial implements Comparable<Monomial> {
	
	// INSTANCE VARIABLES
	private final int COEFF, DEGREE;
	
	// CONSTRUCTOR
	public Monomial( final int coeff, final int degree ) {
		if( degree<0 ) throw  new IllegalArgumentException();
		this.COEFF=coeff; 
		this.DEGREE=degree;
	}//Monomial
	
	// CONSTRUCTOR
	public Monomial( Monomial m ) {
		this.COEFF=m.COEFF;
		this.DEGREE=m.DEGREE;
	}//Monomial
	
	// GETTERS
	public int getCoeff() { 
		return COEFF; 
	}//getCoeff
	
	public int getDegree() { 
		return DEGREE; 
	}//getDegree
	
	// OTHER METHODS
	public Monomial add( Monomial m ) {
		if( !this.equals(m) ) 
			throw new RuntimeException("Monomials are not like terms!");
		return new Monomial( COEFF+m.COEFF, DEGREE );
	}//add
	
	public Monomial mul( int s ) {
		return new Monomial( COEFF*s, DEGREE );
	}//mul
	
	public Monomial mul( Monomial m ) {
		return new Monomial( COEFF*m.COEFF, DEGREE+m.DEGREE );
	}//mul
	
	@Override
	public boolean equals( Object x ) {
		if( !(x instanceof Monomial) ) return false;
		if( x==this ) return true;
		Monomial m=(Monomial)x;
		return this.DEGREE==m.DEGREE;
	}//equals
	
	@Override
	public int hashCode() {
		return DEGREE;
	}//hashCode
	
	public int compareTo( Monomial m ) {
		if( this.DEGREE>m.DEGREE ) return -1;
		if( this.equals(m) ) return 0;
		return 1;
	}//compareTo
	
	public String toString() {
		StringBuilder sb=new StringBuilder(30);
		if( COEFF==0 ) 
			sb.append(0);
		else {
			if( COEFF<0 ) //negative coefficient
				sb.append('-');
			if( Math.abs(COEFF)!=1 || DEGREE==0 ) //coefficient different from 1, or degree equal to 0
				sb.append( Math.abs(COEFF) );
			if( COEFF!=0 && DEGREE>0 ) //non-zero coefficient and degree greater than 0
				sb.append('x');
			if( COEFF!=0 && DEGREE>1 ) { //non-zero coefficient and degree greater than 1
				sb.append('^'); 
				sb.append(DEGREE); 
			}
		}
		return sb.toString();
	}//toString
	
}//Monomial
