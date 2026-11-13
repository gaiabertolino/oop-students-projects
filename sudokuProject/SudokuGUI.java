package Bertolino.sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
class GameWindow extends JFrame implements ActionListener{
	
	private JPanel grid = new JPanel(new GridLayout(9,9));
	protected static Sudoku game = new Sudoku();
	private JButton solutionsButton = new JButton("Show the solutions");
	private JButton newGameButton = new JButton("Start a new game");
	private JButton saveButton = new JButton("Save the game");
	private JButton loadButton = new JButton("Load a game");
	private ChoicePoint ps;
	private JTextField input = new JTextField(10);
	private JButton createButton = new JButton("Create");
	private JFrame inputName = new FileInputWindow();
	protected JFrame solutionsWindow;
	protected JFrame newGameWindow;
	private Component[] cells;
	
	Font f = new Font("Helvetica", Font.ROMAN_BASELINE, 25);
	
	public GameWindow() {
		game = new Sudoku();
		setTitle("GaiaSudoku");
		setSize(700,600);
		setLocation(400,100);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (confirmExit()) System.exit(0); } } );

		for (int i=0; i<81; i++) {
			JTextField cell = new JTextField(3);
			cell.setHorizontalAlignment(NORMAL);
			cell.setFont(f); cell.addActionListener(this);
			grid.add(cell);
		}
		JPanel buttonPanel = new JPanel(); grid.setBackground(Color.red);
		solutionsButton.addActionListener(this); newGameButton.addActionListener(this); saveButton.addActionListener(this); loadButton.addActionListener(this);
		buttonPanel.add(solutionsButton); buttonPanel.add(newGameButton); buttonPanel.add(saveButton); buttonPanel.add(loadButton); buttonPanel.setBackground(Color.red);
		add(grid); add(buttonPanel, BorderLayout.SOUTH); solutionsButton.setEnabled(false);
		saveButton.setEnabled(false);
		newGameButton.setEnabled(false);
		cells = grid.getComponents();
	}
	
	private boolean confirmExit(){
		   int choice=JOptionPane.showConfirmDialog( null, "Quit?", "You will lose any unsaved game!", JOptionPane.YES_NO_OPTION);
		   return choice==JOptionPane.YES_OPTION;
	}//confirmExit
	
	private void checkCellInput(JTextField t, int i) {
		try {
			String text = t.getText();
			int row = i/9;
			int col = i%9;
			ps = new ChoicePoint(row, col);
			int s = Integer.parseInt(text);
			if (game.isAssignable(ps, s)) {
				t.setEnabled(false);
				game.assign(ps,s);
			}
			else {
				JFrame errorWindow = new ErrorWindow("This number breaks the rules of the game");
				errorWindow.setVisible(true);
			}
		}
		catch (NumberFormatException e) {
			JFrame errorWindow = new ErrorWindow("Enter numbers only");
			errorWindow.setVisible(true);
		}
		catch (IllegalArgumentException e) {
			JFrame errorWindow = new ErrorWindow("Enter only values between 1 and 9");
			errorWindow.setVisible(true);
		}
	}//checkCellInput

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == solutionsButton) {
			solutionsWindow = new SolutionsWindow();
			setVisible(false);
			solutionsWindow.setVisible(true);
		}
		if (e.getSource() == saveButton)
			askCreateNewFile();
		if (e.getSource() == newGameButton)
			newGame();
		if (e.getSource() == loadButton) {
			try {
				loadGame();
			} catch (IOException e1) {
				JFrame errorWindow = new ErrorWindow("There was a problem with the file. Try again");
				errorWindow.setVisible(true);
			}
		}
		if (e.getSource() == createButton) {
			try {
				inputName.setVisible(false);
				createFile();
			} catch (IOException e1) {
				JFrame f = new ErrorWindow("There was a problem creating the file. Try again");
				f.setVisible(true);
			}
		}
		for (int i=0; i<grid.getComponentCount(); i++ ) {
			if (e.getSource() == cells[i]) {
				solutionsButton.setEnabled(true);
				saveButton.setEnabled(true);
				newGameButton.setEnabled(true);
				checkCellInput((JTextField) cells[i], i);
			}
		}
	}//actionPerformed
	
	private void createFile() throws IOException {
		String path = input.getText() + ".txt"; 
		File newFile = new File(path);
		if (!newFile.exists())
			newFile.createNewFile();
		saveGame();
	}//createFile()
	
	private void loadGame() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showOpenDialog(this);
		File file;
		try {
			if (n == JFileChooser.APPROVE_OPTION) {
	          file = fileChooser.getSelectedFile();
	          BufferedReader read = new BufferedReader(new FileReader(file));
	          String line = "";
	          String allLines = "";
	          while(line != null) {
	            line = read.readLine();
	            if (line == null) break;
	            allLines+= line;
	          }
	          read.close();
	          StringTokenizer s = new StringTokenizer(allLines," ");
	          for (int i=0; i<81; i++) {
	        	  String t = s.nextToken();
	        	  game.assign(new ChoicePoint(i/9, i%9), Integer.parseInt(t));
	        	  if (!t.equals("0")) {
		        	  ((JTextField) grid.getComponent(i)).setText(t);
		        	  ((JTextField) grid.getComponent(i)).setEnabled(false);
	        	  }
	        	  else {
	        		  ((JTextField) grid.getComponent(i)).setText("");
	        		  ((JTextField) grid.getComponent(i)).setEnabled(true);
	        	  }
	          }
			}
			newGameButton.setEnabled(true);
			solutionsButton.setEnabled(true);
		}
		catch (FileNotFoundException e) {
			JFrame error = new ErrorWindow("The file does not exist");
			error.setVisible(true);
		} 
		catch (IllegalArgumentException e) {
			JFrame errorWindow = new ErrorWindow("The file does not contain valid numbers. Try again");
			errorWindow.setVisible(true);
		}
	}
	
	void newGame() {
		GameWindow.game.solutions = new ArrayList<>();
		Sudoku.solutionCount = 0;
		solutionsButton.setEnabled(false);
		for (int i=0; i<81; i++) {
			((JTextField) grid.getComponent(i)).setText("");
			((JTextField) grid.getComponent(i)).setEnabled(true);
			game.assign(new ChoicePoint(i/9, i%9), 0);
        }
		saveButton.setEnabled(false);
		newGameButton.setEnabled(false);
		setVisible(true);
	}//newGame
	
	private void askCreateNewFile(){
		   int choice=JOptionPane.showConfirmDialog( null, "Do you want to create a new file?", "File", JOptionPane.YES_NO_OPTION);
		   if (choice==JOptionPane.YES_OPTION) {
			   input.addActionListener(this);
			   createButton.addActionListener(this);
			   inputName = new FileInputWindow();
			   inputName.setVisible(true);
			} 
		   else {
				try {
					saveGame();
				} catch (IOException e) {
					JFrame f = new ErrorWindow("There was a problem with the file. Try again");
					f.setVisible(true);
				}
		   }
		}//askCreateNewFile()
	
	private void saveGame() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showSaveDialog(this);
		try {
			if (n == JFileChooser.APPROVE_OPTION) {
				File toSave = fileChooser.getSelectedFile();
				if (!toSave.exists() || toSave==null) throw new FileNotFoundException();
				BufferedWriter write = new BufferedWriter(new FileWriter(toSave));
				int[][] board = GameWindow.game.getBoard();
				for (int i=0; i<board.length; i++) {
					for (int j=0; j<board.length; j++) {
						write.append(board[i][j] + " ");
					}
					write.newLine();
					write.flush();
				}
				write.close();
	      }
		}
		catch (FileNotFoundException e) {
			JFrame error = new ErrorWindow("The file does not exist");
			error.setVisible(true);
		}
	}//saveGame
	
	final class TxtFileFilter extends FileFilter {
		  public boolean accept(File file) {
		    if (file.isDirectory()) return true;
		    String fname = file.getName().toLowerCase();
		    return fname.endsWith("txt");
		  }
		  public String getDescription() {
		    return "Text files";
		  }
	}//TxtFileFilter()
	
	private class ErrorWindow extends JFrame{
		public ErrorWindow(String message) {
			setTitle("Error!");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 20);
			JLabel label = new JLabel(message); label.setFont(f); label.setForeground(Color.red); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//ErrorWindow
	
	private class FileInputWindow extends JFrame {
		public FileInputWindow() {
			setTitle("File");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 20);
			JLabel label = new JLabel("Enter the file path>> "); label.setFont(f); label.setForeground(Color.black); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER); add(input, BorderLayout.EAST); add(createButton, BorderLayout.SOUTH);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//FileInputWindow
	
}//GameWindow


