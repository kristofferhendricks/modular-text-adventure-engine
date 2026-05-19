/**
* Class Name:        	Location
* Class Description: 	this class is used to create Location objects, for the 
*						text-based adventure game.  
* Date of Submission:  	17 Dec 2025
* @author:             	Kristoffer Hendricks 3074048
* Special Instructions:	This class does not run on its own.
* Main Class:          	The main class is the launcher.java
*/

/*
* This class is used to create location objects for the text-based Adventure game.
* the location descriptions are the main drivers of the plot of this text-based game.
* some locations, labelled as endGameOnEnter, trigger the end of the game, other 
* actions can trigger the end of the game, but entering an endGameOnEnter is the 
* primary mechanic used to end the game.
*
* Locations have long descriptions called the "firstDescription" that is displayed
* the first time you enter a room, a short description that is displayed entering
* the second or subsequent times, amd a look description that is displayed when the 
* player enters the "look" command. Different actions and events can change the 
* descriptions that are displayed making it possible to move the plot in different
* directions.
*/

public class Location {
	 
	// ------ GENERAL LOCATION ATTRIBUTES ------
	private int locationID;
	private String locationName;
	private boolean firstTimeHere;
	private String firstDescription;
	private String shortDescription;
	private String lookDescription;
	
	// Special actions was part of the original design but is not implemented
	// in the assignment version of the game. I kept it in for future changes 
	// I intend to make as I continue to develop this game. The intention is
	// for locations to trigger events that occur and can change game states 
	// after you enter a room but before the player enters a command, for 
	// processing room triggered encounters, such as traps or with NPCs.
	private String specialActions; //Events triggered by entering the room.
	
	// EndGame on Enter defines a location that entering will trigger 
	// the end of the game, such as a final boss lair, or safely Home.
	private boolean endGameOnEnter;
						
	// A saferoom is defined as a room where no random encounters occur.
	private boolean isSafeRoom;
	
	// ------ Exit related attributes.------
	
	// the locationId of the location that is North of this location.
	private int northExitTo; 
	
	private boolean northExitIsLocked;
	
	// the itemName of the item required to unlock the way, 
	// could be any item, not necessarily a key.
	private String northExitKeyItemName;
	
	private int southExitTo;
	private boolean southExitIsLocked;
	private String southExitKeyItemName;
	
	private int eastExitTo;
	private boolean eastExitIsLocked;
	private String eastExitKeyItemName;
	
	private int westExitTo;
	private boolean westExitIsLocked;
	private String westExitKeyItemName;
	
	// LOCATION CONSTRUCTOR
	public Location(
        int locationID,
        String locationName,
        boolean firstTimeHere,
        String firstDescription,
        String shortDescription,
        String lookDescription,
        String specialActions,
        boolean endGameOnEnter,
		boolean isSafeRoom,

        int northExitTo,
        boolean northExitIsLocked,
        String northExitKeyItemName,

        int southExitTo,
        boolean southExitIsLocked,
        String southExitKeyItemName,

        int eastExitTo,
        boolean eastExitIsLocked,
        String eastExitKeyItemName,

        int westExitTo,
        boolean westExitIsLocked,
        String westExitKeyItemName
	)
	{
		this.locationID = locationID;
		this.locationName = locationName;
		this.firstTimeHere = firstTimeHere;
		this.firstDescription = firstDescription;
		this.shortDescription = shortDescription;
		this.lookDescription = lookDescription;
		this.specialActions = specialActions;
		this.endGameOnEnter = endGameOnEnter;
		this.isSafeRoom = isSafeRoom;

		this.northExitTo = northExitTo;
		this.northExitIsLocked = northExitIsLocked;
		this.northExitKeyItemName = northExitKeyItemName;

		this.southExitTo = southExitTo;
		this.southExitIsLocked = southExitIsLocked;
		this.southExitKeyItemName = southExitKeyItemName;

		this.eastExitTo = eastExitTo;
		this.eastExitIsLocked = eastExitIsLocked;
		this.eastExitKeyItemName = eastExitKeyItemName;

		this.westExitTo = westExitTo;
		this.westExitIsLocked = westExitIsLocked;
		this.westExitKeyItemName = westExitKeyItemName;
	}

	// Getters and setters for every location attribute. Not all
	// of these getters or setters will be used in every story, but 
	// the game is designed to be modular with changes possible to
	// the story by changing the data files only. The needs of the
	// story will determine which getters and especially which setters 
	// will be used, For example a story with a shifting map or 
	// secret doors may make use of the setters for changing which 
	// room a direction exits to.

	// -------------------------------------------------------
	// ------   GETTER METHODS for Each Attribute.      ------
	// -------------------------------------------------------
	
