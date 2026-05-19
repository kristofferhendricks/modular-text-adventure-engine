/**
* Class Name:        	Action
* Class Description: 	this class takes pre-validated user commands and 
*						executes them, including executing all of the results of
* 						those actions. This class is the main engine that drives 
* 						the story of the game.  
* Date of Submission:  	17 Dec 2025
* @author:             	Kristoffer Hendricks 3074048
* Special Instructions:	This class does not run on its own.
* Main Class:          	The main class is the launcher.java
*/

/*
* This class consists of one "execute()" method and 20 "Handle<verb>()" methods.
* The execute() method is a big switch that switches on the normalized, validated
* "canonical verb" that was derived from the players cammand by Control class.
* execute is essantially the dispatcher for all the different possible inputs the 
* player can enter. And each of the "handle<verb>()" methods are specialized to 
* one particular verb, some of the handle methods are secondary and are called by 
* other handle methods usually depending on the ObjectText that was entered by the
* player in the command. For example the command "Use Iron" will first "be dispatched" 
* to the "handleUse()" method because it starts with "use", handleUse() then checks 
* the second part of the command, the ObjectText, sees that "IRON" is one of the 
* special items that is used to prepare one of the quest items and "re-dispatches" 
* the command to the handlePrepareItem() method to be executed.
*  
*/

import java.util.Scanner;
import java.util.Map;

public class Action {

	// --------- execute() method is the "Action dispatch Switch" ------------
	// Control trims and normalizes user input commands and splits multi-word 
	// commands into two parts the first word is the "verb" and the rest of the 
	// command collectively is the "objectText". This switch is just switches on 
	// the verb, dispatching the command to one of the "handle<verb>()" methods.
	// It passes the second part of the command, the objectText, to the handle 
	// method, when there is objectText, and many of the handle methods also 
	// involve swithces until the command is executed properly. For example the 
	// command Give Uniform to Rabbit will go through three layers of switches, 
	// once to handle that it is a give command, second to validate "what" is 
	// being given and the third to vaidate "whom" it is being given to.
    public void execute(String verb, String objectText, Game game) {

        switch (verb) {
            case "north":
            case "south":
            case "east":
            case "west":
			    handleGo(verb, game);
                break;

			// ObjectText after "Go" should be "north, south, east or west"
			// So the command "Go" and the cardinal direction commands can 
			// be executed by the same handleGo() method, just with a 
			// different variable as the first argument.
			// ("go north" or just "north")
			case "go":
				handleGo(objectText, game);
				break;
						
            case "take":
                handleTake(objectText, game);
                break;
				
			case "talk":
				handleTalk(objectText, game);
				break;
						
            case "look":
                handleLook(objectText, game);
                break;
					
            case "use":
				handleUse(objectText, game);
				break;
						
			// drop and throw are handled mostly the same, however they 
			// are not treated as synonyms on the list so that the wording
			// on the screen will acknowledge whether you dropped or threw
			// an item, even though the end result is that it is lying on 
			// the ground at the location where it was dropped or thrown.
			case "drop":
			case "throw":
				handleDrop(verb, objectText, game);
				break;
						
			case "quit":
				handleQuit(game);
				break;
							
			case "help":
				handleHelp();
				break;

			case "inventory":
                handleInventory(game);
                break;
						
			case "give":
				handleGive(objectText, game);
				break;
					
            case "open":
                handleOpen(objectText, game);
                break;
			
			case "eat":
                handleEat(objectText, game);
                break;

            case "drink":
                handleDrink(objectText, game);
                break;
			
            case "sleep":
                handleSleep(game);
                break;

			case "score":
				handleScore(game);
				break;
									
			case "turns":
				handleTurns(game);
				break;
			
			case "xyzzy":
				handleXyzzy(game);
				break;

			// This message will only come up when an action word was 
			// added in the .txt data file but was not added to the 
			// switch, as a way to catch errors. The word "polish" as
			// in "polish the trumpet" was added to the list specifically 
			// to test this (in order to polish the trumpet as it is a 
			// player needs to enter "use polish" while they are holding 
			// both the polish and the trumpet.
            default:
                System.out.println("That action is not implemented yet.");
        }
    }

	// -----------------------------------------------------------------------
	// ------ GAME MOVEMENT - PLAYER MOVES FROM ONE LOCATION TO ANOTHER ------
	// -----------------------------------------------------------------------
	
