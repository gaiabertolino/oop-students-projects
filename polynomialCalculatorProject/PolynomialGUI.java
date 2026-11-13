package Bertolino.polynomials;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@SuppressWarnings("serial")
class MainWindow extends JFrame implements ActionListener{
	
	private LinkedList<PolynomialSet> polynomials = new LinkedList<>();
	private JMenuBar menu;
	private JMenu file, operations;
	private JMenuItem saveItem, openItem, evaluateItem, derivativeItem, add, mul, removeItem;
	private JTextField inputField, input;
	private JButton okay;
	private JPanel scroll;
	private JButton calculateButton, createButton;
	private double val;
	private JFrame valueWindow, inputName;
	private File textFile;
	
	public MainWindow() {
		setTitle("Polynomial calculator");
		setSize(700,500);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (confirmExit()) System.exit(0); } } );
		
		menu = new JMenuBar();
		file = new JMenu("File"); 
			saveItem = new JMenuItem("Save to file"); saveItem.addActionListener(this); file.add(saveItem); 
			openItem = new JMenuItem("Open from file"); openItem.addActionListener(this); file.add(openItem);
		operations = new JMenu("Operations"); 
			evaluateItem = new JMenuItem("Evaluate at a value of x"); evaluateItem.addActionListener(this); operations.add(evaluateItem);
			derivativeItem = new JMenuItem("Compute the first derivative"); derivativeItem.addActionListener(this); operations.add(derivativeItem); operations.addSeparator();
			add = new JMenuItem("Addition"); add.addActionListener(this); operations.add(add);
			mul = new JMenuItem("Multiplication");  mul.addActionListener(this); operations.add(mul);
			removeItem = new JMenuItem("Remove");  removeItem.addActionListener(this); operations.add(removeItem);
		menu.add(file); menu.add(operations);
		
		JLabel titleLabel = new JLabel("Welcome! Type some polynomials and select them to perform operations", JLabel.CENTER); titleLabel.setForeground(Color.red);     
		JLabel promptLabel = new JLabel("Type a polynomial>> ");

		inputField = new JTextField(20); 
		inputField.addActionListener(this);

		okay = new JButton("Save"); 
		okay.addActionListener(this);
		
		this.setJMenuBar(menu);
		
		JPanel topBar = new JPanel(); topBar.add(titleLabel, BorderLayout.CENTER); topBar.setBackground(Color.white); this.add(topBar, BorderLayout.NORTH); 
		JPanel bottomBar = new JPanel(); bottomBar.add(promptLabel); bottomBar.add(inputField); bottomBar.add(okay); bottomBar.setBackground(Color.white); 
		this.add(bottomBar, BorderLayout.SOUTH);
			
		scroll = new JPanel(new GridLayout(8,8,2,2)); add(scroll);
		
		menuNone();

	}//MainWindow

	private void refresh() {
         if (counter() == 0)
        	menuNone();
        else if (counter() == 1)
        	menuOne();
        else if (counter() == 2)
        	menuTwo();
        else
        	menuMore();
        revalidate(); repaint();
	}//refresh
	
	private boolean confirmExit(){
		   int choice=JOptionPane.showConfirmDialog( null, "Quit?", "You will lose any unsaved data!", JOptionPane.YES_NO_OPTION);
		   return choice==JOptionPane.YES_OPTION;
	}//confirmExit
	
	private void menuNone() {
		saveItem.setEnabled(false);
		evaluateItem.setEnabled(false);
		derivativeItem.setEnabled(false);
		add.setEnabled(false);
		mul.setEnabled(false);
		removeItem.setEnabled(false);
	}//menuNone()
	
	private void menuOne() {
		saveItem.setEnabled(true);
		evaluateItem.setEnabled(true);
		derivativeItem.setEnabled(true);
		add.setEnabled(false);
		mul.setEnabled(false);
		removeItem.setEnabled(true);
	}//menuOne()
	
	private void menuTwo() {
		saveItem.setEnabled(true);
		evaluateItem.setEnabled(false);
		derivativeItem.setEnabled(true);
		add.setEnabled(true);
		mul.setEnabled(true);
		removeItem.setEnabled(true);
	}//menuTwo()
	
	private void menuMore() {
		saveItem.setEnabled(true);
		evaluateItem.setEnabled(false);
		derivativeItem.setEnabled(true);
		add.setEnabled(false);
		mul.setEnabled(false);
		removeItem.setEnabled(true);
	}//menuMore()
	
	public void actionPerformed(ActionEvent e) {
		refresh();
		if (e.getSource() == okay) 
			createPolynomial();
		if (e.getSource() == removeItem)
			removeSelected(); 
		if ( e.getSource() == derivativeItem)
			differentiateSelected();
		if (e.getSource() == add)
			addSelected();
		if (e.getSource() == evaluateItem)
			openValueWindow();
		if (e.getSource() == calculateButton)
			evaluate();
		if (e.getSource() == mul)
			multiplySelected();
		if (e.getSource() == createButton)
			try {
				inputName.setVisible(false);
				createFile();
			} catch (IOException e1) {
				JFrame f = new ErrorWindow("There was a problem creating the file. Try again");
				f.setVisible(true);
			}
		if (e.getSource() == openItem)
			try {
				openFile();
			} catch (IOException e1) {
				JFrame f = new ErrorWindow("There was a problem opening the file. Try again");
				f.setVisible(true);
			}
		if (e.getSource() == saveItem)
				askCreateNewFile();
			
	}//actionPerformed()

	private void saveFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showSaveDialog(this);
		try {
			if (n == JFileChooser.APPROVE_OPTION) {
				textFile = fileChooser.getSelectedFile();
				BufferedWriter write = new BufferedWriter(new FileWriter(textFile));
				for (int i=0; i<scroll.getComponentCount(); i++) {
					if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
						write.append(polynomials.get(i).toString());
						write.newLine();
						}
					}
				write.flush();
				write.close();
	      }
		}
		catch (FileNotFoundException e) {
			JFrame error = new ErrorWindow("The file does not exist");
			error.setVisible(true);
		}
	}//saveFile()
		
	private void createFile() throws IOException {
		String path = input.getText() + ".txt"; 
		textFile = new File(path);
		if (!textFile.exists())
			textFile.createNewFile();
		saveFile();
	}//createFile()
	
	private void askCreateNewFile(){
	   int choice=JOptionPane.showConfirmDialog( null, "Do you want to create a new file?", "File", JOptionPane.YES_NO_OPTION);
	   if (choice==JOptionPane.YES_OPTION) {
		   input = new JTextField(10);
		   input.addActionListener(this);
		   createButton = new JButton("Create");
		   createButton.addActionListener(this);
		   inputName = new FileInputWindow();
		   inputName.setVisible(true);
		} else
		try {
			saveFile();
		} catch (IOException e) {
			JFrame f = new ErrorWindow("There was a problem with the file. Try again");
			f.setVisible(true);
		}
	}//askCreateNewFile()
	
	private void openFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new TxtFileFilter());
		int n = fileChooser.showOpenDialog(this);
		File textFile;
		try {
			if (scroll.getComponentCount() >= 64) throw new IndexOutOfBoundsException();
			if (n == JFileChooser.APPROVE_OPTION) {
	          textFile = fileChooser.getSelectedFile();
	          @SuppressWarnings("resource")
	          BufferedReader read = new BufferedReader(new FileReader(textFile));
	          String line = "";
	          while(line != null) {
	            line = read.readLine();
	            if (line == null) break;
	            if (Regex.isPolynomial(line) == false) throw new IllegalArgumentException();
	            PolynomialSet p = Regex.parse(line); polynomials.add(p); JCheckBox c = new JCheckBox(p.toString()); c.addActionListener(this); scroll.add(c); refresh(); 
	          }
	          read.close();
	        }		
		} 
		catch (FileNotFoundException e) {
			JFrame error = new ErrorWindow("The file does not exist");
			error.setVisible(true);
		} 
		catch (IndexOutOfBoundsException e) {
			JFrame errorWindow = new ErrorWindow("You have reached the limit. Remove some polynomials");
			errorWindow.setVisible(true); 
		}
		catch (IllegalArgumentException e) {
			JFrame errorWindow = new ErrorWindow("The file contains something other than polynomials. Try again");
			errorWindow.setVisible(true);
		}
	}//openFile()
	
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
	
	private void evaluate() {
		valueWindow.setVisible(false);
		try { val = Double.parseDouble(input.getText()); 
			for (int i=0; i<scroll.getComponentCount(); i++)
				if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
					val = polynomials.get(i).valueAt(val);
				}
			OutputWindow f = new OutputWindow(); 
			f.setVisible(true);
		}
		catch (NumberFormatException e) {
			JFrame error = new ErrorWindow("You typed a non-numeric value!");
			error.setVisible(true);
		}
	}//evaluate()
		
	private void addSelected() {
		int end = scroll.getComponentCount();
		int i = 0;
		Polynomial p1 = new PolynomialSet();
		Polynomial p2 = new PolynomialSet();
		boolean flag = false;
		while (i<end) {
			if (((JCheckBox) scroll.getComponent(i)).isSelected() && !flag) {
				p1 = (PolynomialSet) polynomials.get(i);
				flag = true;
			}
			else if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
				p2 = (PolynomialSet) polynomials.get(i);
				break;
			}
			i++;
		} 
		p1 = p1.add(p2); JCheckBox c = new JCheckBox(p1.toString()); scroll.add(c); c.addActionListener(this); polynomials.add((PolynomialSet) p1); refresh();
	}//addSelected
	
	private int counter() {
		int result = 0;
		for (int i=0; i<scroll.getComponentCount(); i++)
			if (((JCheckBox) scroll.getComponent(i)).isSelected())
				result++;
		return result;
	}//counter()
	
	private void differentiateSelected() {
		int end = scroll.getComponentCount();
		int i = 0;
		while (i<end) {
			JCheckBox c = (JCheckBox) scroll.getComponent(i);
			if (c.isSelected()) {
				PolynomialSet p = (PolynomialSet) polynomials.get(i).derivative();
				if (p.size() == 0) {
					JFrame f = new ErrorWindow("The polynomial " + polynomials.get(i) + " is a constant. Its derivative is zero");
					f.setVisible(true);
				}
				else {
					polynomials.add(p);
					JCheckBox d = new JCheckBox(p.toString()); scroll.add(d); d.addActionListener(this);
				} 
			}
			i++;
		}
	 refresh();
	}//differentiateSelected()
	
	private void multiplySelected() {
		int end = scroll.getComponentCount();
		int i = 0;
		Polynomial p1 = new PolynomialSet();
		Polynomial p2 = new PolynomialSet();
		boolean flag = false;
		while (i<end) {
			if (((JCheckBox) scroll.getComponent(i)).isSelected() && !flag) {
				p1 = (PolynomialSet) polynomials.get(i);
				flag = true;
			}
			else if (((JCheckBox) scroll.getComponent(i)).isSelected()) {
				p2 = (PolynomialSet) polynomials.get(i);
				break;
			}
			i++;
		} 
		p1 = p1.mul(p2); JCheckBox c = new JCheckBox(p1.toString()); scroll.add(c); c.addActionListener(this); polynomials.add((PolynomialSet) p1); refresh();
	}//multiplySelected()
	
	private void removeSelected() {
		int end = scroll.getComponentCount();
		int i = 0;
		while (i<end) {
			JCheckBox c = (JCheckBox) scroll.getComponent(i);
			if (c.isSelected()) {
				polynomials.remove(i);
				end--;
				scroll.remove(c);
			}
			else
				i++;
		}
		refresh();
	}//removeSelected()
	
	private void createPolynomial() {
		PolynomialSet p = new PolynomialSet();
		String typed = inputField.getText();
		try {
			if (scroll.getComponentCount() >= 64) throw new IndexOutOfBoundsException();
			if (typed.equals("")) throw new NoSuchElementException();
			if (Regex.isPolynomial(typed) == false) throw new IllegalArgumentException();
			p = Regex.parse(typed); polynomials.add(p); JCheckBox c = new JCheckBox(p.toString()); c.addActionListener(this); scroll.add(c); refresh(); 
		}
		catch (IndexOutOfBoundsException e) {
			JFrame errorWindow = new ErrorWindow("You have reached the limit. Remove some polynomials");
			errorWindow.setVisible(true); 
		}
		catch (IllegalArgumentException e) {
			JFrame errorWindow = new ErrorWindow("You typed an invalid polynomial. Try again");
			errorWindow.setVisible(true);
		}
		catch (NoSuchElementException ex) { 
			JFrame errorWindow = new ErrorWindow("Type a polynomial before continuing");
			errorWindow.setVisible(true);
		}
	}//createPolynomial()
	
	private class ErrorWindow extends JFrame{
		public ErrorWindow(String message) {
			setTitle("Error!");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel label = new JLabel(message); label.setFont(f); label.setForeground(Color.red); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//ErrorWindow
	
	private class OutputWindow extends JFrame {
		public OutputWindow() {
			setTitle("Result");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel label = new JLabel("The value of the polynomial is " + val); label.setFont(f); label.setForeground(Color.black); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//OutputWindow
	
	private void openValueWindow() {
		input = new JTextField(10);
		input.addActionListener(this);
		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(this);
		valueWindow = new ValueInputWindow();
		valueWindow.setVisible(true);
	}//openValueWindow()
	
	private class ValueInputWindow extends JFrame {
		public ValueInputWindow() {
			setTitle("Input");
			setSize(400,100);
			setLocation(550,350);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel label = new JLabel("Enter a value for x>> "); label.setFont(f); label.setForeground(Color.black); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER); add(input, BorderLayout.EAST); add(calculateButton, BorderLayout.SOUTH);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//ValueInputWindow
	
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
	
}//MainWindow

public class PolynomialGUI {
	public static void main(String[] args) {
		JFrame window = new MainWindow();
		window.setVisible(true);
	}//main
	
}//PolynomialGUI