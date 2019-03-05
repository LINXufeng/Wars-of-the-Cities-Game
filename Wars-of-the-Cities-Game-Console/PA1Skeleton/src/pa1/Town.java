package pa1;

public class Town {
	
	protected final int id;
	protected final String name;
	protected int population;
	protected int armySize;
	protected int cropYield;
	
	public Town(int id, String name, int population, int armySize, int cropYield) {
		this.id = id;
		this.name = name;
		this.population = population;
		this.armySize = armySize;
		this.cropYield = cropYield;
	}
	
	public Town(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield);
	}

	// return the unique id of the town object calling this member method
	public int getId() {
		return id;
	}
	// return the name of the town object calling this member method
	public String getName() {
		return name;
	}
	// return the army size of the town object calling this member // method
	public int getArmySize() {
		return armySize;
	}
	
	//TODO 2.4 improveCropYield()
	//		NOTE: 
	//		as described in the description file, every action involving general will conceptually define "targetValue" 
	//		which is simply baseValue*requiredAttribute/100
	//		any intermediate calculations should be handled by floating point calculations. Only the final values should 
	//		be converted back to int type.	
	//		Also, you should call general.endTurn() after any command is successfully carried out.
	public boolean improveCropYield(General general) {
		// this function should be override since the I could not depend the type of city
		double baseValue = 100f;
		int actualImprovement= (int)(baseValue * (general.getWisdomPoint()/ 100f));
		cropYield = cropYield + actualImprovement;
		// this method also needs to print a message for example: “(City HK ) improved it's crop yield by 60”
		// This method always return true
		System.out.println(this.toString()+" improved it's crop yield by "+actualImprovement);
		general.endTurn();
		return true;
	}
	
	//TODO 2.5 collectTax()
	//		baseValue = population*0.2, requiredAttribute: leadershipPoint
	//
	//		increment player's gold by targetValue
	//
	//		You don't need to check any parameter error in this function as well. Still, 
	//		remember to set general's ready state to false.
	public boolean collectTax(Player player, General general) {
		double baseVal= 0.2*population;
		int actualTaxIncome = (int)(baseVal*(general.getLeadershipPoint()/100f));
		player.earnGold(actualTaxIncome);
		// need to output a message for example: “collected 48 tax from (City HK )”
		System.out.println("collected " + actualTaxIncome + " tax from "+this.toString());
		general.endTurn();
		return true;
	}
	
	//TODO 2.6 recruitArmy()
	//		baseValue = budget, requiredAttribute: combatPoint
	//			
	//		spend player's gold to recruit army from its population.
	//			
	//		first, check whether player has enough gold to spend. (Use player.spendGold() function 
	//		which you will implement in TODO 5)
	//		then, increment armySize by targetValue while decrementing population by the same amount.
	//			
	//		In case when targetValue is larger than population or amount of budget is unreasonable, 
	//		you need to print appropriate error message and return false.
	
	public boolean recruitArmy(Player player, General general, int budget) {
		if(budget<=0) {
			System.out.println("ERROR: invalid budget");
			return false;
		}
		else if(player.spendGold(budget) == false) {
			System.out.println("ERROR: not enough gold!");
			return false;
		}
		else {
			int baseValue = budget;
			int actualValue = (int)(baseValue*(general.getCombatPoint()/100f));
			if(actualValue > population) {
				System.out.println("ERROR: cannot recruit more troops than current population!");
				return false;
			}
			else {
				armySize += actualValue;
				population -= actualValue;
				// output a message about the number of soldiers being recruited // see the pa1.jar for the sample output
				System.out.println(toString()+" recruited "+actualValue+" troops");
				general.endTurn();
				return true;
			}
		}
	}
	
	
	//TODO 2.7 receiveAttack()
	//			
	//		decrement armySize by damage and print appropriate message.
	//		armySize cannot go below 0.
	//
	public void receiveAttack(int damage) {
		assert damage>0;
		int originalArmySize = armySize;
		armySize -= damage;
		if (armySize < 0){
			armySize = 0;
			// “<Town HK > lost 20 troops”
			System.out.println(this.toString()+" lost " + originalArmySize + " troops");
		}
		else {
			System.out.println(this.toString()+" lost " + damage + " troops");
		}
	}
	
	
	//TODO 2.8 processTurn()
	//			
	//		This function is called after the end of every turn, and random number ranging from 0 to 1 is passed to this function.
	//		Town and City has 30% chance of getting hit by a natural disaster, and it will lose 20% of its population as a result.
	//		
	//		If natural disaster don't happen, its population will grow by 25% of excessCropYield.
	//		excessCropYield is calculated by cropYield - population - armySize*2
	//
	
	public void processTurn(double rand) {
		// 30% chance of getting hit by a natural disaster
		if(rand<0.3) {
			population -= (int)(population*0.2d);
			// print "(C0ity NY ) was hit by natural disaster! Lost 100 population!"
			// (City TPE)'s growth is at STAGNATION
			System.out.println(this.toString()+" was hit by natural disaster! Lost " +(int)(population *0.2d) + " population!");
			//System.out.println(this.toString()+"'s growth is at STAGNATION");
		}
		// natural disaster don't happen
		else {
			int excessCrop = cropYield - population - armySize*2;
			// print "(City SG )'s population increased by 50! Population = 320"
			if (excessCrop>0){
				population += (int) (excessCrop/4.0);
				System.out.println(this.toString()+"'s population increased by "+(int)(excessCrop/4.0)+"! Population = " + population);
			}
			else {
				excessCrop = 0;
				System.out.println(this.toString()+"'s growth is at STAGNATION");
			} 
		}
	}
	
	//=======================================
	//			End of your code
	//=======================================
	
	
	@Override
	public boolean equals(Object other) {
		return id == ((Town)other).id;
	}
	
	public String toString() {
		return String.format("<Town %-3s>", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("<%-3.3s:%-3.3s>", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize * 2 >= 0)? (cropYield - population - armySize * 2): 0;
		System.out.println(String.format("<%-5.5s: Town %-5.5s>  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d", 
				ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop));
	}
}
