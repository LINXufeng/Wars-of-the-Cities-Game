package pa1;

public class City extends Town {
	
	public City(int id, String name, int population, int armySize, int cropYield) {
		super(id, name, population, armySize, cropYield);
	}
	
	public City(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield);
	}
	
	// TODO 3.1: transferArmy(Town targetTown, General general, int troops)
	// 		Transfer the given amount of troops (army) to targetTown. You need to check whether 
	//		it is possible to send such amount of troops and print appropriate messages.
	public boolean transferArmy(Town targetTown, General general, int troops){
		if (troops>armySize || troops <= 0) {
			if(troops>armySize) {System.out.println("ERROR: not enough units!");}
			if(troops <= 0) {System.out.println("ERROR: invalid troops size!");}
			return false;
		}
		else {
			this.armySize -= troops;
			targetTown.armySize += troops;
			System.out.println(this.toString()+" transferred "+troops+" troops to "+targetTown.toString());
			general.endTurn();
			return true;
		}
	}
	
	
	// TODO 3.2: attackTown(Town targetTown, General general, int troops)
	// 		attack targetTown with given amount of troops. You need to check whether it is possible 
	//		to send such amount of troops and print appropriate messages.
	//		Make sure to call receiveAttack() instead of directly subtracting targetTown's armySize.
	public boolean attackTown(Town targetTown, General general, int troops) {
		if (troops>armySize || troops <= 0) {
			if(troops>armySize) {System.out.println("ERROR: not enough units!");}
			if(troops <= 0) {System.out.println("ERROR: invalid troops size!");}
			return false;
		}
		else {
			// armySize of this city will be changed by:
			this.armySize -= troops;
			// the real damage value to the target town is:
			int targetVal= (int)(troops * (general.getCombatPoint()/100f));
			// then call: targetTown.receiveAttack(targetVal)
			System.out.println(this.toString()+" attacked "+targetTown.toString()+" with "+troops+" troops for "+targetVal+" damage");
			targetTown.receiveAttack(targetVal);
			// and then print an appropriate message, for example:
			// “(City SG ) attacked [Metr HK ] with 15 troops for 12 damage”
			
			general.endTurn();
			return true;
		}
	}
	
	
	//=======================================
	//			End of your code
	//=======================================
	
	public String toString() {
		return String.format("(City %-3s)", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("(%-3.3s:%-3.3s)", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize * 2 >= 0)? (cropYield - population - armySize * 2): 0;
		System.out.println(String.format("(%-5.5s: City %-5.5s)  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d", 
				ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop));
	}
	
}
