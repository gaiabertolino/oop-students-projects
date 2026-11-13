package Bertolino.sudoku;

class ChoicePoint {
	
	private int i;
	private int j;
	
	public ChoicePoint(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	@Override
	public String toString() {
		return "<" + i + "," + j + ">";
	}//toString
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof ChoicePoint))
			return false;
		ChoicePoint p = (ChoicePoint) o;
		return p.i==this.i && p.j==this.j;
	}//equals
}