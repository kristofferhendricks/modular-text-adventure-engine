/**
* Class Name:         	Character
* Class Description:  	This class is used to create Character objects for the 
*                     	WonderlandGame. It stores identity, location, inventory
*                     	limits, and special role information.
* Date of Submission: 	17 Dec 2025
* @author:            	Kristoffer Hendricks 3074048
* Special Instructions:	This class does not run on its own.
* Main Class:         	The Main class is Launcher.java
*/

/*
* This class is used to create character objects for the Text-based adventure Game.
*/

public class Character {

    // -------- Attributes of a Character Object --------
    private int characterId;
    private String name;
    private int startingLocationId;
    private int currentLocationId;
	
	// isPlayer and maxInventory are used to determine the number of items 
	// that can be held in the inventory, this is part of the game's modular
	// design so that the maximum inventory size can be changed by changing 
	// data in the text files - no changes to java code required.
    private boolean isPlayer;
	private int maxInventory;
	
	//the Roletag, messageKey and specialTag attributes are part of the 
	// original game design and are not implemented in the assignment 
	// version of the program. I left them in because I intend to continue 
	// developing this game in the future for my own use and practice.
	// implemented in the final version, instead encounters are determined 
	// in the "handleTalk()" method in the action class.
    private String roleTag;      // advisor, questGiver, thief, etc.
    private String messageKey;  // key for Messages.txt
    private String specialTag;  // off_with_head, rabbit_quest, etc.

    // -------- CHARACTER CONSTRUCTOR --------
    public Character(int characterId, String name, int startingLocationId,
                     boolean isPlayer, int maxInventory,
                     String roleTag, String messageKey, String specialTag) {

        this.characterId = characterId;
        this.name = name;
        this.startingLocationId = startingLocationId;
        this.currentLocationId = startingLocationId;
        this.isPlayer = isPlayer;
        this.maxInventory = maxInventory;
        this.roleTag = roleTag;
        this.messageKey = messageKey;
        this.specialTag = specialTag;
    }

    // -------- GETTER METHODS --------

    public int getCharacterId() {
        return characterId;
    }

    public String getName() {
        return name;
    }

    public int getStartingLocationId() {
        return startingLocationId;
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public int getMaxInventory() {
        return maxInventory;
    }

    public String getRoleTag() {
        return roleTag;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getSpecialTag() {
        return specialTag;
    }

    // -------- SETTER METHODS --------

	//Used when characters move to different locations on the map.
    public void setCurrentLocationId(int currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public void setRoleTag(String roleTag) {
        this.roleTag = roleTag;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public void setSpecialTag(String specialTag) {
        this.specialTag = specialTag;
    }
    
} // End of Class Character
