/**
* Class Name:        	Game
* Class Description: 	this class is used to create Game instance objects, and to run 
* 						The main game-play loop, it returns the boolean (won) at the 
*						conclusion of playing the game.  
* Date of Submission:  	17 Dec 2025
* @author:             	Kristoffer Hendricks 3074048
* Special Instructions:	this class does not run on its own.
* Main Class:          	The Main class is Launcher.java
*/

/*
* This class contains all of the "Game-Level" attributes and the main game play loop
* (the "play()" method. It is constructed as an object for each instance of the game.
* The class contains getters and setters for game-level attributes (such as whether 
* the gamis over (gameOver) or whether the player won or lost (won), and acts as the 
* main hub for the high level game activities.  
*
* Playing the game consists of displaying a room description and printing out a list of 
* characters and items that are present in the current location and prompting for the
* player to enter a command, then sending that raw command to "Control" to be normalized 
* and validated, then to "Action" to execute the command and process all the results.
* If the player moved it returns to the top and displays the room description for the 
* new location, If the player did not move to a new location then it returns to the 
* prompt asking what the player would like to do next. the loop continues until some 
* action results in the gameOver boolean being changed to "true" the game ends and returns
* the value of "won" (true or false) to the Launcher class to display your final results. 
*/

import java.nio.file.Path;
import java.util.Scanner;
import java.util.Map;

public class Game {

	// ------------------------------------------------------------------------
	// -------------------   Game-scope variables   ---------------------------
	// ------------------------------------------------------------------------
	
    private boolean gameOver;	// false = keep playing, true = end the game.
    private boolean won;       	// true = player won, false = player lost
	
	private boolean locationChanged; // toggles on and off when the player moves.
    private int currentLocationId;   // the locationId where the player is.
	
	private int turnCount;		// increments each time player enters a command. 
	private static final int MAX_TURNS = 100; // Game over at 100 turns.
	
	// HashMaps are used to map Id codes to objects.
	private Map<Integer, Location> locationMap; 
	private Map<Integer, Item> itemMap;
	private Map<Integer, Character> characterMap;
		
	// The messageMap maps id labels with misc. game messages.
	// This feature was not implemented for the assignment version of this game
	// but I left it in because I intend to use it in the future as I expand and 
	// adapt this game. The purpose of this feature is to store longer messages
	// from encounters and actions, including alternate location descriptions
	// outside the java code. Which will be more valuable if I expand or change 
	// the game significantly, to keep the code clean.
	private Map<String, String> messageMap;
	
	// maps all action words (synonyms) with valid actions (canonical verbs). 
	private Map<String, String> actionMap;
	
	// The variable that refers to the Control Object
	private Control control;
	
	//Player-related Objects
	private Inventory inventory;
	private Character playerCharacter;
	
	// SCORING
	private int score = 0;
	
	// PLAYER STATE
	// For Wonderland 0=Tiny, 1=Normal sized 2=Giant sized,
	// For other versions of the game each number could mean something
	// different, for example 0=normal 1=hungry 2=tired 3=invisible etc, 
	private int playerState = 1;
	
	private final Path dataDir; //base folder for this game's data
	
	// -----------------------------------------------------------------
	// ------               Game Instance Constructor             ------
	// -----------------------------------------------------------------
	
    public Game(Path dataDir) {
		
		// store base folder
		this.dataDir = dataDir;
		
		// initialize game instance variables.
        this.gameOver = false;
        this.won = false;
       	this.locationChanged = false;
		this.currentLocationId = 1;
		this.turnCount = 0;
				
		// Load the game data from text files.
		this.locationMap 	= DataLoader.loadLocations(dataDir.resolve("locations.txt"));
		this.messageMap 	= DataLoader.loadMessages(dataDir.resolve("messages.txt"));
		this.itemMap		= DataLoader.loadItems(dataDir.resolve("items.txt"));
		this.characterMap	= DataLoader.loadCharacters(dataDir.resolve("characters.txt"));
		this.actionMap		= DataLoader.loadActions(dataDir.resolve("actions.txt"));
					
		// Create a new instance of the Control Object.	
		// Passing a reference to "this" instance of game creates "two-way"
		// communication between the Game instance and the Control instance.
		this.control = new Control(this, actionMap);
		
		// Find the player character in the list of all characters.
		this.playerCharacter = null;
		for (Character c : characterMap.values()) {
			if (c.isPlayer()) {
				this.playerCharacter = c;
				break;
			}
		}//end of for loop

		// Create the player's inventory instance.
		this.inventory = new Inventory(this.playerCharacter.getMaxInventory());
								
    } // end game instance constructor.
	
	
	// ------------------------------------------------------------
	// ------                Setter Methods                  ------
	// ------------------------------------------------------------
	 
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public void setLocationChanged(boolean locationChanged) {
		this.locationChanged = locationChanged;
	}

	public void setCurrentLocationId(int currentLocationId) {
		this.currentLocationId = currentLocationId;
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
	}
		
	
	// -------------------------------------------------------------
	// -------                Getter Methods                  ------
	// -------------------------------------------------------------
	
