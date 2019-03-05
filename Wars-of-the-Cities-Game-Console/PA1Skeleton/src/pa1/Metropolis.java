package pa1;

public class Metropolis extends City{
	
	private int walls;
	
	public Metropolis(int id, String name, int population, int armySize, int cropYield) {
		super(id, name, population, armySize, cropYield);
		walls = 0;
	}
	
	public Metropolis(Town town) {
		this(town.id, town.name, town.population, town.armySize, town.cropYield);
	}
	
	//TODO 4.1 receiveAttack()
	//			
	//		Metropolis has walls that absorb damage.
	//		army stationed in metropolis will not get harmed until walls reach 0.
	//
	@Override
	public void receiveAttack(int damage) {
		// Correctness is already checked at City.attackTown()
		// walls -= damage until walls reaches 0.
		// armySize= armySize-max(damage-walls,0)
		// print an appropriate message:
		
		//      [Metr HK ]'s wall absorbed 5 damage
		//      [Metr HK ] lost 0 troops
		//OR
		//      [Metr HK ]'s wall absorbed 10 damage
		//		[Metr HK ] lost 12 troops
		int originalArmySize = armySize;
		int originalWalls = walls;
		int damageToArmy = damage - walls;
		//walls =  damageToArmy > 0 ? 0 : walls - damage;
		
		if (damage>=walls+armySize){
			// all armies and walls are destroyed
			armySize = 0;
			walls = 0;
			System.out.println(this.toString()+"'s wall absorbed " + originalWalls + " damage");
			System.out.println(this.toString()+" lost " + originalArmySize + " troops");
			return;
		}
		
		if(damage<=walls) {
			// only part of walls destroyed, no troops died
			walls-=damage;
			System.out.println(this.toString()+"'s wall absorbed " + damage + " damage");
			System.out.println(this.toString()+" lost 0 troops");
			return;
		}
		else{
			// all walls and some troops died
			walls = 0;
			armySize -= damageToArmy;
			System.out.println(this.toString()+"'s wall absorbed " + originalWalls + " damage");
			System.out.println(this.toString()+" lost " + damageToArmy + " troops");
			return;
		}	
	}
	
	//TODO 4.2 processTurn()
	//
	//		Metropolis follows the same behavior as Town and City, Except that it does not get affected by natural disaster at all.
	//		In addition to that, metropolis will regenerate wall by 10 hit points.
	//
	public void processTurn(double rand){
		// Unlike town or city, there is no disaster for a Metropolis // object. Other things are exactly same as
		// Town.processTurn().
		// update walls by: walls += 10
		int excessCrop = (int)(cropYield - population - armySize*2);
		population += (int)(excessCrop/4.0);
		walls += 10;
		// output an appropriate message:
		// [Metr HK ]'s population increased by 6! Population = 506 
		// [Metr HK ] regenerated wall by 10 hit points! Walls = 10 
		//OR
		// [Metr HK ]'s growth is at STAGNATION
		// [Metr HK ] regenerated wall by 10 hit points! Walls = 10
		if (excessCrop>0){
			System.out.println(this.toString()+"'s population increased by "+(int)(excessCrop/4.0)+"! Population = " + population);
			System.out.println(this.toString()+" regenerated wall by 10 hit points! Walls = "+walls);		
		}
		else {
			System.out.println(this.toString()+"'s growth is at STAGNATION");
			System.out.println(this.toString()+" regenerated wall by 10 hit points! Walls = "+walls);		
		}
	}
	
	//=======================================
	//			End of your code
	//=======================================
	
	public String toString() {
		return String.format("[Metr %-3s]", name);
	}
	
	public String tokenize(String ownerName) {
		return String.format("[%-3.3s:%-3.3s]", PrintUtils.allignMid(ownerName, 3), PrintUtils.allignMid(name, 3));
	}
	
	public void displayInfo(String ownerName) {
		int excessCrop = (cropYield - population - armySize * 2 >= 0)? (cropYield - population - armySize*2): 0;
		System.out.println(String.format("[%-5.5s: Metr %-5.5s]  Population:%-5d  Army:%-5d Crop_yield:%-5d Excess_crop:%-5d Walls:%-5d",
				ownerName, PrintUtils.allignMid(name, 5), population, armySize, cropYield, excessCrop, walls));
	}
}
