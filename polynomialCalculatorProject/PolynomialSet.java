package Bertolino.polynomials;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PolynomialSet extends AbstractPolynomial{
	
	private Set<Monomial> monomials=new TreeSet<>();
	
	public int size() { 
		return monomials.size(); 
	}//size
	
	public Iterator<Monomial> iterator(){ 
		return monomials.iterator(); 
	}//iterator
	
	public PolynomialSet create() { 
		return new PolynomialSet(); 
	}//create
	
	public void add( Monomial m ) {
		if( m.getCoeff()==0 ) return;
		Iterator<Monomial> it=monomials.iterator();
		Monomial ms=null;
		while( it.hasNext() ) {
			Monomial mc=it.next();
			if( mc.equals(m) ) {
				it.remove();
				ms=mc;
				break;
			}
		}
		if( ms!=null ) {
			ms=ms.add(m);
			if( ms.getCoeff()!=0 ) monomials.add(ms);
		}
		else monomials.add(m);
	}//add

}//PolynomialSet