	public Inventory getInventory() {
    return inventory;
	}

	public Map<Integer, Item> getItemMap() {
		return itemMap;
	}

	public Map<Integer, Character> getCharacterMap() {
		return characterMap;
	}

	public Character getPlayerCharacter() {
		return playerCharacter;
	}

	public int getCurrentLocationId() {
		return currentLocationId;
	}
	
	public int getTurnCount() {
		return turnCount;
	}

	public boolean isWon() {
		return won;
	}
	
	public int getPlayerState() {
		return playerState;
	}

	
	// -------- getCurrentLocation() HELPER METHOD ---------------
	// This helper method returns the Location object that corresponds to the
	// currentlocationId (an integer). It looks up the Location in the HashMap using the current
	// currentlocationId as the key, and returns the associated Location object. This allows
	// the game to always work with the correct location data (descriptions, exits, etc.)
	// without hard-coding any locations into the game logic.
	public Location getCurrentLocation() {
		return locationMap.get(currentLocationId);
	}

	// ---- Get Current Location by location Id.----
	public Location getLocationById(int id) {
		return locationMap.get(id);
	}

	// ----- SCORING HELPERS -----

	public int getScore() {
		return score;
	}

	public void addScore(int points) {
		score += points;
	}



// --------------------------------------------------------------------------
// ------               THE MAIN "PLAY THE GAME" METHOD.               ------
// --------------------------------------------------------------------------

    public boolean play() {

        Scanner scanner = new Scanner(System.in);
		
		gameLoop:
        while (!gameOver) { 
			
			// Once this next assignment is made the variable currentLocation can be 
			// used as if it was the actual location Object that the getCurrentLocation()
			// method returns, and can access all the attributes and methods of that 
			// location object.This immensely simplifies the code, because you can be
			// any where at any time, but you are always in the currentlocation.
			Location currentLocation = getCurrentLocation();

			
			if (currentLocation.isFirstTimeHere()) {
				System.out.println("\n" + currentLocation.getFirstDescription());
				currentLocation.setFirstTimeHere(false);
			}
			else {
				System.out.println("\n" + currentLocation.getShortDescription());
			}

			// Entering certain rooms triggers the end of the game right after the 
			// room description is displayed. Game over is usually triggered in this
			// way, including reaching the maximum turncount will send you to the 
			// final location and end the game.
			if (currentLocation.isEndGameOnEnter()) {
				gameOver = true;
				break gameLoop;
			}
			
			locationChanged = false;
			
			// ---- CHARACTERS IN ROOM MESSAGE ----
			// For each character in the game, check if they're in the current location.
			// If they are, and they are not the player, print a message.
			System.out.println("");
			for (Character c : characterMap.values()) {

				// Skip the player character
				if (c.isPlayer()) {
					continue;
				}

				if (c.getCurrentLocationId() == currentLocationId) {
					System.out.println("You see " + c.getName() + " here.");
				}
			}

			
			// ---- ITEMS IN ROOM MESSAGE ----
			// For each item in the game, check if it's in the current location.
			// If it is, print a message. Items in the player's inventory will
			// not show up because they are considered to be at loaction -1.
			System.out.println("");
			for (Item item : itemMap.values()) {
				if (item.getCurrentLocationId() == currentLocationId) {
					System.out.println("There is a " + item.getItemName() + " here.");
				}
			}
			
				
			// --------------------------------------------------------------
			// ------          USER INPUT/COMMAND NESTED LOOP          ------ 	
			// --------------------------------------------------------------
			inputLoop:
			while (!locationChanged && !gameOver){
			
				System.out.println("\nWhat do you want to do? ");
				System.out.println("(Type 'help' for commands.)");
			
				// -------- Wait for the player to enter a Command. -----------
				String input = scanner.nextLine().trim().toLowerCase();
				
				// Every time the player enters a command it counts as a turn.
				turnCount++;
				
				// Upon Reaching the Maximum number of turns player will be sent to a
				// a location that is set as endGameOnEnter = true. The room behaviour
				// will determine the printed description of the encounter and 
				if (turnCount >= MAX_TURNS) {
					System.out.println("\nThe Time Is Up!");
					won = false;
					setCurrentLocationId(7);
					setLocationChanged(true);
					break;
				}				
				
				// ------ CONTROL COMMAND HANDLER -----
				// Hands the user input over to Control to handle,
				// Control normalizes and validates the user's commands,
				// and sends them to Action to be executed.
				control.handleCommand(input);
				
				
				// Re-fetch currentLocation after actions; if the action resulted in 
				// the player moving to a new location it goes to the top of the 
				// gameLoop, if not it goes back up to the input loop.
				if (locationChanged && !gameOver) {
					currentLocation = getCurrentLocation();
				}
					
			} // end of inputLoop ((while (!locationChanged && !gameOver)))
				
        } // end of gameLoop ((while (!gameOver)))

        return won;
		
    } // End of play() method.
	
} // End of Game class.

