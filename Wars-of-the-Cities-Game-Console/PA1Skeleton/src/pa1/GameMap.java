package pa1;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class GameMap {
	
	private final ArrayList<Player> players;

	private int townsCount;
	private int[][] adjacencyMatrix;
	private ArrayList<String> displayMap;
	
	public GameMap(ArrayList<Player> players) {
		this.players = players;
		displayMap = new ArrayList<String>();
	}
	
	// TODO 6: Load adjacencyMatrix and displayMap based on specified input textFile.
	public void loadGameMap(String filename) throws IOException {
		try (Scanner scanner = new Scanner(new File(filename)))
		{
			// Create a file object and provide the path of the file
			// MapData.txt. Open a Scanner object using the file object // read the file line by line.
			// First line is an int number indicate the number of cities in the game
			townsCount = scanner.nextInt();
			// The next few lines are for the adjacency matrix.
			// if there are 3 cities, and city 0 connects to city 1, city 
			// 1 connects to cities 0 and 2, we have the following matrix: 
			// 0 1 0
			// 1 0 1
			// build an 2-D int matrix and reference it by the instance 
			// variable adjacencyMatrix, i.e.
			// adjacencyMatrix = new int[numberOfTowns][numberOfTowns]; 
			// read the adjacency matrix from the file to the 2-D array 
			// then read the remaining lines of the file and add the lines 
			// one by one to displayMap ArrayList.
			adjacencyMatrix = new int[townsCount][townsCount]; 
			for(int i = 0; i < townsCount; ++i) {
				for(int j = 0; j < townsCount; ++j) {
					adjacencyMatrix[i][j] = scanner.nextInt();
				}
			}
			while(scanner.hasNextLine()) {
				displayMap.add(scanner.nextLine());
			}
		}
	}
	
	// TODO 7: return the list of all nearby towns of the given town by referring to adjacency matrix. Return empty matrix if there is no nearby towns.
	// HINT: if adjacencyMatrix[i][j] = 1, then town with ID = i and town with ID = j are adjacent to each other. Otherwise, it is false.
	//       use getTownById() defined below to find town object by id.
	
	public ArrayList<Town> getAdjacentTownList(Town town) {
		//return the list of all nearby towns of the given town by
		// referring to adjacency matrix. Return empty matrix if there
		// is no nearby towns.
		// HINT: if adjacencyMatrix[i][j] = 1, then town with ID = i 
		// and town with ID = j are adjacent to each other. Otherwise,
		// it is false.
		// use getTownById() defined below to find town object by id. 
		// assume town id goes from 0,1,...,n-1, where n is the total 
		// number of towns in the MapData.txt file	
		
		// town have member variable protected final int id;
		ArrayList<Town> adjacentTownList = new ArrayList<Town>();
		for(int j =0;j<adjacencyMatrix.length;++j) {
			if(adjacencyMatrix[town.getId()][j] == 1) {
				adjacentTownList.add(getTownById(j));
			}
		}
		return adjacentTownList;
	}
	
	
	
	public Town getTownById(int id) {
		for (Player p: players) {
			for (Town t: p.getTownList()) {
				if (t.getId() == id) {
					return t;
				}
			}
		}
		return null;
	}
	
	public Player getTownOwner(Town town) {
		for (Player p: players) {
			if (p.getTownList().contains(town)) {
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Town> getAllTownList() {
		ArrayList<Town> towns = new ArrayList<Town>();
		for (Player p: players) {
			for (Town t: p.getTownList()) {
				towns.add(t);
			}
		}
		return towns;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (String line: displayMap) {
			StringBuffer processedLine = new StringBuffer(line);
			char[] chrSeq = line.toCharArray();
			
			for (int i = 0; i<chrSeq.length; ++i) {
				
				// stop pointer at the starting point of id
				if (Character.isDigit(chrSeq[i])) {
					int id=Integer.parseInt(extractId("", i, chrSeq));
					
					// replace id token with String representation of city
					processedLine.replace(i-4, i+5, getTownById(id).tokenize(getTownOwner(getTownById(id)).getName()));
					
					// skip pointer to the end of id
					if (id!=0) {
						i += (int)(Math.log10(id));
					}
				}
			}
			
			s.append(processedLine);
			s.append("\n");
		}
		return s.toString();
	}
	
	private String extractId(String id, int idx, char[] chrSeq) {
		if (idx >= chrSeq.length || !Character.isDigit(chrSeq[idx])) {
			return id;
		}
		
		return chrSeq[idx] + extractId(id, idx+1, chrSeq);
	}
}
