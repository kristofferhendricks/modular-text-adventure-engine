/**
* Class Name:         	Inventory
* Class Description:  	This class is used to create Inventory objects for the
*                     	Text-based adventure Game. It stores a limited number of Item
*                    	references and provides methods to add, remove and list items.
* Date of Submission: 	17 Dec 2025
* @author:            	Kristoffer Hendricks 3074048
* Special Instructions: this class is called by other classes in this program and
*						does not run on its own.
* Main Class:         	The main class is Launcher.java
*/

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Item> items;
    private int maxSize;

    // INVENTORY CONSTRUCTOR
    public Inventory(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<Item>();
    }

    // -------- GETTERS ----------
	
	// the maximum size of the inventory is set in the character data
	// files, each character has an inventory size and one of the 
	// characters is identified as the player character, that 
	// character's max inventory is passed to this object when it is
	// constructed.
    public int getMaxSize() {
        return maxSize;
    }

	// the current size of the inventory is the number of items the 
	// player is carrying.
    public int getCurrentSize() {
        return items.size();
    }

	// player is carrying the maximum number of items they can.
    public boolean isFull() {
        return (items.size() >= maxSize);
    }
	
	// the player is not carrying anything.
	public boolean isEmpty() {
		return items.isEmpty();
	}

	// Used when the player uses the "inventory" command to check 
	// waht they are currently carrying.
    public List<Item> getItems() {
        return items;
    }

    // -------- CORE METHODS ----------
    public boolean takeItem(Item item) {
        if (isFull()) {
            return false;
        }
        
		items.add(item);
		return true;
        
    }

    public boolean dropItem(Item item) {
        return items.remove(item);
    }

	// used when the player tries to enter a command that is 
	// dependent on a certain item being in their inventory
	// such as "use key" - the player must be holding the key.
    public boolean contains(Item item) {
        return items.contains(item);
    }

} // end of class Inventory
