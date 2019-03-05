package pa1;

public class General {

	private String name;
	private int combatPoint;
	private int leadershipPoint;
	private int wisdomPoint;
	
	private boolean isReady;


	public General(String name, int combatPoint, int leadershipPoint, int wisdomPoint) {
		this.name = name;
		this.combatPoint = combatPoint;
		this.leadershipPoint = leadershipPoint;
		this.wisdomPoint = wisdomPoint;
		isReady = false;
	}
	
	// return the name of the General object
	public String getName() {
		return name;
	}
	// return the leadershipPoint of the General object
	public int getLeadershipPoint() {
		return leadershipPoint;
	}
	// return the combatPoint of the General object
	public int getCombatPoint() {
		return  combatPoint;
	}
	// return the wisdomPoint of the General object
	public int getWisdomPoint() {
		return wisdomPoint;
	}
	//mutator to set  isReady of the General object to be true;
	public void beginTurn() {
		isReady = true;
	}

	//mutator to set  isReady of the General object to be false;
	public void endTurn() {
		isReady = false;
	}
	//accessor to return isReady of the General object
	public boolean isReady() {
		return isReady;
	}
	
	public String toString() {
		return String.format("{General %-2s}", name);
	}
	
	
	public void displayInfo() {
		System.out.println(String.format("{General %-5.5s}  COMBAT:%-3d  LEADERSHIP:%-3d WISDOM:%-3d %-1s", PrintUtils.allignMid(name, 5), combatPoint, leadershipPoint, wisdomPoint, (isReady?"READY":"DONE")));
	}
}
