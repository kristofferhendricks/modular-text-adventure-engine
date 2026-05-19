/**
* Program Name:			Launcher
* Program Description:	This program takes a command line argument to select which text
*						based game you want to play, then it starts a new instance of that 
*						game and runs until the game finishes, and asks if you want to play again.	 
* Date of Submission: 	17 DEC 2025
* Date of last update:	26 Jan 2026
* @author:				Kristoffer Hendricks
* Special instructions: takes command line arguments to indicate which game you want to play
*						Usage: java Launcher <GameDataFolderName>
*						Example: java Launcher Alice
*						(will default to the Alice game if no argument is entered) 
* Main Class:			Yes. 
*/

/**
* DOCUMENTATION...
*/

/**
* This program launches and runs a text-based adventure game, in the style of 
* Colossal Cave Adventure, the main program is a game engine that can run a
* different games based on which set of text data files is selected when the 
* game is launched.
* 
* The first game I created is loosely based on characters and locations from the
* book "Alice in Wonderland" by Lewis Carrol. Players play the game as Alice 
* and are able to explore 8 different locations as they progress through a 
* story. The object of the game is to help the White Rabbit find and prepare 
* his four ceremonial items (his Royal Uniform, his Trumpet, his Parchment
* Scroll and his Kid Gloves) and then arrive at the royal courts of justice
* for his duties as herald before the time runs out (there is a turn counter
* and the time runs out at 100 turns, at which time the game promptly ends and 
* you are executed, if you have not already completed the goals of the game). 
*
* The game was designed using object-oriented principles with components such
* as locations, items, characters, inventory, and the instance of the game 
* itself constructed as objects when the game is played. The game was designed
* to be modular with game data loading from external data files so that the 
* story could be expanded, adapted or completely changed (For example to be 
* about the Wizard of Oz or Peter Pan in Neverland or anything you can think
* of) simply by changing the data text files and the Action Class, (and making 
* minimal changes to variable values in the Game class).
*
* When the data loader class runs it reads the data files creating as many objects
* as are listed in the rows of data and stores references to them in hashMaps so that
* there is effectively no limit to the number of locations or items a game could have.
* (The size of a hashMap is limited in practice by the the amount of memory allocated 
* to the JVM, and theoretically limited by the maximum value of the int type.)   
* 
* The game's files consist of:
* (This) Launcher.java
* Game.java
* DataLoader.java
* Control.java 
* Action.java
* Inventory.java
* Location.java
* Item.java
* Character.java
* 
* And the Data Files for each game (kept in separate folders) consist of:
* locations.txt
* items.txt
* characters.txt
* actions.txt
* messages.txt
*/

/**
* FUTURE CHANGES...
*/

/**
* Create a config file called "settings.txt" and move different settings out of the 
* java code (in launcher and Game) into this text file.(or create new variables...)
* such as:
* 	MAX_TURNS = 100			// definitely needs to be different for each game. 
* 	CurrentLocationId = 1 	// i.e. starting location  
* 	playerState = 1			// i.e. starting state
* 	WINLOSE = boolean 		// Does the game even have a win or lose condition, or is it just a score?
*	Thresholds for speed ranks
*	names of speed ranks 
*		(or change speed ranks to a rank that takes both speed and score into account)
*	
*
* Create logic so items can be hidden and do not show up in the "ITEMS IN ROOM MESSAGE"
*
* Create "accomplishments" if you have been to a certain room or talked to a certain 
* character it gives you a token in the background as a way of tracking game progress
* these tokens are stored in a type of accomplishments inventory and having certain
* accomplishments will effect future events. (for example, if you have talked to a 
* certain character another character will extend the bridge for you letting you cross.)
*
* Move messages from the code to the data files, even messages like "you won!" should be
* come from the messages.txt file so they are customized and not generic.
*
*/   

/**
* CODE...
*/

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner; 

public class Launcher { 

	public static void main(String[] args) {	 

		// Read which game to load from the command line
        // Usage: java Launcher Alice
        // Default to "Alice" if no command line argument is provided
        String gameName = (args.length >= 1 && !args[0].isBlank()) ? args[0].trim() : "Alice";

        // Build the folder path: ./<gameName>
        // Example: TextBasedGame> java Launcher Alice  -> dataDir = TextBasedGame/Alice
        Path dataDir = Paths.get("games", gameName);

        if (!Files.isDirectory(dataDir)) {
            System.out.println("Could not find game data folder: " + dataDir.toAbsolutePath());
			System.out.println("Are you running this from inside the TextBasedGame folder?");
            System.out.println("Usage: java Launcher <GameFolderName>");
            System.out.println("Example: java Launcher Alice");
            return;
        }

		boolean playAgain = true; 
		Scanner scanner = new Scanner(System.in);
		
		while (playAgain) {
			
			//Create a new instance of the game, passing the data directory
			Game game = new Game(dataDir);
			
			//Run the game until it ends, returns a boolean, true if you won.
			boolean won = (game.play()); // contains the main game play loop.
			
			// After play ends:
			System.out.println("\n\n          ****************************************");
            System.out.println("          ***            GAME  OVER            ***");
            System.out.println("          ****************************************");
			
			int turns = game.getTurnCount();
			int score = game.getScore();
			System.out.println("\nYou took " + turns + " turns to play.");
			System.out.println("Your final score is " + score + " out of 425.");

			
			if (turns <= 40){
				System.out.println("\nYour Speed Rank = Speedmaster!");
			} 
			else if (turns <= 60){
				System.out.println("\nYour Speed Rank = Pretty Quick!");
			} 
			else if (turns <= 80){
				System.out.println("\nYour Speed Rank = Average Pace!");
			} 
			else {
				System.out.println("\nYour Speed Rank = Slow Poke!");
			}
				
			
			if (won) {
				System.out.println("\nYOU WON!");
            } else {
                System.out.println("\nYou Lost!");
			}
						
			// Ask if the player wants to play again.
			boolean validResponse = false;
			while (!validResponse) {
			
				System.out.print("\nPlay again? (Y/N): ");
				String response = scanner.nextLine().trim().toLowerCase();
			
				if (response.equals("y") || response.equals("yes")) {
					playAgain = true;
					validResponse = true;
				} 
				else if (response.equals("n") || response.equals("no")) {
					playAgain = false;
					validResponse = true;
				} 
				else {
					System.out.println("Please enter Y or N.");
				} 
			
			} // End of While (!validResponse)
				
		} // End of While (playAgain)
			
		System.out.println("\nThanks for playing! Goodbye.");
	
	} // end of Main()
	
}  // end of class Launcher	