	// from the two cases in the switch above that redirect to this method, 
	// if the player had entered just "north" this "verb" (the first word 
	// of the command) is passed to this method as the direction, If the player 
	// had entered "go north" then the word "north", is passed as objectText,
	// so that by the time it gets to this method, through either route, just 
	// a direction* is passed as valid to this method. ("go Home" is also recognized
	// as a special case. Other special cases, such as "go to the forest" could be 
	// implemented with not too much effort.
    private void handleGo(String direction, Game game) {
		Location currentLocation = game.getCurrentLocation();
		
		int moveTo;
		boolean locked;
							
		switch (direction) {
			
			// n, s, e, w, are listed here because they could be the objectText
			// after the word Go, or 'g' - 
			// Moving will work on "n", "north", "go North", "go n", or "g n".
			case "n":
			case "north": {
				moveTo = currentLocation.getNorthExitTo();
				locked = currentLocation.isNorthExitIsLocked();
				break;
			}

			case "s":
			case "south": {
				moveTo = currentLocation.getSouthExitTo();
				locked = currentLocation.isSouthExitIsLocked();
				break;
			}
			
			case "e":
			case "east": {
				moveTo = currentLocation.getEastExitTo();
				locked = currentLocation.isEastExitIsLocked();
				break;
			}	

			case "w":
			case "west": {
				moveTo = currentLocation.getWestExitTo();
				locked = currentLocation.isWestExitIsLocked();
				break;
			}
			
			// Special Move Case:
			// if you are still at the start you can "go home" and effectively
			// quit the game. But once you have entered Wonderland, by moving to 
			// location #2 and beyond, you can't just "go home" any more.
			case "home":{
				if ( currentLocation.getLocationID() == 1){
					System.out.println("\nYou walk to your House.");
					game.setCurrentLocationId(0);
					game.setLocationChanged(true);
				} else {
					System.out.println("\nYou can't get home from here.");
				}
				return;
			}
					
			default: // should only happen if player enters "go" without a direction.
				System.out.println("\nPlease state the direction when you want to go somewhere.");
				return;
		}
		
		// No exit in that direction (ExitTo for that direction = -1)
		if (moveTo == -1) {
			System.out.println("You can't go that direction.");
			return;
		}
		
		// Exit exists but is currently locked
		if (locked) {
			System.out.println("The door is locked.");
			return;
		}
		
		// If there is a room defined in the direction you want to go
		// and if the door to get there is not locked, you will move.
		game.setCurrentLocationId(moveTo);
		game.setLocationChanged(true);
		
	}

	// -----------------------------------------------------------------
	// ------ LOOK AROUND ANY LOCATION - SEE WHAT ITEMS ARE THERE ------
	// -----------------------------------------------------------------
	