@SuppressWarnings("serial")
class SolutionsWindow extends JFrame implements ActionListener {
	
	private JPanel grid = new JPanel(new GridLayout(9,9));
	private int current = 0;
	private JButton next = new JButton("NEXT");
	private JButton previous = new JButton("PREVIOUS");
	private JButton newGameButton = new JButton("NEW GAME");
	
	public SolutionsWindow() {
		setTitle("GaiaSudoku");
		setSize(700,600);
		setLocation(400,100);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (confirmExit()) System.exit(0); } } );
		
		JPanel buttonPanel = new JPanel(); buttonPanel.setBackground(Color.red); grid.setBackground(Color.red); 
		next.addActionListener(this); previous.addActionListener(this); newGameButton.addActionListener(this);
		buttonPanel.add(previous); buttonPanel.add(next); buttonPanel.add(newGameButton);
		add(grid); add (buttonPanel, BorderLayout.SOUTH);
		
		GameWindow.game.solve();
		updateButtons();
		showSolution(current);
	}//SolutionsWindow
	
	private void updateButtons() {
		if (current == 0) {
			next.setEnabled(true);
			previous.setEnabled(false);
		}
		else if (current == 9) {
			next.setEnabled(false);
			previous.setEnabled(true);
		}
		else {
			next.setEnabled(true);
			previous.setEnabled(true);
		}
	}//updateButtons
	
	private void showSolution(int n) {
		Font f = new Font("Helvetica", Font.ROMAN_BASELINE, 25);
		int[][] board = GameWindow.game.solutions.get(n);
		grid = new JPanel(new GridLayout(9,9));
		for (int i=0; i<81; i++) {
			int row = i/9;
			int col = i%9;
			String s = ""+board[row][col];
			JTextField cell = new JTextField(3); cell.setText(s); cell.setEditable(false);
			cell.setHorizontalAlignment(NORMAL);
			cell.setFont(f); this.remove(grid); 
			grid.add(cell); add(grid);
			revalidate(); repaint();
		}
	}//showSolution
	
	private boolean confirmExit(){
		   int choice=JOptionPane.showConfirmDialog( null, "Quit?", "Come back and play soon!", JOptionPane.YES_NO_OPTION);
		   return choice==JOptionPane.YES_OPTION;
	}//confirmExit
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == next) {
			current++;
			updateButtons();
			showSolution(current);
		}
		if (e.getSource() == previous) {
			current--;
			updateButtons();
			showSolution(current);
		}
		if (e.getSource() == newGameButton) {
			setVisible(false);
			((GameWindow) WelcomeWindow.gameWindow).newGame();
		}
	}//actionPerformed
	
}//SolutionsWindow


