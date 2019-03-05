package pa1;


import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

/*
 *	I have done bonus part, thank you very much for marking. 
 *  I have modified main method, you can start a new game(enter 0) or 
 *  	load a saved game(enter 1) according to information printing on screen
 */
public class GameEngine 
{
	private final ArrayList<Player> players = new ArrayList<>();
	private final GameMap gameMap = new GameMap(players);
	private static final Scanner userInputScanner = new Scanner(System.in);
	//
	//		bonus part
	//		sample output 
	//
	/*	2
		Red 100 2 3
		0 SG Town 300 50 500
		1 NY City 500 100 600
		A 80 100 60
		B 100 80 60
		C 60 80 100
		Blu 100 2 2
		2 TPE Town 200 0 100
		3 HK Metropolis 500 0 800
		D 80 80 80
		E 100 100 100
	 */
	private void writePlayerData() throws IOException{
		Scanner sc1; 
		System.out.println("Input your file name: ");
		System.out.println("PlayersData.txt is used to load new game, "
				+ "you are not allowed to input PlayersData.txt as filename");
		sc1 = new Scanner(System.in);
		String inputfilename = sc1.next();
		while(inputfilename.equals("PlayersData.txt")){
			System.out.println("~~~!!! name conflict with exsiting file !!!~~~"); 
			System.out.println("Input your file name: ");
			System.out.println("PlayersData.txt is used to load new game, "
					+ "you are not allowed to input PlayersData.txt as filename");
			inputfilename = sc1.next();
		}
		
		File inputFile = new File(inputfilename);
		try (
			PrintWriter wr = new PrintWriter(inputFile);
		){
			// sample output: 2
			wr.println(players.size());
			// sample output: Red 100 2 3
			for (Player player: this.players) {
				wr.print(player.getName()+"\t");
				wr.print(player.getGold()+"\t");
				wr.print(player.getTownList().size()+"\t");
				wr.println(player.getGeneralList().size());
				// sample output: 0 SG Town 300 50 500
				for (Town town: player.getTownList()) {
					wr.print(town.getId()+"\t");
					wr.print(town.getName()+"\t");
						
					if (town instanceof Metropolis) {
						wr.print("Metropolis"+"\t");
					}
					else if (town instanceof City) { 
						wr.print("City"+"\t");
					}
					else {
						wr.print("Town"+"\t");
					}
					
					wr.print(town.population+"\t");
					wr.print(town.armySize+"\t");
					wr.println(town.cropYield);
				}
				// sample output: A 80 100 60
				for (General general: player.getGeneralList()) {
					wr.print(general.getName()+"\t");
					wr.print(general.getCombatPoint()+"\t");
					wr.print(general.getLeadershipPoint()+"\t");
					wr.println(general.getWisdomPoint()+"\t");
				}
			}
			//wr.close();
		}
		//sc1.nextLine();	
		//sc1.close();
		System.out.println("THE GAME HAS BEEN WELL SAVED!");
	}
	//
	//
	//		end of bonus part
	//
	//
	
