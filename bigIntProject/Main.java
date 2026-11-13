/** @author Gaia Bertolino */
package Bertolino.math;
import java.util.Scanner;
public class Main {
	
	/** Reads keyboard input through the given Scanner until a valid BigIntLL can be built. 
	 * When that happens, it leaves the loop and returns the BigIntLL created
	 * @param sc Scanner reading from the keyboard
	 * @return a BigIntLL built from the user's keyboard input
	 */
	private static BigIntLL readBigInt (Scanner sc) {
		BigIntLL result = new BigIntLL();
		start:
		for (;;) {
			try { 
				System.out.print("Type a string or a number to build a BigInt>> ");
				String s = "";
				if (sc.hasNext()) {
					s = sc.next();
					if (s.toUpperCase().equalsIgnoreCase("STOP")) {
						System.out.println("BYE");
						sc.close();
						break;
					}
					else {
						result = new BigIntLL(s);
						break start;
					}
				}
			}
			catch (NumberFormatException e) {
				System.out.println("You entered a non-numeric symbol. Remember to enter only positive numbers. Try again! \n");
				continue start;
				}
			
		}
		return result;
	}//readBigInt(Scanner sc)
	
	/** Repeatedly lets the user perform operations on the BigIntLL passed as argument
	 * @param sc Scanner reading from the keyboard
	 * @param number BigIntLL the operations are performed on
	 */
	private static void operationsMenu(Scanner sc, BigIntLL number) {
		menuLoop:
		for (;;) {
			System.out.print("\nWhat do you want to do with the BigInt? Type "
					+ "\n>> INC to increment it"
					+ "\n>> DEC to decrement it" 
					+ "\n>> C to compare it with another BigInt "
					+ "\n>> P to raise it to a power "
					+ "\n>> L to find out how many digits it has" 
					+ "\n>> D to divide it"
					+ "\n>> R to get the remainder of a division "
					+ "\n>> A to add "
					+ "\n>> S to subtract >>>  ");
			if (sc.hasNext()) {
				String s = sc.next();
				s = s.toUpperCase();
				switch (s) {
					case "INC" : { System.out.print("\nYou chose to increment the BigInt. " + number + " incremented is: "); 
						number = (BigIntLL) number.incr();
						System.out.println(number); break;}
					case "DEC" : { System.out.print("\nYou chose to decrement the BigInt. ");
						try {number.decr(); } catch (IllegalArgumentException e) {System.out.println("Decrementing would give a negative number! Choose another operation"); continue menuLoop;}  
						System.out.print(number + " decremented is: "); number = (BigIntLL) number.decr(); System.out.println(number); break;}
					case "C" : { System.out.print("\nYou chose to compare " + number + " with another BigInt.\nTo enter the second BigInt: ");
						BigIntLL second = readBigInt(sc);   
						if (number.compareTo(second) > 0) System.out.println(number + " is greater than " + second);
						else if (number.compareTo(second) < 0) System.out.println(number + " is less than " + second);
						else System.out.println("The two BigInts are equal"); break;}
					case "P" : { System.out.print("\nYou chose to raise to a power. Enter the exponent>> ");
						int exp = 1; if (sc.hasNext()) exp = Integer.parseInt(sc.next()); 
						System.out.print("\n" + number + " raised to " + exp + " is: "); number = (BigIntLL) number.pow(exp);
						System.out.println(number); break;}
					case "L" : { System.out.print("\nYou chose to find out how many digits are in " + number + ".\n" + number + " has " + number.length() + " digits"); break; }
					case "D" : { System.out.print("\nYou chose to divide. \nTo enter the second BigInt: "); 
						BigIntLL second = readBigInt(sc);  
						try { number.div(second); } catch (IllegalArgumentException e) { System.out.println("\nThe divisor is greater than the dividend, or one of the terms is zero. Choose another operation"); continue menuLoop;}
						System.out.print("\nThe result of dividing " + number); number = (BigIntLL) number.div(second); System.out.println(" by " + second + " is: " + number); break;}
					case "R" : { System.out.print("\nYou chose to compute the remainder of a division. \nTo enter the second BigInt: "); 
						BigIntLL second = readBigInt(sc);  
						try { number.rem(second); } catch (IllegalArgumentException e) { System.out.println("\nThe divisor is greater than the dividend, or one of the terms is zero. Choose another operation"); continue menuLoop;}
						System.out.print("The remainder of dividing " + number + " by " + second + " is: " + number.rem(second) + 
								"\n" + number + " has become "); number = (BigIntLL) number.div(second); System.out.println(number); break;}
					case "M" : { System.out.print("\nYou chose to multiply. \nTo enter the second BigInt: "); 
						BigIntLL second = readBigInt(sc);  System.out.print("The product of " + number + " and " + second + " is: "); number = (BigIntLL) number.mul(second); 
						System.out.println(number); break;}
					case "A" : { System.out.print("\nYou chose to add. \nTo enter the second BigInt: "); 
						BigIntLL second = readBigInt(sc);  System.out.print("The sum of " + number + " and " + second + " is: "); number = number.add(second); 
						System.out.println(number); break;}
					case "S" : { System.out.print("\nYou chose to subtract. \nTo enter the second BigInt: "); BigIntLL second = readBigInt(sc);
						try {number.sub(second); } catch (IllegalArgumentException e) {System.out.println("\nThe subtraction gives a negative number! Choose another operation"); continue menuLoop; }  
						 System.out.print("\nThe difference between " + number + " and " + second + " is: ");  number = number.sub(second);
						  System.out.println(number); break;} 
						default : System.out.println("\nType a valid command from the list!"); continue menuLoop;
				}//switch
			}//ifHasNext
			System.out.print(	"\n>> To perform another operation type OP " +
								"\n>> To create a new BigInt type N " + 
								"\n>> To quit type STOP >>> ");
			if (sc.hasNext()) {
				String s = sc.next();
				s = s.toUpperCase();
				switch (s) {
					case "N" : { System.out.println(); number = readBigInt(sc); break;}
					case "STOP" : { System.out.println("BYE"); sc.close(); break menuLoop; }
				}
			}
		}
	}//operationsMenu(Scanner sc, BigIntLL number)
	
	public static void main (String[] args) {
		
		Scanner sc = new Scanner(System.in);
		BigIntLL number = readBigInt(sc);
		System.out.println("The number you typed is " + number);
		operationsMenu(sc, number);
	}//main

}//Main
