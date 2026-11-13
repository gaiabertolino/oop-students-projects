package Bertolino.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Sudoku extends Backtracking<ChoicePoint,Integer> {
	
	private int[][] board;
	private List<ChoicePoint> choicePoints;
	protected List<int[][]> solutions = new ArrayList<>();
	protected static int solutionCount = 0;
	
	public Sudoku() {
		board = new int[9][9];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[0].length; j++) {
				board[i][j] = 0;
			}
		}
		choicePoints = choicePoints();
	}//Sudoku()
	
	public int[][] getBoard() {
		int[][] result = new int[9][9];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[0].length; j++) {
				result[i][j] = board[i][j];
			}
		}
		return result;
	}//getBoard()

	private boolean checkSubgrid(int i, int j, int v) {
		int r = 0;
		int c = 0;
		
		if (i<=2 && j<=2);
		else if (i<=2 && j<=5)
			c = 3;
		else if (i<=2 && j<=8) {
			c = 6;
		}
		else if (i<=5 && j<=2)
			r = 3;
		else if (i<=5 && j<=5) {
			r = 3;
			c = 3;
		}
		else if (i<=5 && j<=8) {
			r = 3;
			c = 6;
		}
		else if (i<=8 && j<=2)
			r = 6;
		else if (i<=8 && j<=5) {
			r = 6;
			c = 3;
		}
		else {
			r = 6;
			c = 6;
		}
		
		for (int row=r+0; row<r+3; row++) {
			for (int col=c+0; col<c+3; col++) {
				if(board[row][col] == v)
					return false;
			}
		}
		return true;
	}//checkSubgrid
	
	private void setCell(int i, int j, int v) {
		board[i][j] = v;
	}//setCell
	
	@Override
	protected boolean isAssignable( ChoicePoint ps, Integer s ) {
		if (s<1 || s>9) throw new IllegalArgumentException();
		int i = ps.getI();
		int j = ps.getJ();
		if (board[i][j] !=0) {
			if (board[i][j] == s) {
				if( isSolutionComplete(ps) ) recordSolution(ps);
				else attempt( choicePoints, nextChoicePoint(choicePoints,ps) );}
			return false;
		}
		for (int row=0; row<board.length; row ++) {
			if (board[row][j] == s) return false;
		}
		for (int col=0; col<board.length; col++) {
			if (board[i][col] == s) return false;
		}
		if (!checkSubgrid(i,j,s))
			return false;
		return true;
	}//isAssignable
	
	@Override
	protected void assign( ChoicePoint ps, Integer s ) {
		setCell(ps.getI(),ps.getJ(),s);
	}//assign
	
	@Override
	protected List<ChoicePoint> choicePoints() {
		List<ChoicePoint> choicePoints = new ArrayList<>(81);
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				choicePoints.add(new ChoicePoint(i,j));
			}
		}
		return choicePoints;
	}//choicePoints
	
	@Override
	protected void unassign( ChoicePoint ps, Integer s ) {
		board[ps.getI()][ps.getJ()] = 0;
	}//unassign
	
	@Override
	protected void recordSolution( ChoicePoint p) {
		int[][] snapshot = new int[9][9];
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board.length; j++) {
				snapshot[i][j] = board[i][j];
			}
		}
		solutions.add(snapshot);
		solutionCount++;
	}//recordSolution

	@Override
	protected Collection<Integer> choices(ChoicePoint p) {
		Integer[] values = {1,2,3,4,5,6,7,8,9};
		List<Integer> possibleValues = Arrays.asList(values);
		return possibleValues;
	}//choices
	
	@Override 
	protected boolean isLastSolution(ChoicePoint p) {
		if (solutionCount == 11)
			return true;
		return false;
	}//isLastSolution
	
	@Override
	protected boolean isSolutionComplete(ChoicePoint p) {
		if (p.getI() == board.length-1 && p.getJ() == board.length-1)
			return true;
		return false;
	}//isSolutionComplete
	
	@Override
	protected void solve() {
		attempt(choicePoints, new ChoicePoint(0,0));
	}//solve

}//Sudoku