	// Load the Players, Generals and Towns based on specified input textFile.
	private void loadPlayersData(String filename) throws IOException
	{
		try (Scanner scanner = new Scanner(new File(filename)))
		{
			
			int numPlayers = scanner.nextInt();
			
			for (int n = 0; n < numPlayers; n++)
			{
				String name = scanner.next();
				int gold = scanner.nextInt();
				int numTowns = scanner.nextInt();
				int numGenerals = scanner.nextInt();
				
				
				Player player = new Player(name, gold);
				
				for (int h = 0; h < numTowns; ++h)
				{
					int townId = scanner.nextInt();
					String townName = scanner.next();
					String townType = scanner.next();
					int population = scanner.nextInt();
					int armySize = scanner.nextInt();
					int foodProduction = scanner.nextInt();
					
					Town town;
					switch (townType)
					{
					case "Town":
						town = new Town(townId, townName, population, armySize, foodProduction);
						break;
					case "City":
						town = new City(townId, townName, population, armySize, foodProduction);
						break;
					case "Metropolis":
						town = new Metropolis(townId, townName, population, armySize, foodProduction);
						break;
					default:
						throw new IOException();
					}
					
					player.addTown(town);
				}
				
				for (int c = 0; c < numGenerals; ++c)
				{
					String generalName = scanner.next();
					int population = scanner.nextInt();
					int armySize = scanner.nextInt();
					int foodProduction = scanner.nextInt();
					
					player.addGeneral(new General(generalName, population, armySize, foodProduction));
				}
				
				players.add(player);
			}
		}
	}
	
	
	private void processPlayerTurns(int turns)
	{
		for (Player player: players) {
			player.readyAllGenerals();
			
			while (player.hasReadyGenerals() && player.getTownList().size()>0) {
				System.out.println("\n\n\n_____________________ Turn " + turns + " " + player + "'s turn ____________________");
				System.out.println(gameMap);
				player.displayInfo();

				// Phase 1: Select General
				displayCurrentCommandTree(player, null, null, false);
				General selectedGeneral = selectGeneral(player);
				
				// End turn or input again.
				if (selectedGeneral == null) {
					break;
				}
				if (!selectedGeneral.isReady()) {
					System.out.println("ERROR: " + selectedGeneral + " is not ready!");
					continue;
				}
				
				// Proceed to Phase 2
				selectCommandForTown(player, selectedGeneral);
			}
		}
		System.out.println("\n\n\n\n\n\n\nTurn " + turns + " over");
		System.out.println("-----------");
		
		// Increase population and process any natural disaster after each turn
		for (Town town:gameMap.getAllTownList()) {
			town.processTurn(Math.random());
		}
		
		System.out.println("\nPress the Enter key to return continue.");
		try {
			System.in.read();
		} catch (Exception e) {}
	}
	
	// Phase 2 and 3: Select Town and Command
	private void selectCommandForTown(Player player, General selectedGeneral) {
		// Phase 2: Select Town
		displayCurrentCommandTree(player, selectedGeneral, null, false);
		Town selectedTown = selectTown(player.getTownList(), false, player);
		// Return to Phase 1
		if (selectedTown == null) {
			return;
		}

		// Phase 3: Select Command
		boolean actionFlag = false;
		while(!actionFlag) {
			displayCurrentCommandTree(player, selectedGeneral, selectedTown, false);
			System.out.println("            COMMAND SELECTION");
			System.out.println("                [1] Improve crop yield");
			System.out.println("                [2] Collect tax");
			System.out.println("                [3] Recruit army");
			System.out.println("                [4] Upgrade town (costs 50 gold)");
			System.out.println("                [5] Send troops to nearby town");
			System.out.println("                [6] Save game state");
			System.out.print("Select command[1~6] (0 to return): ");
			
			int command = (userInputScanner.next().charAt(0) - 48);
			// Return to Phase 1
			if (command == 0) {
				return;
			}
			
			actionFlag = processPlayerCommand(command, player, selectedTown, selectedGeneral);
		}
		// Return to Phase 1 after command is successfully carried out
		System.out.println("\nPress the Enter key to return continue.");
		try {
			System.in.read();
		} catch (Exception e) {}
		
	}
	
	// Player selects one general from his list of generals
	private General selectGeneral(Player player) {
		System.out.println("GENERAL SELECTION");
		
		ArrayList<General> generals = player.getGeneralList();
		
		 // No general remaining for player
		if (generals == null || generals.size() == 0 || !player.hasReadyGenerals()) {
			return null;
		}
		
		for (int i=0; i<generals.size(); ++i) {
			System.out.print(String.format("%-8.8s[%-1d] ", "", i+1));
			generals.get(i).displayInfo();
		}
		System.out.println();
		
		while (true)
		{
			System.out.print("Select general to issue command [1 ~ " + generals.size() + "] (0 to END TURN): ");
			String rawInput = (userInputScanner.next());
		    try {
		    	int selection = Integer.parseInt(rawInput);
		    	if (selection == 0)
		    		return null;
		    	
		    	return generals.get(selection - 1);
		    	
		    } catch (NumberFormatException | IndexOutOfBoundsException e) {
			    System.out.println("ERROR: invalid input");
		    }
		}
	}
	
