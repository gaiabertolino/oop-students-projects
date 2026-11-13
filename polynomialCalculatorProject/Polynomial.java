package Bertolino.polynomials;
import java.util.Iterator;

public interface Polynomial extends Iterable<Monomial>{
	
	default int size() {
		int c=0;
		for( Iterator<Monomial> it=iterator(); it.hasNext(); it.next(),c++ );
		return c;
	}//size
	
	void add( Monomial m );
	
	Polynomial create(); //factory method
	
	default Polynomial add( Polynomial p ) {
		Polynomial sum=create();
		for( Monomial m: this ) sum.add(m);
		for( Monomial m: p ) sum.add(m);
		return sum;
	}//add
	
	default Polynomial mul( Monomial m ) {
		Polynomial product=create();
		for( Monomial m1: this )
			product.add( m.mul(m1) );
		return product;
	}//mul
	
	default Polynomial mul( Polynomial p ) {
		Polynomial product=create();
		for( Monomial m: this )
			product=product.add( p.mul(m) );
		return product;
	}//mul
	
	default Polynomial derivative() {
		Polynomial d=create();
		for( Monomial m: this )
			if( m.getDegree()>0 )
				d.add( new Monomial( m.getCoeff()*m.getDegree(), m.getDegree()-1 ) );
		return d;
	}//derivative
	
	default double valueAt( double x ) {
		double v=0.0D;
		for( Monomial m: this )
			v=v+m.getCoeff()*Math.pow(x, m.getDegree());
		return v;
	}//valueAt

	
}//Polynomial
