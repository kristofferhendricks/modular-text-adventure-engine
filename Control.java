/**
* Class Name:        	Control
* Class Description: 	This class takes user commands (passed from Game class),
*						and trims leading and trailing whitespace, converts the
*						command to lowercase, separates it into two parts (a "verb"
*						and an "objectText", checks the verb part to see if it 
*						appears on the list of recognized verbs, then sends it to the
*						actionHandler (Action class) to be executed. 
* Date of Submission:  	17 Dec 2025
* @author:             	Kristoffer Hendricks 3074048
* Special Instructions:	This class is called by the Game class and does not run 
*						on its own.
* Main Class:          	The main class is Launcher.java
*/

import java.util.Map;

public class Control {

	// Each instance of Control has a reference to the current instance of Game.
	private Game game;
	private Map<String, String> actionMap;
	private Action actionHandler;
	
	// ----------------- CONTROL CONSTRUCTOR ----------------------------
	public Control(Game game, Map<String, String> actionMap) {
		this.game = game; // a reference to the CURRENT game instance.
		this.actionMap = actionMap;
		this.actionHandler = new Action();
	}

	// ----------------- HANDLE COMMANDS ---------------------------------
	public void handleCommand(String input) {
		
		// if no command was entered, return.
		if (input == null){
			System.out.println("Please type an action.");
			return;
		}

		// normalize input into lowercase, trimmed commands.
		// if only spaces were entered, return 
		String command = input.trim().toLowerCase();
		if (command.isEmpty()){
			System.out.println("Please type an action.");
			return;
		}
		
		// split the normalized command string into substrings
		// first word of the command, the verb
		// and the rest of the command, the "objectText"
		// "Object" here is in the grammatical sense, 
		// i.e. splits "Take key" into "take" and "key".
		String verb;
		String objectText = ""; // gives compiler error if not initialized
		
		int firstSpaceAt = command.indexOf(' ');
		
		//if the command contains no spaces,
		if (firstSpaceAt == -1) {
			// treat as single word command like "look" or "north"
			// "gonorth" will be treated as one word and not recognized.
			verb = command;
		} else {
			
			// first word of the command, i.e. "take"
			verb = command.substring(0,firstSpaceAt);
			
			// everything after the first word, spaces trimmed off i.e. "key"
			// ObjectText (in the grammatical sense, direct or indirect object).
			objectText = command.substring(firstSpaceAt +1).trim();
		}
		
		// Find the verb on the key side of the HashMap of actions, and return
		// the corresponding value, i.e. verb "synonym" returns "canonical verb"
		String canonicalVerb = actionMap.get(verb);
		
		// If the entered verb wasn't on the recognized list let player know.
		if (canonicalVerb == null){
			System.out.println("I don't understand \"" + verb + "\".");
			return;
		}
		
		// Now that the input has been normalized and validated it can be sent
		// to the actionHandler (action class) to be executed. 
		actionHandler.execute(canonicalVerb, objectText, game);
		
		
	} // end of Handle Commands.
					
} // End of Control class.