	// Player selects one town from given list of towns.
	private Town selectTown(ArrayList<Town> townList, boolean nearbyTownList, Player player)
	{
		System.out.println((nearbyTownList? "NEIGHBORING ": "") + "TOWN SELECTION");
		
		// no town remaining for player
		if (townList == null || townList.size() == 0) {
			return null;
		}

		for (int i=0; i<townList.size(); ++i) {
			String targetCommandString = (gameMap.getTownOwner(townList.get(i)).equals(player))? "TRANSFER ": "ATTACK   ";
			if (!nearbyTownList) {
				targetCommandString = "";
			}
			
			System.out.print(String.format("%-" + 4+"."+4 + "s[%-1d] " + targetCommandString, "", i + 1));
			townList.get(i).displayInfo(gameMap.getTownOwner(townList.get(i)).getName());
		}
		System.out.println();
		
		while (true)
		{
			System.out.print("Select Town [1 ~ " + townList.size() + "] (0 to return): ");
			String rawInput = (userInputScanner.next());
		    try {
		    	int selection = Integer.parseInt(rawInput);
		    	if (selection == 0)
		    		return null;
		    	
		    	return townList.get(selection - 1);
		    	
		    } catch (NumberFormatException | IndexOutOfBoundsException e) {
		    	System.out.println("ERROR: invalid input");
		    }
		}
	}
	
