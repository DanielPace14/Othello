package Othello;
import java.util.Scanner;

public class Game {
	
	public void startGame() {
		System.out.print("1. Load Game\n2. Start a New Game\n"
				+ "3. Quit\n\nPlease enter an option: ");
		
		Scanner input = new Scanner(System.in);
		int option = input.nextInt();
		if (option == 1) {
			System.out.println("Enter file name:");
			String fileName = input.next()+".txt";
			Board board = new Board(fileName);
			board.play();
		}
		else if (option == 2) {
			System.out.println("Enter first names of players and a "
					+ "number from 1 to 4 (separated by spaces)");
			String name1 = input.next();
			String name2 = input.next();
			int start = input.nextInt();
			Board board = new Board(new Player(name1, Position.BLACK),
						  new Player(name2, Position.WHITE),start);
			board.play();
		}
		else if (option == 3) {
			System.out.print("Game has been quit.");
			
		}
		else {
			System.out.print("Invalid option. Try again");
		}
		input.close();
	}
	
}
