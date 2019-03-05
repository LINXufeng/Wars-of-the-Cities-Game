package pa1;




import java.util.ArrayList;

public class Player {
	private final String name;
	private ArrayList<General> generals;
	private ArrayList<Town> towns;
	
	private int gold;
	
	public Player(String name, int gold)
	{
		this.name = name;
		generals = new ArrayList<General>();
		towns = new ArrayList<Town>();
		this.gold = gold;
	}
	
	public String getName() 
	{
		return name; 
	}
	
	public ArrayList<General> getGeneralList()
	{
		return generals;
	}
	
	public ArrayList<Town> getTownList()
	{
		return towns;
	}

	public int getGold() {
		return gold;
	}

	public void earnGold(int amount) {
		gold += amount;
	}

	
	public void addGeneral(General c)
	{
		generals.add(c);
	}
	
	public void addTown(Town t)
	{
		towns.add(t);
	}

	// TODO 5.1: spendGold()
	//		check if player has enough amount of gold to spend, and decrement gold by the provided value.
	//
	// return false if amount<=0 or not enough gold.
	// simple subtraction to update gold of the Player object, // no output required
	public boolean spendGold(int amount) {
		if(amount<=0||amount>gold) {
			return false;
		}
		else{
			gold-=amount;
			return true;
		}
	}
	
	// TODO 5.2: upgradeTown()
	//			use copy constructor to create a new instance of town of higher tier, and replace the current town.
	//			You can safely assume that the town can always be found from the player's town list,
	//			but you still have to check whether:
	//				- if player can spend 50 gold to upgrade town,
	//				- if town can be upgraded any further. (Metropolis cannot be upgraded)
	//
	public boolean upgradeTown(Town town, General general) {
		// return false if (not enough gold || metropolis)
		// outpt an appropriate message, for example:
		// ERROR: not enough gold
		// OR
		// ERROR: already at max tier
		if(town instanceof Metropolis) {
			System.out.println("ERROR: already at max tier");
			return false;
		}
		if(getGold()<50) {
			System.out.println("ERROR: not enough gold");
			return false;
		}
		// otherwise subtract 50 from the gold variable of the player object, then upgrade the town by construct an upgraded
		
		// town (i.e town->city, city->metropolis). 
		// Remove the current town, and add newly constructed upgraded town
		// object to the towns ArrayList (using remove() and add()
		if(town instanceof City && getTownList().contains(town)) {
			earnGold(-50);
			String oldTownInformation = town.toString(); 
			Metropolis newMetropolis = new Metropolis(town);
			getTownList().remove(town);
			getTownList().add(newMetropolis);
			// output an appropriate message after the upgrade, for // example: “(City HK ) is upgraded to [Metr HK ]!”
			System.out.println(oldTownInformation+" is upgraded to "+newMetropolis.toString()+"!");
			general.endTurn();
			return true;
		}
		else if(town instanceof Town && getTownList().contains(town)) {
			earnGold(-50);
			String oldTownInformation = town.toString(); 
			City newCity = new City(town);
			getTownList().remove(town);
			getTownList().add(newCity);
			// output an appropriate message after the upgrade, for // example: “(City HK ) is upgraded to [Metr HK ]!”
			System.out.println(oldTownInformation+" is upgraded to "+newCity.toString()+"!");
			general.endTurn();
			return true;
		}
		else {
			return false;
		}
	}
	
	// TODO 5.3: surrenderTown()
	//			transfer ownership of the town to opponent. You can safely assume that the town can always 
	//			be found from the player's town list.
	public void surrenderTown(Town town, Player opponent) {
		// remove the town from the towns() ArrayList of the current
		// player object. Add town to the towns() ArrayList of the
		// opponent object using addTown(town) method.
		this.getTownList().remove(town);
		opponent.addTown(town);
		// Output an appropriate message, for example:
		// “Player Blu surrendered <Town TPE> to Player Red!”
		System.out.println(this.toString()+" surrendered "+ town.toString()+" to "+opponent.toString()+"!");
	}
	
	// TODO 5.4: hasReadyGenerals()
	//		check if player has any general in ready state.
	//
	// check to see if there is still a general that is in the “READY” state for tasks
	public boolean hasReadyGenerals() {
		for(int i = 0;i<this.getGeneralList().size();++i) {
			if(this.getGeneralList().get(i).isReady()==true) {
				return true;
			}
		}
		return false;
	}
	

	//=======================================
	//			End of your code
	//=======================================

	//at the beginning of the turn set all generals to be ready
	public void readyAllGenerals() {
		for (General c:generals) {
			c.beginTurn();
		}
	}
	
	
	public String toString() {
		return "Player " + getName();
	}
	
	public void displayInfo() {
		int readyGenerals = 0;
		int barLength = 50 + Math.max((Math.max(generals.size() - 2, 0) * 14), Math.max(towns.size()-2, 0) * 11);
		
		for (General g: generals) {
			if (g.isReady()) {
				++readyGenerals;
			}
		}
		
		System.out.println("__________________________");
		System.out.println(String.format("%-25.25s |",this));
		System.out.println(new String(new char[barLength]).replace("\0","_"));
		
		System.out.println(String.format(" Gold: %-8d | %-1d/%-1d ready Generals | %-1d towns", gold, readyGenerals, generals.size(), towns.size()));
		System.out.println(new String(new char[barLength]).replace("\0","="));
		
		System.out.print("  General List  | ");
		for (General g: generals) {
			System.out.print(g + "  ");
		}
		System.out.println("\n" + new String(new char[barLength]).replace("\0","-"));
		
		System.out.print("     Town List  | ");
		for (Town t: towns) {
			System.out.print(t + "  ");
		}
		System.out.println("\n" + new String(new char[barLength]).replace("\0","-"));
	}
}