	private boolean processPlayerCommand(int command, Player player, Town selectedTown, General selectedGeneral) {
		switch (command) {
		case 1:
			return selectedTown.improveCropYield(selectedGeneral);
			
		case 2:
			return selectedTown.collectTax(player, selectedGeneral);
			
		case 3:
			System.out.print("Enter the amount of budget:");
			int budget;
			
			try {
				budget = Integer.parseInt(userInputScanner.next());
		    } catch (NumberFormatException e) {
		    	System.out.println("ERROR: invalid input");
		    	return false;
		    }
			
			return selectedTown.recruitArmy(player, selectedGeneral, budget);
			
		case 4:
			return player.upgradeTown(selectedTown, selectedGeneral);
			
		case 5:
			Town targetTown;
			int troops;
			
			displayCurrentCommandTree(player, selectedGeneral, selectedTown, true);
			targetTown = selectTown(gameMap.getAdjacentTownList(selectedTown), true, player);
			if (targetTown == null) {
				return false;
			}
			
			System.out.print("Enter the number of troops to send ["+0+" ~ "+selectedTown.getArmySize()+"]:");
			try {
				troops = Integer.parseInt(userInputScanner.next());
		    } catch (NumberFormatException e) {
		    	System.out.println("ERROR: invalid input");
		    	return false;
		    }
			
			return processSendTroopsCommand(player, selectedTown, targetTown, selectedGeneral, troops);
		case 6:
			try	{
				this.writePlayerData();
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		default:
			System.out.println("ERROR: Invalid input");
			return false;
		}
	}
	
	// TODO 8: processeSendTroopsCommand()
	//		After player selects one of his general, and town, he further selects one of neighboring towns and number 
	//		of troops (armySize) to work with. (already implemented above)
	//		You are required to implement command 5 (send troops to another town) given all the user selections as parameters.
	//			
	//		If the targetTown is owned by a player himself, he will transfer troops (armySize) to targetTown by using selectedGeneral. 
	//			You can use Town.transferArmy() that you implemented in TODO 2.
	//			
	//		If the targetTown is owned by someone else however, the player will attack targetTown instead. similarly, 
	//		use Town.attackTown() to implement it.
	//		After attacking targetTown, check whether targetTown's armySize has reached 0.
	//		In that case, you need to transfer ownership of the town. Use Player.surrenderTown() in TODO 5.
	//		
	//		Lastly, check if whether the transfer or attack command can be successfully carried out. Print appropriate error 
	//		message and return false if there is any error.
	//		Only City and Metropolis can send troops to another city, and obviously you cannot send troops that exceed 
	//		selectedTown's armySize or negative number of troops.
	//		Of course it does not make sense to send troops to your own town. (which might actually happen depending on how you implemented TODO 7)
	//		Other than the errors described here, you can assume correctness of all other variables.
	//
	//		Recall that for simplicity, all of the troops sent for attack mission is be expended.
	//		For example, Even after you take over targetTown with million troops, the resulting armySize at targetTown will be 0.
	//
	// HINT: use gameMap.getTownOwner() to find owner of the town
	private boolean processSendTroopsCommand(Player player, Town selectedTown, Town targetTown, General selectedGeneral, int troops) {
		// return false if (selectedTown is of type Town || any other 
		// integer value related faults). A Town is NOT allowed to
		// attack. Only City/Metropolis objects can attack.
		// Do the checks and output appropriate message(s), for
		// example:
		// “ERROR: Invalid town type”
		// “ERROR: not enough units”
		// “ERROR: invalid troops size!”
		if(!(selectedTown instanceof City) && !(selectedTown instanceof Metropolis)) {
			System.out.println("ERROR: Invalid town type");
			return false;
		}
		if(troops<=0) {
			System.out.println("ERROR: invalid troops size!");
			return false;
		}
		if(troops>selectedTown.getArmySize()) {
			System.out.println("ERROR: not enough units");
			return false;
		}
		// then
		// 1.either transfer the army to a town/city/metropolis of
		// the same player if this town/city/metropolis (i.e.
		// targetTown) is adjacent to the selectedTown.
		// may need to check whether:
		// owner(selectedTown) === owner(selectedTown),
		// look at Town.transfer() for details
		if(player.getTownList().contains(targetTown)==true) {
			return ((City)selectedTown).transferArmy(targetTown,selectedGeneral,troops);
		}
		else {
			if(((City)selectedTown).attackTown(targetTown,selectedGeneral,troops)==true) {
				if(targetTown.getArmySize()<=0) {
					gameMap.getTownOwner(targetTown).surrenderTown(targetTown, player);
				}
			}
			return true;
		}
		// 2. Attack the targetTown with the supplied general and
		// troops (troops == int indicating army size). Then checks to see if the army at the targetTown has been all killed 
		// (i.e. getArmySize()==0), if this is the case let the owner of the targetTown surrenderTown() to the player. Output
		// appropriate message:
		// “(City NY ) attacked (City TPE) with 10 troops for 8 damage” 
		// “(City TPE) lost 0 troops”
		// “Player Blu surrendered (City TPE) to Player Red!”
		// return true if 1. Can transfer troop, or 2. Can attack
	}
	
	// TODO 9: If only one player has Towns Remaining, then the game is over.
	private boolean isGameOver() 
	{
		// straight forward
		// checks to see how many players have at least one town 
		// remaining under his/her control. Can use the players 
		// ArrayList, for-each statement works for ArrayList:
		// for (Player player: players){}
		for (Player player: players){
			if(player.getTownList().size()==0) {
				return true;
			}
		}
		return false;
	}

	private void displayGameOver() 
	{
		System.out.println("\n\n\nGAME OVER\n");
		
		for (Player player:players) 
		{
			if (0 < player.getTownList().size())
			{
				System.out.println(player.getName() + " WON.\n");
			}
			else
			{
				System.out.println(player.getName() + " LOST.\n");
			}
		}
	}
	
	private void displayCurrentCommandTree(Player player, General selectedGeneral, Town selectedTown, boolean sendTroops) {
		String g = (selectedGeneral!=null)?selectedGeneral.toString():"{}";
		String t = (selectedTown!=null)?selectedTown.toString():"<>";
		String s = sendTroops?" => <-SELECT->":"";
		
		if (selectedGeneral == null) {
			g = "{-SELECT-}";
		} else if (selectedTown == null) {
			t = "<-SELECT->";
		} else if (!sendTroops){
			s = ": -SELECT-";
		}
		
		System.out.println("\n\n\n"+player+", Gold: "+player.getGold()+", "+g+", "+t+s);
		System.out.println("---------------------------------------------------------------------------");
	}
	
	public static void main(String[] args)
	{
		GameEngine game = new GameEngine();
		Scanner sc = new Scanner(System.in);
		// Load the GameMap, Players, and Units, from user-input textFiles.
		try
		{
			game.gameMap.loadGameMap("MapData.txt");
			int mode = -1;
			System.out.println("Start a new game?(enter 0)/Load a saved game?(enter 1)");
			mode = sc.nextInt();
			while(mode!=0 && mode !=1) {
				System.out.println("Start a new game?(enter 0)/Load a saved game?(enter 1)");
				mode = sc.nextInt();
			}
			if(mode==0) {
				game.loadPlayersData("PlayersData.txt");
			}
			else if(mode==1){
				System.out.println("Please enter the file name of saved game");
				sc.nextLine();
				String inputFileName = sc.nextLine();
				game.loadPlayersData(inputFileName);
			}
			else {
				System.out.println("Please enter valid value");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// Run the game.
		int turns = 1;
		while (!game.isGameOver()) 
		{
			game.processPlayerTurns(turns++);
		}
		
		// Game Over.
		game.displayGameOver();
		
		// Close userInput.
		userInputScanner.close();
	}
}