    public int getLocationID() {
        return locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean isFirstTimeHere() {
        return firstTimeHere;
    }

    public String getFirstDescription() {
        return firstDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLookDescription() {
        return lookDescription;
    }
   
    public String getSpecialActions() {
        return specialActions;
    }
	
	public boolean isEndGameOnEnter() {
		return endGameOnEnter;
	}
	
	public boolean isSafeRoom() {
		return isSafeRoom;
	}
	
	// ------ GETTERS - NORTH EXIT -------
	public int getNorthExitTo() {
		return northExitTo;
	}

	public boolean isNorthExitIsLocked() {
		return northExitIsLocked;
	}

	public String getNorthExitKeyItemName() {
		return northExitKeyItemName;
	}


	// ------ GETTERS - SOUTH EXIT ------
	public int getSouthExitTo() {
		return southExitTo;
	}

	public boolean isSouthExitIsLocked() {
		return southExitIsLocked;
	}

	public String getSouthExitKeyItemName() {
		return southExitKeyItemName;
	}


	// ------ GETTERS - EAST EXIT ------
	public int getEastExitTo() {
		return eastExitTo;
	}

	public boolean isEastExitIsLocked() {
		return eastExitIsLocked;
	}

	public String getEastExitKeyItemName() {
		return eastExitKeyItemName;
	}


	// ------ GETTERS - WEST EXIT ------
	public int getWestExitTo() {
		return westExitTo;
	}

	public boolean isWestExitIsLocked() {
		return westExitIsLocked;
	}

	public String getWestExitKeyItemName() {
		return westExitKeyItemName;
	}
    
	// ------------ SETTER METHODS ------------------------------
	
    // ----- first time Here Setter method -----
	// First time here should generally be set to "true" for all 
	// locations in the data files, except perhaps for rooms that 
	// look exactly like other rooms and are meant to fool the players
	// into getting lost, or thinking they are lost. This field is 
	// automatically changed to false after the 
	// "firstDescription" is printed when the player enters the room 
	// for the first time, after that the player will see the 
	// "shortDescription" each time they enter the location. Setting
	// this to false in the data files will result in the 
	//	first description being skipped and the short decription will
	// be displayed the first time the player enters the room.
    public void setFirstTimeHere(boolean firstTimeHere) {
        this.firstTimeHere = firstTimeHere;
    }

	// ----- Location description Setter Methods -----
    public void setFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLookDescription(String lookDescription) {
        this.lookDescription = lookDescription;
    }
 
	// ------ Special Actions Setter ------
    public void setSpecialActions(String specialActions) {
        this.specialActions = specialActions;
    }
 
 	// ------ END GAME ON ENTER SETTER ------
	// This could be set to true in the data files or it
	// could be set to true (or false) upon some player 
	// action occuring or an event from an encounter, etc.
	public void setEndGameOnEnter(boolean endGameOnEnter) {
		this.endGameOnEnter = endGameOnEnter;
	}
	
	// ------ IS SAFE ROOM SETTER ------
	// This setter could be used if a room becomes safe
	// for example after a certain encounter.
	public void setIsSafeRoom(boolean isSafeRoom){
		this.isSafeRoom = isSafeRoom;
	}
		
	// ------ SETTERS - NORTH EXIT ------
	public void setNorthExitTo(int northExitTo) {
		this.northExitTo = northExitTo;
	}

	public void setNorthExitIsLocked(boolean northExitIsLocked) {
		this.northExitIsLocked = northExitIsLocked;
	}

	public void setNorthExitKeyItemName(String northExitKeyItemName) {
		this.northExitKeyItemName = northExitKeyItemName;
	}


	// ------ SETTERS - SOUTH EXIT ------
	public void setSouthExitTo(int southExitTo) {
		this.southExitTo = southExitTo;
	}

	public void setSouthExitIsLocked(boolean southExitIsLocked) {
		this.southExitIsLocked = southExitIsLocked;
	}

	public void setSouthExitKeyItemName(String southExitKeyItemName) {
		this.southExitKeyItemName = southExitKeyItemName;
	}

	// ------ SETTERS - EAST EXIT ------
	public void setEastExitTo(int eastExitTo) {
		this.eastExitTo = eastExitTo;
	}

	public void setEastExitIsLocked(boolean eastExitIsLocked) {
		this.eastExitIsLocked = eastExitIsLocked;
	}

	public void setEastExitKeyItemName(String eastExitKeyItemName) {
		this.eastExitKeyItemName = eastExitKeyItemName;
	}

	// ------ SETTERS - WEST EXIT ------
	public void setWestExitTo(int westExitTo) {
		this.westExitTo = westExitTo;
	}

	public void setWestExitIsLocked(boolean westExitIsLocked) {
		this.westExitIsLocked = westExitIsLocked;
	}

	public void setWestExitKeyItemName(String westExitKeyItemName) {
		this.westExitKeyItemName = westExitKeyItemName;
	}

    	
} // end of class Location