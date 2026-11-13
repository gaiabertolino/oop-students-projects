package Bertolino.expression;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EmptyStackException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
class MainWindow extends JFrame implements ActionListener{
	
	private JLabel title = new JLabel("Welcome! ", JLabel.CENTER);
	private JLabel track = new JLabel("Enter an expression to evaluate >> ");
	private JTextField text = new JTextField(20);
	private JButton calculateButton = new JButton("Calculate");
	private String result ="";

	public MainWindow() {
		setTitle("GaiaMath");
		setSize(600,100);
		setLocation(450,300);
		setBackground(Color.CYAN);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { 
			if (confirmExit()) System.exit(0); } } );
		text.addActionListener(this); calculateButton.addActionListener(this);
		
		JPanel inputPanel = new JPanel(); inputPanel.setBackground(Color.WHITE);
		inputPanel.add(track, BorderLayout.WEST); inputPanel.add(text, BorderLayout.CENTER); inputPanel.add(calculateButton, BorderLayout.EAST);	
		
		JPanel titlePanel = new JPanel(); titlePanel.add(title, BorderLayout.NORTH); 
		
		add(titlePanel, BorderLayout.NORTH); add(new JSeparator(SwingConstants.HORIZONTAL));  add(inputPanel,BorderLayout.SOUTH);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calculateButton) {
			JFrame window = new ResultWindow();
			window.setVisible(true);
		}
		if (e.getSource() == text)
			checker();
	}
	
	void checker() {
		try {
			if (text.getText() == null)
				calculateButton.setEnabled(false);
			else if (text.getText().length() > 0 && Regex.isWellFormed(text.getText())) {
				String exp = text.getText();
				StringTokenizer s = new StringTokenizer(exp,"+-/%*^()",true);
				result = Evaluator.evaluate(s);
				calculateButton.setEnabled(true);
			}
			else
				calculateButton.setEnabled(false);
		}
		catch (IllegalArgumentException e) {
			calculateButton.setEnabled(false);
		}
		catch (EmptyStackException e) {
			calculateButton.setEnabled(false);
		}
		catch (NullPointerException e) {
			calculateButton.setEnabled(false);
		}
	}//checker

	private boolean confirmExit(){
		   int choice=JOptionPane.showConfirmDialog( null, "Do you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
		   return choice==JOptionPane.YES_OPTION;
	}//confirmExit 
	
	private class ResultWindow extends JFrame {
		public ResultWindow() {
			setTitle("Result");
			setSize(300,80);
			setLocation(600,310);
			Font f = new Font("Helvetica", Font.PLAIN, 15);
			JLabel label = new JLabel("The result of the expression is " + result); label.setFont(f); label.setForeground(Color.black); label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { setVisible(false);} } );
		}
	}//ResultWindow

}//MainWindow
	
public class EvaluatorGUI {
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setVisible(true);
		for (;;)
			window.checker();
	}
}//EvaluatorGUI
