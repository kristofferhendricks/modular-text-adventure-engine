/**
* Class Name:         	Item
* Class Description:  	This class is used to create Item objects for the 
*                     	WonderlandGame.
* Date of Submission: 	17 Dec 2025
* @author:            	Kristoffer Hendricks 3074048
* Special Instructions:	This class is used by other classes in the program
						it does not run on its own.
* Main Class:         	The Main class is Launcher.java.
*/

/*
* This class is used to create item objects for the Text-Based Adventure Game.
*/

public class Item {

    // ----- Attributes of an Item Object -----
    private int itemId;
    private String itemName;         // ALL CAPS
    private int locationAtStart;     // Location Id where it is when game starts.
    private int currentLocationId;   // Where it is now locationId or -1 for inventory.
    private int pointValue;          // How many points it is worth
    private boolean isInInventory;   // Do you have it on you?
	
	// The specialUse attribute was part of the initial design but is not 
	// implemented in the assignment version. Using items is handled through 
	// the Action class method handleUse() instead.
    private String specialUse;       // i.e. "eat", "drink" etc. 
	private boolean prepared;		 // e.g. ironed uniform, polished trumpet etc.
	
	private boolean scoreAwarded = false; // used to ensure you only get points for taking an item once.

    // ----- ITEM CONSTRUCTOR -----
    public Item(int itemId, String itemName, int locationAtStart,
                int pointValue, boolean isInInventory, String specialUse) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.locationAtStart = locationAtStart;
        this.pointValue = pointValue;
        this.isInInventory = isInInventory;              
        this.specialUse = specialUse;
		this.currentLocationId = locationAtStart;
		
		
	} // end of item constructor.
		
    // ----- GETTER METHODS -----
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getLocationAtStart() {
        return locationAtStart;
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public int getPointValue() {
        return pointValue;
    }

    public boolean isInInventory() {
        return isInInventory;
    }

	// This is used as part of the victory conditions for the game.
	public boolean isPrepared() {
		return prepared;
	}

	public boolean isScoreAwarded() {
		return scoreAwarded;
	}

    // ----- SETTER METHODS -----
	
	// Used when the item is picked up or dropped by the player.
	// can be triggered by actions (not implemented)
    public void setCurrentLocationId(int currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public void setInInventory(boolean inInventory) {
        this.isInInventory = inInventory;
    }
    
	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}
	
	public void setScoreAwarded(boolean scoreAwarded) {
		this.scoreAwarded = scoreAwarded;
	}
	
} // end of class Item