@SuppressWarnings("serial") 
class WelcomeWindow extends JFrame implements ActionListener{
	
	protected static JFrame gameWindow;
	private JButton startButton ;
	
	public WelcomeWindow() {
		setTitle("GaiaSudoku");
		setSize(600,330);
		setLocation(500,200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (confirmExit()) System.exit(0); } } );
		
		Font f2 = new Font("Helvetica", Font.BOLD, 15);
		JLabel welcome = new JLabel("Welcome!"); welcome.setFont(f2);
		
		String rulesText = "                                        Instructions: \n"
				+ "                  1) Enter only numbers between 1 and 9\n"
				+ "         2) Once a number is entered it cannot be changed\n"
				+ "      3) You can view the possible solutions at any time!\n"
				+ "   4) You cannot enter a number already present in the same\n"
				+ "                         row, column or 3x3 box!\n\n"
				+ "                                     Have fun!!\n\n";
		
		Font f1 = new Font("Helvetica", Font.ROMAN_BASELINE, 20);
		JTextArea rulesArea = new JTextArea(rulesText, 20, 20); rulesArea.setEditable(false); rulesArea.setFont(f1); 
		
		startButton = new JButton("Click here to start playing!");
		startButton.addActionListener(this);
		
		JPanel title = new JPanel(); title.add(welcome, BorderLayout.NORTH); title.setBackground(Color.magenta);
		JPanel panel = new JPanel(); panel.setBackground(Color.white);
		JPanel south = new JPanel(); south.add(startButton);
		panel.add(title, BorderLayout.NORTH); panel.add(rulesArea, BorderLayout.CENTER);
		add(panel); add(south,BorderLayout.SOUTH);
	}//WelcomeWindow

	private boolean confirmExit(){
		   int choice=JOptionPane.showConfirmDialog( null, "Quit?", "You haven't played yet!", JOptionPane.YES_NO_OPTION);
		   return choice==JOptionPane.YES_OPTION;
	}//confirmExit

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			SudokuGUI.welcomeWindow.setVisible(false);
			gameWindow = new GameWindow();
			gameWindow.setVisible(true);
		}
	}//actionPerformed
}//WelcomeWindow

public class SudokuGUI {
	
	public static JFrame welcomeWindow;

	public static void main (String[] args) {
		welcomeWindow = new WelcomeWindow();
		welcomeWindow.setVisible(true);
	}//main
	
}//SudokuGUI