    private void handleLook(String objectText, Game game) {  
		Location currentLocation = game.getCurrentLocation();
		int currentLocationId = game.getCurrentLocationId();
		
		System.out.println("\n" + currentLocation.getLookDescription());
	
		// --- ITEMS IN ROOM MESSAGES (LOOK) ---
		for (Item item : game.getItemMap().values()) {
			
			if (item.getCurrentLocationId() == currentLocationId) {
				System.out.println("There is a " + item.getItemName() + " here.");
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// ------ TAKE ANY ITEM AT YOUR CURRENTLOCATION, PUT IT IN INVENTORY ------
	// ------------------------------------------------------------------------
	
    private void handleTake(String objectText, Game game) {
		
		Location currentLocation = game.getCurrentLocation();
		int currentLocationId = game.getCurrentLocationId();
		Inventory inventory = game.getInventory();
		String targetName = objectText.toUpperCase();
		boolean found = false;
		
		if (objectText.isEmpty()) {
			System.out.println("\nTake what?");
			return;
		} 
		else {
			for (Item item : game.getItemMap().values()) {

				if (item.getItemName().equals(targetName) &&
					item.getCurrentLocationId() == currentLocationId) {

					found = true;
					
					// Try to put it in the inventory
					boolean success = inventory.takeItem(item);  

					if (success) {
						
						// Award points only the FIRST time this item is picked up.
						if (!item.isScoreAwarded()) {
							game.addScore(item.getPointValue());
							item.setScoreAwarded(true);
						}
						
						item.setInInventory(true);
						item.setCurrentLocationId(-1);  // now carried by player

						System.out.println("\nYou take the " + objectText + ".");
					} else {
						System.out.println("\nYou can't carry any more.");
					}

					break; 
				}	
			}
			
			if (!found){
				System.out.println("\nYou don't see " + objectText + " here.");
			}
		} 
	}
	 
	// ----------------------------------------------------
	// ------ CHECK WHAT ITEMS ARE IN YOUR INVENTORY ------
	// ----------------------------------------------------

    private void handleInventory(Game game) {
        
		Inventory inventory = game.getInventory();
		if (inventory.isEmpty()) {
			System.out.println("\nYou check your pockets... nothing!");
		} else {
			System.out.println("\nYou are carrying:");
			for (Item item : inventory.getItems()){
				System.out.println(" - " + item.getItemName());
			}
		}
	}

	// ------------------------------------------------------
	// ------           QUITTING THE GAME              ------
	// ------------------------------------------------------
	
	private void handleQuit(Game game) {
		
		Scanner scanner = new Scanner(System.in);
						
		System.out.println("\nQuit Game - Are you sure? Y/N");
		
		String quitConfirmation = scanner.nextLine().trim().toLowerCase();
		switch (quitConfirmation) {
			case "y":
			case "yes":
				game.setWon(false);
				game.setGameOver(true);
				break;
				
			default :
				break;
		}		
	}
   
	// ---------------------------------------------------------
	// ------           DISPLAY THE "HELP" TEXT           ------
	// ---------------------------------------------------------
   
	private void handleHelp() {
		System.out.println("""
							****************************************
							*          ALICE IN WONDERLAND         *
							*                 HELP                 *
							*                                      *
							* Try commands like:                   *
							* - Look                               *
							* - go east                            *
							* - Take key                           *
							* - use key                            *
							* - drop book                          *
							* - use polish                         *
							* - give gloves to rabbit              *
							* - inventory                          *
							* - score                              *
							* - turns                              *
							* - quit                               *
							*                                      *
							* Some commands work with shortcuts:   *
							* - n = north                          *
							* - i = inventory                      *
							* - q = quit.                          *
							* - etc.                               *
							****************************************
							""");
	}
	
	// ----------------------------------------------------------------------------------
	// ------ DROP/THROW AN ITEM - REMOVE IT FROM INVENTORY - LEAVE IT IN THE ROOM ------
	// ----------------------------------------------------------------------------------
	
	private void handleDrop(String verb, String objectText, Game game) {

		String targetName = objectText.toUpperCase();

		int currentLocationId = game.getCurrentLocationId();
		Inventory inventory = game.getInventory();
		
		// If the player entered "drop" or "throw" without any argument it will
		// Change the case of the verb (drop or throw) so the first letter is 
		// capitalized to make it grammatically correct when it is printed out:
		// "Drop what?" or "Throw what?" instead of "drop what?" or "throw what?"
		String verbCapped = verb.substring(0, 1).toUpperCase() + verb.substring(1);
		boolean found = false;
		if (objectText.isEmpty()){
			System.out.println("\n" + verbCapped + " what?");
			return;
		} else {
		
			// Look for the item in the player's inventory
			for (Item item : game.getItemMap().values()) {

				if (item.getItemName().equals(targetName) &&
					item.isInInventory()) {
						
					found = true;

					// attempt to remove item from inventory, returns boolean.
					boolean success = inventory.dropItem(item);

					if (success) {
						item.setInInventory(false);
						item.setCurrentLocationId(currentLocationId);  // now in this room

						System.out.println("\nYou " + verb + " the " + objectText + ".");
					} else {
						
						System.out.println("\nYou can't " + verb + " that.");
					}
					break;
				}
			}

			if (!found) {
				System.out.println("\nYou aren't carrying " + objectText + ".");
			}
		}
	} 
	   
	// ---------------------------------------------------------------------------		
	// ------           USE AN ITEM, USUALLY TO "PREPARE" ANOTHER ITEM      ------
	// ---------------------------------------------------------------------------
	
	private void handleUse(String objectText, Game game) {

		String targetName = objectText.toUpperCase();

		// get the current Location and location Id
		Location currentLocation = game.getCurrentLocation();
		int currentLocationId = game.getCurrentLocationId();

		// get reference to Inventory
		Inventory inventory = game.getInventory();

		if (objectText.isEmpty()) {
					System.out.println("\nUse what?");
		} 
		else {

			// find the item in the player's inventory
			Item usedItem = null;
			for (Item item : game.getItemMap().values()) {
				if (item.getItemName().equals(targetName) &&
					item.isInInventory()) {

					usedItem = item;
					break;
				}
			}

			if (usedItem == null) {
				System.out.println("\nYou aren't carrying " + objectText + ".");
				return;
			}

			// Now decide what happens based on which item it is
			switch (usedItem.getItemName()) {

				case "KEY":
										
					// This key Unlocks the door between location 2 and location 3, so 
					// we need a reference to each of the rooms independent of where the player is.	
					Location hallOfLockedDoors = game.getLocationById(2);
					Location theWoods = game.getLocationById(3);
					
					// ------ If player is in room 2 or 3 and the DOOR IS LOCKED - UNLOCK IT ------
					
					// If player is in location 2 AND that side of the door is locked OR
					// if the player is in location 3 AND THAT side of the door is locked..					
					if ((currentLocationId == 2 && currentLocation.isEastExitIsLocked()) ||
						(currentLocationId == 3 && currentLocation.isWestExitIsLocked())) {
						
						// THEN UNLOCK BOTH SIDES OF THE DOOR.						
						hallOfLockedDoors.setEastExitIsLocked(false);
						theWoods.setWestExitIsLocked(false);
						
						// IF they are in the HallOf Locked doors - tell them they unlocked East.
						if (currentLocationId == 2) {
							System.out.println("\nYou UNLOCK the EAST door with the KEY.");
						}
						else { // if they were in the woods - tell them they unlocked West.
							System.out.println("\nYou UNLOCK the WEST door with the KEY.");
						}
					}
					
					// ------ If player is in room 2 or 3 and the door is UNLOCKED ------
					else if ((currentLocationId == 2 && !currentLocation.isEastExitIsLocked()) ||
							(currentLocationId == 3 && !currentLocation.isWestExitIsLocked())) {
						
						// THEN LOCK BOTH SIDES OF THE DOOR.						
						hallOfLockedDoors.setEastExitIsLocked(true);
						theWoods.setWestExitIsLocked(true);
						
						// IF they are in the HallOf Locked doors - tell them they locked East.
						if (currentLocationId == 2) {
							System.out.println("\nYou LOCK the EAST door with the KEY.");
						}
						else { // if they were in the woods - tell them they locked West.
							System.out.println("\nYou LOCK the WEST door with the KEY.");
						}
					
					// ------ If the player is in any other Location - nothing happens. 
					} else {
						System.out.println("\nYou jingle the key, but nothing happens here.");
					}
					break;

				// The perpareItem() method is called if the item used is one of the three listed.
				// this was written this way so the list can easily be expanded if I want more 
				// items that need preparation.
				case "IRON":
					handlePrepareItem("UNIFORM",
									  "You carefully press the uniform. It looks much better now.", game);
					break;

				case "POLISH":
					handlePrepareItem("TRUMPET",
									  "You polish the trumpet until it shines.", game);
					break;

				case "WAX":
					handlePrepareItem("SCROLL",
									  "You seal the scroll with a proper blob of wax.",
									  game );
					break;
				
				// if the player attempts to use any other item, it displays that they can't.
				default:
					System.out.println("\nYou can't think of a way to use that right now.");
					break;
			}
		}	
	}

	// ---------------------------------------------------------------------------
	// ------ PREPARE AN ITEM - SECONDARY ACTION - USE -> CALLS PREPARE     ------
	// ---------------------------------------------------------------------------
	
	// Using the IRON   prepares The UNIFORM
	// Using the POLISH prepares the TRUMPET
	// Using the WAX    prepares the SCROLL
	
	private void handlePrepareItem(String targetItemName, String successMessage, Game game) {

		Inventory inventory = game.getInventory();
		
		// initialize to null so we can tell if we didn't find the target item
		// in the players inventory.
		Item target = null;
		
		// Find the item object by its name from the itemMap, and check if its in the 
		// the player's inventory.
		for (Item item : game.getItemMap().values()) {
			if (item.getItemName().equals(targetItemName) &&
				item.isInInventory()) {

				target = item;
				break;
			}
		}

		if (target == null) {
			System.out.println("\nYou don't have anything that needs that right now.");
			return;
		}

		if (target.isPrepared()) {
			System.out.println("\nIt's already been taken care of.");
		} else {
			target.setPrepared(true);
			System.out.println("\n" + successMessage);
		}
	}

	// --------------------------------------------------------------- 
	// ------                Give items to Rabbit               ------
	// ---------------------------------------------------------------
	
	// The player can only give items to rabbit (no one else).
	// Rabbit will only accept HIS items from you, and he will only accept
	// them once they have been prepared. (He will accept the gloves without
	// any special preparation). 
	
	private void handleGive(String objectText, Game game) {

		if (objectText.isEmpty()) {
			System.out.println("\nGive what to whom?");
		} 
		else {

			// objectText is like: "key to rabbit"
			String lower = objectText.toLowerCase();

			// Look for the " to " part
			int toIndex = lower.indexOf(" to ");
			if (toIndex == -1) {
				System.out.println("\nTry 'give <item> to rabbit'.");
				return;
			}

			// Split into "<item>" and "<target>"
			String itemPart = objectText.substring(0, toIndex).trim();       // e.g. "key"
			String targetPart = objectText.substring(toIndex + 4).trim();    // e.g. "rabbit"

			String targetUpper = targetPart.toUpperCase();
			if (!targetUpper.equals("RABBIT")) {
				System.out.println("\nRight now, you can only give items to the Rabbit.");
				return;
			}

			// Rabbit is always in location 4 until he has his items.
			int currentLocationId = game.getCurrentLocationId();
			if (currentLocationId != 4) {
				System.out.println("\nThe Rabbit isn't here.");
				return;
			}

			// Normalize item name to match Items.txt (ALL CAPS: GLOVES, TRUMPET, SCROLL, UNIFORM)
			String itemNameUpper = itemPart.toUpperCase();

			Inventory inventory = game.getInventory();

			// Find the item in the player's inventory
			Item givenItem = null;
			for (Item item : game.getItemMap().values()) {
				if (item.getItemName().equals(itemNameUpper) && item.isInInventory()) {
					givenItem = item;
					break;
				}
			}

			if (givenItem == null) {
				System.out.println("\nYou aren't carrying " + itemPart + ".");
				return;
			}

			// Allow only Rabbit's items
			String name = givenItem.getItemName(); 

			boolean isRabbitItem =
					name.equals("GLOVES") ||
					name.equals("TRUMPET") ||
					name.equals("SCROLL") ||
					name.equals("UNIFORM");

			if (!isRabbitItem) {
				System.out.println("\nThe Rabbit doesn't want that.");
				return;
			}

			// For TRUMPET, SCROLL, UNIFORM, they must be prepared first.
			boolean needsToBePrepared =
					name.equals("TRUMPET") ||
					name.equals("SCROLL") ||
					name.equals("UNIFORM");

			if (needsToBePrepared && !givenItem.isPrepared()) {
				System.out.println("\nThe Rabbit sniffs and says, \"This isn't ready yet.\"");
				return;
			}

			// At this point, the Rabbit will accept the item.
			// Remove it from the player's inventory and move it "out of the world".
			boolean removed = inventory.dropItem(givenItem);  // just removes from the list
			if (removed) {
				givenItem.setInInventory(false);
				
				// Technically moving it to location "0" moves it to Alice's "Home" but 
				// since Alice can never go there once entering the rabbit hole it is 
				// effectively moved "out of the world" so it can't be found again.
				givenItem.setCurrentLocationId(0);  // 0 = with Rabbit.

				System.out.println("\nYou give the " + itemPart + " to the Rabbit.");
				System.out.println("He looks very pleased.");
				
				// Check if this completes the quest
				checkQuestProgress(game);
				
			} else {
				System.out.println("\nSomething went wrong. You can't seem to give that.");
			}
		}	
	}
	
	// -----------------------------------------------------------
	// ------      SLEEP - JUST DISPLAYS A MESSAGE          ------
	// -----------------------------------------------------------
	
	private void handleSleep(Game game) {
		System.out.println("\nYou lie down and try to sleep, but you can't sleep here.");
	}

	// -----------------------------------------------------------------
	// ------                  OPEN - ITEM                        ------ 
	// -----------------------------------------------------------------
		// the only thing that can be opened is the box.
		// but this method is ready to add logic to open other things.
		
	private void handleOpen(String objectText, Game game) {

		if (objectText.isEmpty()) {
			System.out.println("\nOpen what?");
			return;
		}

		int currentLocationId = game.getCurrentLocationId();
		String targetName = objectText.toUpperCase();

		switch (targetName) {

			case "BOX":
				handleOpenGloveBox(currentLocationId, game);
				break;

			default:
				System.out.println("\nYou can't open the " + objectText + ".");
				break;
		}
	}

	// ---------------------------------------------------------------
	// ------           OPEN THE BOX - FIND THE GLOVE           ------
	// ---------------------------------------------------------------
	
	private void handleOpenGloveBox(int currentLocationId, Game game) {

		Map<Integer, Item> itemMap = game.getItemMap();

		// Find the gloves
		Item gloves = null;
		for (Item item : itemMap.values()) {
			if (item.getItemName().equals("GLOVES")) {
				gloves = item;
				break;
			}
		}

		if (gloves == null) {
			System.out.println("\nYou open the box, but it seems to be empty.");
			return;
		}
		
		if (gloves.getCurrentLocationId() == 0 && !gloves.isInInventory()) {
			gloves.setCurrentLocationId(currentLocationId);
			gloves.setInInventory(false);

			System.out.println("\nYou open the box and a pair of gloves falls out.");
			System.out.println("They are now lying on the ground.");
		} else {
			System.out.println("\nThe box is empty. You must have already taken the gloves out.");
		}
	}
	
	// ----------------------------------------------------------
	// ------ TURNS - TELLS HOW MANY TURNS YOU HAVE TAKEN  ------         
	// ----------------------------------------------------------
	
	private void handleTurns(Game game) {

		int turns = game.getTurnCount();
		int maxTurns = 100;

		System.out.println("\nYou have played " + turns + " turn(s) out of " 
						   + maxTurns + " maximum turns before the Rabbit is late.");
	}

	// -----------------------------------------------------------
	// ------ EAT the CAKE or MUSHROOM SIDES  GROW/SHRINK   ------
	// -----------------------------------------------------------
	
	private void handleEat(String objectText, Game game) {

		if (objectText.isEmpty()) {
			System.out.println("\nEat what?");
			return;
		}

		String targetName = objectText.toUpperCase();

		// Verify item is in inventory
		Item itemToEat = null;
		for (Item item : game.getItemMap().values()) {
			if (item.getItemName().equals(targetName) && item.isInInventory()) {
				itemToEat = item;
				break;
			}
		}

		if (itemToEat == null) {
			System.out.println("\nYou aren't carrying " + objectText + ".");
			return;
		}

		int beforeSize = game.getPlayerState();

		switch (targetName) {

			// ---- GROW ITEMS ----
			// PlayerState 0 = Tiny, 1 = Normal Sized, 2 = Giant.
			case "CAKE":
				System.out.println("\nYou take a little nibble from the cake.");
				switch (beforeSize) {
					case 0:
						game.setPlayerState(1);
						System.out.println("\nYou grow back to your normal size.");
						break;
					case 1:
						game.setPlayerState(2);
						System.out.println("\nYou grow to the size of a house.");
						break;
					case 2:
						game.setPlayerState(2);
						System.out.println("\nYou stay the same giant size.");
						break;	
				}
				break;

			case "MUSHRIGHT":
				System.out.println("\nYou take a little nibble from the right side of the mushroom.");
				switch (beforeSize) {
					case 0:
						game.setPlayerState(1);
						System.out.println("\nYou grow back to your normal size.");
						break;
					case 1:
						game.setPlayerState(2);
						System.out.println("\nYou grow to the size of a house.");
						break;
					case 2:
						game.setPlayerState(2);
						System.out.println("\nYou stay the same giant size.");
						break;	
				}
				break;

			// ---- SHRINK ITEM ----
			case "MUSHLEFT":
				System.out.println("\nYou take a little nibble from the left side of the mushroom.");
				switch (beforeSize) {
					case 0:
						game.setPlayerState(0);
						System.out.println("\nYou stay the same tiny size.");
						break;
					case 1:
						game.setPlayerState(0);
						System.out.println("\nYou shrink down to the size of a Mouse.");
						break;
					case 2:
						game.setPlayerState(1);
						System.out.println("\nYou shrink back to your normal size.");
						break;	
				}
				break;

			// ---- THE QUEEN'S TARTS ----
			case "TARTS":
				System.out.println("\nYou eat one of the tarts.");
				
				double encounterRoll = Math.random();
				
				if (encounterRoll < 0.20) { // 20% chance of encounter with Queen.
					System.out.println("A shadow falls over you... The Queen of Hearts is standing behind you!");
					
					// 25% chance of execution, 75% chance of warning.
					double queensMood = Math.random();
					
					if (queensMood < 0.25) { // 25% of the 20% (overall 5% death chance per tart)
						System.out.println("\n\"YOU DARE TO EAT MY TARTS?\" she bellows.");
						System.out.println("She points at you and screams, \"OFF WITH HER HEAD!\"");
						System.out.println("The guards seize you before you can even protest...");
						
						game.setWon(false);
						game.setGameOver(true);
					}
					else {
						System.out.println("\nThe Queen glares at you.");
						System.out.println("\"Touch my tarts again,\" she growls,"
										+ " \"and it WILL be off with your head.\"");
						System.out.println("With a final murderous look, she storms away.");
					}
				}
				else {
					System.out.println("For now, nothing else seems to happen.");
				}
				break;

			default:
				System.out.println("\nYou can't eat the " + objectText + ".");
				break;
		}
	}

	// ------------------------------------------------------------------------
	// ------               DRINK FROM THE BOTTLE to SHRINK              ------
	// ------------------------------------------------------------------------

    private void handleDrink(String objectText, Game game) {

		if (objectText.isEmpty()) {
			System.out.println("\nDrink what?");
			return;
		}

		String targetName = objectText.toUpperCase();

		Item itemToDrink = null;
		for (Item item : game.getItemMap().values()) {
			if (item.getItemName().equals(targetName) && item.isInInventory()) {
				itemToDrink = item;
				break;
			}
		}

		if (itemToDrink == null) {
			System.out.println("\nYou aren't carrying " + objectText + ".");
			return;
		}

		int beforeSize = game.getPlayerState();

		switch (targetName) {

			case "BOTTLE":
				System.out.println("\nYou take a little sip from the bottle...");
				switch (beforeSize) {
					case 0:
						game.setPlayerState(0);
						System.out.println("\nYou stay the same tiny size.");
						break;
					case 1:
						game.setPlayerState(0);
						System.out.println("\nYou shrink down to the size of a Mouse.");
						break;
					case 2:
						game.setPlayerState(1);
						System.out.println("\nYou shrink back to your normal size.");
						break;	
				}
				break;
			default:
				System.out.println("\nYou can't drink the " + objectText + ".");
				break;
		}
	}

	
	// -------------------------------------------------------------------
	// ------                   CHECK YOUR SCORE                    ------
	// -------------------------------------------------------------------
	
	private void handleScore(Game game) {

		int currentScore = game.getScore();
	
		System.out.println("\nYour current score is " + currentScore + ".");
	}
	
	// --------------------------------------------------------------------
	// ------                         XYZZY                          ------
	// --------------------------------------------------------------------
	// Added this to pay homage to Colossal Cave Adventure.
	
	private void handleXyzzy(Game game) {
       System.out.println("\nYou Say the Magic Word, but nothing happens.");
	} 
    	
	// ---------------------------------------------------------------------
	// ------                   CHECK QUEST PROGRESS                  ------
	// ---------------------------------------------------------------------
	
	public void checkQuestProgress(Game game) {

		if (game.isWon()) {
			return; // already done, don't check again.
		}

		Map<Integer, Item> items = game.getItemMap();  

		boolean hasGloves  = false;
		boolean hasTrumpet = false;
		boolean hasScroll  = false;
		boolean hasUniform = false;

		//Goes through all items looking for items that are in Rabbit's possesion.
		for (Item item : items.values()) {
			if (!item.isInInventory() && item.getCurrentLocationId() == 0) {
				String name = item.getItemName();

				if (name.equals("GLOVES"))   hasGloves = true;
				if (name.equals("TRUMPET"))  hasTrumpet = true;
				if (name.equals("SCROLL"))   hasScroll = true;
				if (name.equals("UNIFORM"))  hasUniform = true;
			}
		}
		// If Rabbit Has his 4 items - PLayer Wins
		if (hasGloves && hasTrumpet && hasScroll && hasUniform) {
			
			game.setWon(true);
						
			System.out.println("The White Rabbit Says:\n\n"
							+ "      'Thank-you! I shall be on time after all!\n"
							+ "       Follow me to the Royal court of Justice.'\n\n"
							+ " And he runs away towards the Royal Courts of Justice."); 

			// Move Rabbit to room 7
			for (Character c : game.getCharacterMap().values()) {
				if (c.getName().equals("RABBIT")) {
					c.setCurrentLocationId(7);
					break;
				}
			}

			// Update room 7's first-time description to happy ending text
			Location court = game.getLocationById(7);
			if (court != null) {
				court.setFirstDescription(
					"You step into the Queen's court just as the White Rabbit arrives,\n"
				  + "perfectly dressed and right on time.\n\n"
				  + "He raises his polished trumpet, blows a clear, bright fanfare,\n"
				  + "and announces your name in a grand voice.\n\n"
				  + "The Queen of Hearts smiles warmly and thanks you for your service to Wonderland.\n"
				  + "For a moment, everything feels absolutely right.\n\n"
				  + "Then the scene fades. You blink and realize you've been daydreaming.\n"
				  + "You are back where you started, but somehow, you feel braver and lighter.\n\n"
				);

				// If the player *might* have visited room 7 before,
				// reset firstTimeHere so this new text will show next time.
				court.setFirstTimeHere(true);
			}
					
		}
		
	}//  END OF CHECK QUEST Progress	
	
	// --------------------------------------------------------------- 
	// ------                 Talk to characters                ------
	// ---------------------------------------------------------------

	private void handleTalk(String objectText, Game game) {

		// No target specified - i.e. player only said "talk"
		if (objectText == null || objectText.trim().isEmpty()) {
			System.out.println("\nTalk to whom?");
			return;
		}

		// Normalize the input
		// e.g. "TO RABBIT" or "TO THE WHITE RABBIT"
		String upper = objectText.toUpperCase().trim();      
		String talkTarget = upper;

		// If it starts with "TO ", strip that off
		// e.g. "RABBIT", "THE WHITE RABBIT"
		if (upper.startsWith("TO ")) {
			talkTarget = upper.substring(3).trim();           
		}

		// check again if no target.
		// (i.e. if the player only said "talk to"
		if (talkTarget.isEmpty()) {
			System.out.println("\nTalk to whom?");
			return;
		}

		// get the player's current location.
		int playerLocationId = game.getCurrentLocationId();

		// ------ Find a character whose NAME appears in what the player typed ------
		// (Names in the data are short: RABBIT, CAT, HARE, QUEEN, etc.)
		Character matchedCharacter = null;

		for (Character c : game.getCharacterMap().values()) {
			
			// Make sure we are comparing uppercase strings
			String charName = c.getName().toUpperCase();      // e.g. "RABBIT"
			
			// Does the player's phrase contain that word? 
			// e.g. if the player typed "THE WHITE RABBIT" it contains "RABBIT"
			if (talkTarget.contains(charName)) {
				matchedCharacter = c;
				break;
			}
		}

		// No such character in the game.
		if (matchedCharacter == null) {
			System.out.println("\nYou don't see anyone called \""
									+ talkTarget + "\" to talk to.");
			return;
		}

		// ------ Check if that character is in the same location ------
		if (matchedCharacter.getCurrentLocationId() != playerLocationId) {
			// Character exists in the world, but not here right now
			System.out.println("\n" + matchedCharacter.getName() + " is not here right now.");
			return;
		}

		// ------ there is a valid target in the same room ------
		String targetName = matchedCharacter.getName().toUpperCase();   // e.g. "RABBIT", "QUEEN"

		// The four "advisor" characters will randomly say one of these sayings
		// when they are spoken to.
		String[] ADVISOR_SAYINGS = {
			"There's nothing like a freshly polished trumpet!",
			"A properly ironed uniform opens doors in the court.",
			"Important scrolls should always be sealed with wax.",
			"The White Rabbit keeps his gloves in a fancy little glove box.",
			"Always butter your watch before breakfast. It keeps time slippery.",
			"If you meet yourself coming back, be sure to say hello politely.",
			"Tea tastes better if you add exactly eleven questions to the conversation.",
			"The best path is the one that wasn't there a moment ago.",
			"Never trust a clock that isn't late.",
			"Sometimes the floor is actually the ceiling on holiday."
		};
		
		int index = (int) (Math.random() * ADVISOR_SAYINGS.length);
		String saying = ADVISOR_SAYINGS[index];

		// Execute the conversation.
		switch (targetName) {
			case "RABBIT":
				System.out.println("\nThe White Rabbit Says: \n\n\"Mary-Ann!" 
								+ " Did you find any more of my items?\""
								+ " \n\nthen he rushes off to another room to keep searching"
								+ " through his stuff."); 
				break;
				
			case "QUEEN":
				System.out.println("\nThe Queen of Hearts looks down her nose at you and says:"
								+ "\n\n\"Now run along little girl, children should be seen and not heard.\""
								+ "\n\nThen she turns her back on you and walks away.");
				matchedCharacter.setCurrentLocationId(7);
				break;
				
			case "KNAVE":
				System.out.println("\nThe Knave of Hearts says:"
								+ "\n\n\"You should try some of the Tarts"
								+ "\n     ...they are simply to Die for!\"");    
				break;	
				
			case "CAT":
				System.out.println("\nYou ask the Cheshire Cat" 
								+ " if they have any advice for you, and they say:\n"
								+ "\"" + saying + "\"");
				break;

			case "HARE":
				System.out.println("\nYou ask the March Hare"  
								+ " if they have any advice for you, and they say:\n"
								+ "\"" + saying + "\"");
				break;
			
			case "CATERPILLAR":
				System.out.println("\nYou ask the Caterpillar" 
								+ " if they have any advice for you, and they say:\n"
								+ "\"" + saying + "\"");
				break;
				
			case "HATTER":
				System.out.println("\nYou ask the Mad Hatter" 
								+ " if they have any advice for you, and they say:\n"
								+ "\"" + saying + "\"");
				break;
						
			default:
				// Fallback just in case.
				System.out.println("\nThey don't seem to have anything to say right now.");
				break;
		}
	} // End of HandleTalk
	
}// End of Action Class.
