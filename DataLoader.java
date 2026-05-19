/**
* Class Name:        	DataLoader
* Class Description: 	this class loads all the data from the text files and
*						creates the objects and returns the HashMaps storing 
*						the references to all these objects.
* Date of Submission:  	17 Dec 2025
* @author:             	Kristoffer Hendricks 3074048
* Special Instructions:	This class does not run on its own.
* Main Class:          	The Main class is Launcher.java
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;

public class DataLoader {

	// ------------------------ LOCATION LOADER ------------------------
    public static Map<Integer, Location> loadLocations(Path filePath) {

		Map<Integer, Location> locationMap = new HashMap<Integer, Location>();
		File file = filePath.toFile();
		
		try (Scanner locScanner = new Scanner(file)) {
			
			// read header line and ignore
			if (locScanner.hasNextLine()) {
				locScanner.nextLine();
			}

			// -- LOCATION LOADER LOOP --
			while (locScanner.hasNextLine()) {
				
				// Read the next line.
				String line = locScanner.nextLine();
				
				// split the line of text at pipes, create an "array of parts"
				// The "-1" ensures that empty "cells" are kept and not omitted.
				String[] parts = line.split("\\|", -1);

				// each "part" has specific rules to turn it from text into variables. 
				// .trim() is used in each case to protect from hidden spaces that could
				// be present in the data, due to it being built in a spreadsheet.
				int locationID 				= Integer.parseInt(parts[0].trim());
				String locationName 		= parts[1].trim();
				boolean firstTimeHere 		= Boolean.parseBoolean(parts[2].trim());
				String firstDescription  	= parts[3].replace("\\n", "\n").trim();
				String shortDescription  	= parts[4].replace("\\n", "\n").trim();
				String lookDescription   	= parts[5].replace("\\n", "\n").trim();
				String specialActions 		= parts[6].trim();
				boolean endGameOnEnter 		= Boolean.parseBoolean(parts[7].trim());
				boolean isSafeRoom			= Boolean.parseBoolean(parts[8].trim());
				
				int northExitTo 			= Integer.parseInt(parts[9].trim());
				boolean northExitIsLocked	= Boolean.parseBoolean(parts[10].trim());
				String northExitKeyItemName	= parts[11].trim();
				
				int southExitTo 			= Integer.parseInt(parts[12].trim());
				boolean southExitIsLocked	= Boolean.parseBoolean(parts[13].trim());
				String southExitKeyItemName	= parts[14].trim();
				
				int eastExitTo 				= Integer.parseInt(parts[15].trim());
				boolean eastExitIsLocked	= Boolean.parseBoolean(parts[16].trim());
				String eastExitKeyItemName	= parts[17].trim();
				
				int westExitTo 				= Integer.parseInt(parts[18].trim());
				boolean westExitIsLocked	= Boolean.parseBoolean(parts[19].trim());
				String westExitKeyItemName	= parts[20].trim();
				

				Location loc = new Location(	
					locationID,
					locationName,
					firstTimeHere,
					firstDescription,
					shortDescription,
					lookDescription,
					specialActions,
					endGameOnEnter,
					isSafeRoom,

					northExitTo,
					northExitIsLocked,
					northExitKeyItemName,

					southExitTo,
					southExitIsLocked,
					southExitKeyItemName,

					eastExitTo,
					eastExitIsLocked,
					eastExitKeyItemName,

					westExitTo,
					westExitIsLocked,
					westExitKeyItemName				
				); 

				locationMap.put(locationID, loc);
				
			} // end of LOCATION LOADER While Loop. 
		
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not load locations file: " + filePath.toAbsolutePath());
			System.exit(1);
		}
		
		return locationMap;
		
	} // end of LOCATIONS LOADER method.


	// ------------------ MESSAGE LOADER ------------------
	public static Map<String, String> loadMessages(Path filePath) {

		Map<String, String> messageMap = new HashMap<String, String>();
		File file = filePath.toFile();

		try (Scanner msgScanner = new Scanner(file)) {

			// Skip header
			if (msgScanner.hasNextLine()) {
				msgScanner.nextLine();
			}

			while (msgScanner.hasNextLine()) {

				String line = msgScanner.nextLine();
				String[] parts = line.split("\\|");

				String key = parts[0];
				String message = parts[1].replace("\\n", "\n");

				messageMap.put(key, message);
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not load messages file: " + filePath.toAbsolutePath());
			System.exit(1);
		}

		return messageMap;
		
	} // end of MESSAGE LOADER method.

    	// ------------------ ACTIONS LOADER ------------------
	public static Map<String, String> loadActions(Path filePath) {

		Map<String, String> actionMap = new HashMap<String, String>();
		File file = filePath.toFile();

		try (Scanner actScanner = new Scanner(file)) {

			// Skip header
			if (actScanner.hasNextLine()) {
				actScanner.nextLine();
			}

			while (actScanner.hasNextLine()) {

				String line = actScanner.nextLine().trim(); // trim to get rid of accidental whitespaces
				
				if (line.isEmpty()) {
					continue;
				}
				
				String[] parts = line.split("\\|");

				if (parts.length < 2){
					System.out.println("WARNING: bad action line skipped -> " + line);
					continue;
				}

				String synonym = parts[0].trim().toLowerCase();
				String validAction = parts[1].trim().toLowerCase();

				actionMap.put(synonym, validAction);
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not load actions file: " + filePath.toAbsolutePath());
			System.exit(1);
		}

		return actionMap;
		
	} // end of ACTIONS LOADER method.
    
	// ------------------------ ITEM LOADER ------------------------
	public static Map<Integer, Item> loadItems(Path filePath) {

		Map<Integer, Item> itemMap = new HashMap<>();
		File file = filePath.toFile();

		try (Scanner itemScanner = new Scanner(file)) {

			// Skip header
			if (itemScanner.hasNextLine()) {
				itemScanner.nextLine();
			}

			while (itemScanner.hasNextLine()) {

				String line = itemScanner.nextLine();

				String[] parts = line.split("\\|");

				int itemId = Integer.parseInt(parts[0]);
				String itemName = parts[1].trim();
				int locationAtStart = Integer.parseInt(parts[2]);
				int pointValue = Integer.parseInt(parts[3]);
				boolean isInInventory = Boolean.parseBoolean(parts[4]);
				String specialUse = parts[5].trim();

				Item item = new Item(
						itemId,
						itemName,
						locationAtStart,
						pointValue,
						isInInventory,
						specialUse
				);

				itemMap.put(itemId, item);
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not load items file: " + filePath.toAbsolutePath());
			System.exit(1);
		}

		return itemMap;

	} // end of ITEM LOADER

	
	
	
	
    // public static Map<Integer, Character> loadCharacters(String filename)
	// ------------------------ CHARACTER LOADER ------------------------
	public static Map<Integer, Character> loadCharacters(Path filePath) {

		Map<Integer, Character> characterMap = new HashMap<>();
		File file = filePath.toFile();

		try (Scanner charScanner = new Scanner(file)) {

			// Skip header
			if (charScanner.hasNextLine()) {
				charScanner.nextLine();
			}

			// ---- CHARACTER LOADING LOOP ----
			while (charScanner.hasNextLine()) {

				String line = charScanner.nextLine();

				String[] parts = line.split("\\|");

			
				int characterId = Integer.parseInt(parts[0]);
				String name = parts[1];
				int startingLocationId = Integer.parseInt(parts[2]);
				boolean isPlayer = Boolean.parseBoolean(parts[3]);
				int maxInventory = Integer.parseInt(parts[4]);
				String roleTag = parts[5];
				String messageKey = parts[6];
				String specialTag = parts[7];

				Character character = new Character(
						characterId,
						name,
						startingLocationId,
						isPlayer,
						maxInventory,
						roleTag,
						messageKey,
						specialTag
				);

				characterMap.put(characterId, character);
			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Could not load characters file: " + filePath.toAbsolutePath());
			System.exit(1);
		}

		return characterMap;

	} // end of CHARACTER LOADER method
	
	
} // End of Class DataLoader
