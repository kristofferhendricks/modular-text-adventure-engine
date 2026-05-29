# TEXT BASED GAME REFACTOR NOTES

--- Location Class Changes ---
-- Create Exit Objects/Exit Class

current Location class has  functions for 
- northExitTo
- northExitLocked
- northExitKeyItem
etc. (south, ...)

Future:
Location
 - exits: List<Exit>

Exit
 - direction
 - targetLocationId
 - locked
 - keyItem
 
 
 -------------------------------------------------------------
 new Launcher concept:
 
 Player runs Launcher
→ Launcher reads games.csv
→ Launcher displays available games, 
   - 1. Alice in Wonderland
   - 2. Lost Station
   - 3. Black Rain Detective
   - Q. Quit
   - R. refresh game list
   - H. help
→ Player chooses a game
→ Launcher validates the choice
   - Does the selected folder exist?
   - Does it contain the required files?
   - Can the game config be loaded?
→ Launcher finds that game’s data folder
→ Launcher creates Game using that selected folder/config
→ Game takes over


Games.csv SUGGESTION/EXAMPLE
id,title,folder,description,default
alice,Alice in Wonderland,Alice,Help the White Rabbit reach court,true
station,Lost Station,LostStation,A sci-fi survival mystery,false
noir,Black Rain Detective,BlackRain,Solve a murder before sunrise,false