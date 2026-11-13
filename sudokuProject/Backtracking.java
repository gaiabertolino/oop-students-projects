package Bertolino.sudoku;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class Backtracking<P,S> {
	
	protected abstract boolean isAssignable( P p, S s );
	protected abstract void assign( P ps, S s );
	protected abstract void unassign( P ps, S s );
	protected abstract void recordSolution( P p );
	
	protected P nextChoicePoint( List<P> ps, P p ) {
		if( isSolutionComplete(p) ) throw new NoSuchElementException();
		int i=ps.indexOf(p);
		return ps.get(i+1);
	}//nextChoicePoint
	
	protected boolean isSolutionComplete( P p ) {
		return false;
	}//isSolutionComplete
	
	protected boolean isLastSolution( P p ) {
		return false; //look for all possible solutions
	}//isLastSolution
	
	//factory
	protected abstract List<P> choicePoints();
	protected abstract Collection<S> choices( P p );
	
	protected void attempt( List<P> ps, P p ) {
		Collection<S> admissible = choices(p); //admissible choices for p
		for( S s: admissible) {
			if( isLastSolution(p) ) break;
			if( isAssignable(p,s) ) {
				assign(p,s);
				if( isSolutionComplete(p) ) recordSolution(p);
				else attempt( ps, nextChoicePoint(ps,p) );
				unassign(p,s);
			}
		}
	}//attempt
	
	protected abstract void solve();
	
}//Backtracking
