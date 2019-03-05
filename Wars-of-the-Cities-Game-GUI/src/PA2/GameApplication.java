package PA2;
// submit version



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;



public class GameApplication extends Application {
	// Note: Please play the game in full-screen.
	// Resolution, Tiles and Offset for Rendering Unit ID on bottom-right.


	private static final int RESOLUTION_GAMEPLAY_WIDTH = 960;
	private static final int RESOLUTION_GAMEPLAY_HEIGHT = 720;



	// Scene and Stage
	private static final int SCENE_NUM = 2;
	private static final int SCENE_WELCOME = 0;
	private static final int SCENE_STARTGAME = 1;
	private static final String[] SCENE_TITLES = {"Welcome", "Wars of the cities"};
	private Scene[] scenes = new Scene[SCENE_NUM];
	private Stage stage;

	// Part 1: paneWelcome
	private Label lbMenuTitle;
	private Button btNewGame, btQuit;

	// Part 2: paneGameStart

	//Buttons at the botton 
	private Button btReStartGame;	
	private Button btExit;

	//Log window
	private ListView<String> listViewMessage;
	private ObservableList<String> listViewMessageItems = FXCollections.observableArrayList();
	private Label lbLog;

	private ListView<String> listViewRandomEvent;
	private ObservableList<String> listViewRandomEventItems = FXCollections.observableArrayList();
	private Label lbRandomEvent;

	//my generals
	private ListView<String> listViewGeneral;
	private ObservableList<String> listViewGeneralItems = FXCollections.observableArrayList();
	private String listViewSelectedGeneral = null;
	private Label lbMyGeneral;
	private Label lbMyGeneralTitle;

	//my cities
	private ListView<String> listViewCity;
	private ObservableList<String> listViewCityItems = FXCollections.observableArrayList();
	private String listViewSelectedCity= null;
	private Label lbMyCity;
	private Label lbMyCityTitle;

	//my neighbor
	private ListView<String> listViewNei;
	private ObservableList<String> listViewNeiItems = FXCollections.observableArrayList();
	private String listViewSelectedNei= null;
	private Label lbMyNei;
	private Label lbMyNeiTitle;

	//my gold
	private Label lbMyGold;
	//Command Button
	private Button btImproveCrop;
	private Button btCollectTax;
	private Button btRecruit;
	private Button btUpgrade;
	private Button btSendTroop;
	private Button exit;
	private TextField tfRecruit;
	private TextField tfSendTroop;

	//Map
	private Canvas canvasGameStart = new Canvas(RESOLUTION_GAMEPLAY_WIDTH, RESOLUTION_GAMEPLAY_HEIGHT);

	// Part 3: paneGameOver
	private Label lbGameOver;
	private Button btExitToMenu, btGameOverQuitGame;

	//Thead for player
	private Thread playerThread;

	private GameEngine game = new GameEngine();
	private Player humanPlayer;
	private ArrayList<Player> computerPlayers = new ArrayList<>();

	protected static ArrayList<String> printResult = new ArrayList<>();//To store the message that will be printed in the log window
	protected static ArrayList<String> RandomEventResult = new ArrayList<>(); //To store the message that will be printed in the random event window

	private boolean gameOver = false;


	private Pane paneWelcome() 
	{	// done!
		/**
		 *  To do: 
		 *  1. This function is to create the welcome scene
		 *  2. There are two buttons and a label in this scene
		 *  Hint:
		 *  1. To set the background of the pane, you can use welcomePane.getStyleClass().add("pane-welcome")
		 *  2. To set the style of the button/label, you can use 
		 *  	lbMenuTitle.getStyleClass().add("menu-title");
		 *  	btNewGame.getStyleClass().add("large-button"); 
		 *  	where lbMenuTitle is a Label and btNewGame is a button.
		 */
		BorderPane welcomePane = new BorderPane();
		welcomePane.getStyleClass().add("pane-welcome");

		// create the components of log in interface 
		Label lbMenuTitle = new Label("Wars of the cities");
		lbMenuTitle.getStyleClass().add("menu-title");
		Button btNewGame = new Button("New Game");
		btNewGame.getStyleClass().add("large-button"); 
		btNewGame.setOnAction(e->{
			handleNewGame();
		});

		Button btQuit = new Button("Quit");
		btQuit.getStyleClass().add("large-button"); 
		btQuit.setOnAction(e->{
			handleExitGame();
		});

		// use one horizontal box to contain these components
		VBox vboxLogIn = new VBox(10);
		vboxLogIn.getChildren().addAll(lbMenuTitle,btNewGame,btQuit);
		vboxLogIn.setAlignment(Pos.CENTER);
		// add log in interface to the main scene

		welcomePane.setCenter(vboxLogIn);

		return welcomePane;
	}

	private Pane paneStartGame() 
	{	
		// done!
		/**
		 * To do: 
		 * 1. This function is to create the game start scene.
		 * 2. There are two parts in this scene:
		 * 2.1 the canvas where you can draw the map 
		 * 2.2 the control part that consists of four listview, some buttons and two TextField
		 * 3. Please think that how to put the button/listview/label in the right place
		 * Hint: To set the background of the control pane, you can use pane.getStyleClass().add("pane-game-start");
		 */
		Pane componentI = paneStartGameComponentI();
		Pane componentII = paneStartGameComponentII();
		Pane componentIII = paneStartGameComponentIII();
		Pane componentIV = paneStartGameComponentIV();
		Pane componentV = paneStartGameComponentV();
		Pane componentVI = paneStartGameComponentVI();

		VBox containerCanvas = new VBox(20);
		containerCanvas.getChildren().addAll(canvasGameStart);
		containerCanvas.setAlignment(Pos.CENTER);


		VBox containerLbt = new VBox();
		containerLbt.getChildren().addAll(componentI, componentII,componentIII);
		//containerLbt.setAlignment(Pos.BOTTOM_CENTER);
		containerLbt.setPadding(new Insets(10, 20, 20, 20));
		containerLbt.setAlignment(Pos.CENTER);

		//Right Pane
		VBox containerRbt = new VBox(20);

		lbMyGold = new Label("My Gold:		");
		lbMyGold.getStyleClass().add("menu-title");
		containerRbt.getChildren().addAll(lbMyGold,componentIV, componentV,componentVI);
		containerRbt.setPadding(new Insets(10, 20, 20, 20));
		containerRbt.setAlignment(Pos.CENTER_RIGHT);

		HBox contral_pane = new HBox(20);
		contral_pane.getChildren().addAll(containerLbt,containerRbt);
		contral_pane.setAlignment(Pos.CENTER);


		BorderPane pane = new BorderPane();
		pane.setRight(contral_pane);
		pane.setCenter(containerCanvas);
		pane.getStyleClass().add("pane-game-start");

		return pane;
	}


	// begin of helper functions
	private Pane paneStartGameComponentI() {
		VBox vbMyNei = new VBox();

		listViewNei = new ListView<String>();
		listViewNei.setPrefSize(150, 200);
		listViewNei.setItems(listViewNeiItems);
		listViewNei.getStyleClass().add("game-control");



		lbMyNei = new Label("Neighbour Cities");
		lbMyNei.getStyleClass().add("menu-title");
		lbMyNeiTitle = new Label("Name  Population  Army   Crop    Wall");
		lbMyNeiTitle.getStyleClass().add("game-title");			

		vbMyNei.getChildren().add(lbMyNei);
		vbMyNei.getChildren().add(lbMyNeiTitle);
		vbMyNei.getChildren().add(listViewNei);
		vbMyNei.setAlignment(Pos.CENTER);
		return vbMyNei;
	}
	private Pane paneStartGameComponentII() {
		VBox vbMyCity = new VBox();

		listViewCity = new ListView<String>();
		listViewCity.setPrefSize(200, 200);
		listViewCity.setItems(listViewCityItems);
		listViewCity.getStyleClass().add("game-control");

		lbMyCity = new Label("My cities");
		lbMyCity.getStyleClass().add("menu-title");
		lbMyCityTitle = new Label("Name  Population  Army   Crop    Wall");
		lbMyCityTitle.getStyleClass().add("game-title");			

		vbMyCity.getChildren().add(lbMyCity);
		vbMyCity.getChildren().add(lbMyCityTitle);
		vbMyCity.getChildren().add(listViewCity);
		vbMyCity.setAlignment(Pos.CENTER);
		return vbMyCity;
	}
	private Pane paneStartGameComponentIII() {
		VBox vbMyGenerals = new VBox();


		listViewGeneral = new ListView<String>();
		listViewGeneral.setPrefSize(200, 200);
		listViewGeneral.setItems(listViewGeneralItems);

		lbMyGeneral = new Label("My Generals");
		lbMyGeneral.getStyleClass().add("menu-title");
		lbMyGeneralTitle = new Label("Name  Combat  Leadership   Wisdom    Status");
		lbMyGeneralTitle.getStyleClass().add("game-title");			
		listViewGeneral.getStyleClass().add("game-control");

		vbMyGenerals.getChildren().add(lbMyGeneral);
		vbMyGenerals.getChildren().add(lbMyGeneralTitle);
		vbMyGenerals.getChildren().add(listViewGeneral);
		vbMyGenerals.setAlignment(Pos.CENTER);
		return vbMyGenerals;
	}
	private Pane paneStartGameComponentIV() {
		VBox vbRandomEvents = new VBox();

		listViewRandomEvent = new ListView<String>();
		listViewRandomEvent.setPrefSize(200, 200);
		listViewRandomEvent.setItems(listViewRandomEventItems);
		listViewRandomEvent.getStyleClass().add("game-control");
		lbRandomEvent = new Label("Random Events");
		lbRandomEvent.getStyleClass().add("menu-title");
		vbRandomEvents.getChildren().add(lbRandomEvent);
		vbRandomEvents.getChildren().add(listViewRandomEvent);
		vbRandomEvents.setAlignment(Pos.CENTER);
		return vbRandomEvents;
	}
	private Pane paneStartGameComponentV() {
		VBox vbCommandWindow = new VBox();

		listViewMessage = new ListView<String>();
		listViewMessage.setPrefSize(200, 200);
		listViewMessage.setItems(listViewMessageItems);
		listViewMessage.getStyleClass().add("game-control");
		lbLog = new Label("Command Windows");
		lbLog.getStyleClass().add("menu-title");
		vbCommandWindow.getChildren().add(lbLog);
		vbCommandWindow.getChildren().add(listViewMessage);
		vbCommandWindow.setAlignment(Pos.CENTER);
		return vbCommandWindow;
	}
	private Pane paneStartGameComponentVI() {
		/*
		 * this is a helper function defined by myself, 
		 * it create buttton and label collection in areaVI 
		 * and return it. 
		 * */
		btCollectTax = new Button("Collect Tax");
		btUpgrade = new Button("Upgrade Town");
		btImproveCrop = new Button("Improve Crop yield");
		btRecruit = new Button("Recruit Army");
		btSendTroop = new Button("Send Troops");
		exit = new Button("Exit");

		btCollectTax.getStyleClass().add("menu-button");
		btCollectTax.setOnAction(e->{
			handleCollectTax();
		});
		btUpgrade.getStyleClass().add("menu-button");
		btUpgrade.setOnAction(e->{
			handleUpgrade();
		});
		btImproveCrop.getStyleClass().add("menu-button");
		btImproveCrop.setOnAction(e->{
			handleImproveCrop();
		});
		btRecruit.getStyleClass().add("menu-button");
		btRecruit.setOnAction(e->{
			handleRecruit();
		});
		btSendTroop.getStyleClass().add("menu-button");
		btSendTroop.setOnAction(e->{
			handleSendTroop();
		});
		exit.getStyleClass().add("menu-button");
		exit.setOnAction(e->{
			handleExitGame();
		});
		btReStartGame = new Button("Restart Game");
		btReStartGame.getStyleClass().add("menu-button");
		btReStartGame.setOnAction(e->{
			handleReStartGame();
		});
		// labels
		tfRecruit = new TextField();
		tfSendTroop = new TextField();
		// use one grid pane to contain these button and text field
		GridPane grid = new GridPane();

		grid.add(btCollectTax, 1, 1);
		grid.add(btUpgrade, 1, 2);
		grid.add(btImproveCrop, 1, 3);
		grid.add(btRecruit, 1, 4);
		grid.add(btSendTroop, 1, 5);
		grid.add(exit, 1, 6);
		grid.add(btReStartGame, 0, 6);
		grid.add(tfRecruit, 0, 4);
		grid.add(tfSendTroop, 0, 5);
		//grid.setPadding(new Insets(25, 25, 25, 25));
		grid.getStyleClass().add("game-control");
		return grid;
	}
	// end of helper functions

	private void handleExitGame(){	// done!
		/**
		 * Todo: 
		 * 1. The handler for the Button exit and quit
		 * 2. When the button exit or quit is clicked, this function will be called.
		 * 3. When the function is called, there will be a dialog box showing 
		 * 		"Do you want to exit this game?"
		 * 4. If the user choose yes, then you can call Platform.exit();
		 * 
		 * Hint: user Alert: 
		 * 		
		 */
		gameOver = true;
		//System.out.println("private void handleExitGame() was invoked");
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to exit this game?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {
				System.out.println("ButtonType.OK was clickde");
				Platform.exit();
			}
		});
	}

	//Draw the line in the map
	private void drawPath(int x1,int y1,int x2,int y2){			
		canvasGameStart.getGraphicsContext2D().strokeLine(x1,y1,x2,y2);
		canvasGameStart.getGraphicsContext2D().setStroke(Color.GREEN);
		canvasGameStart.getGraphicsContext2D().setLineWidth(5);
		canvasGameStart.getGraphicsContext2D().stroke();	
	}
	private void clearLayer(Canvas layer){
		//clear the content of the canvas
		layer.getGraphicsContext2D().clearRect(0, 0, layer.getWidth(), layer.getHeight());
	}
	//load the map and draw the towns in the map
	private void loadTownMap(){	
		/**
		 * TODO: 
		 * 1. Draw the town/city/Metropolis in the canvas according to their coordinate
		 * 2. Draw the flags next to the town/city/Metropolis's owners. 
		 * 	For the 1st player, use the "flag_0.png", for the 2nd player, use the "flag_1.png"
		 * 3. Draw the line between the connected town/city/Metropolis according to the data in the "MapData"
		 * 	You can use the drawPath(int x1,int y1,int x2,int y2) to draw the line between two points (x1,y1) and (x2,y2).
		 *
		 */
		clearLayer(canvasGameStart);
		
		for(int i = 0;i < game.players.size();i++)
		{
			Player player = game.players.get(i);
			for(Town town: player.getTownList())
			{
		ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
		for(Town neiTown:neiTowns){
			int x1 = town.getLatitude() + 48;
			int y1 = town.getLongitude() + 48;
			int x2 = neiTown.getLatitude() + 48;
			int y2 = neiTown.getLongitude() +48;
			drawPath(x1,y1,x2,y2);
		}
		}
		
		}
		
		
		for(int i = 0;i < game.players.size();i++)
		{
			Player player = game.players.get(i);
			for(Town town: player.getTownList())
			{
				int longitute = town.getLongitude();
				int latitude = town.getLatitude();
				String townName = town.getName(); 
				Image image_flag = new Image("file:flag_"+i+".png");
				if(town instanceof Metropolis)
				{
					Image image = new Image("file:metropolis.png");

					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);					
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);

				}
				else if(town instanceof City)
				{
					Image image = new Image("file:city.png");

					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);
				}
				else if(town instanceof Town)
				{
					Image image = new Image("file:town.png");

					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);

				}
				
			}
		}		
	}

	private void handleNewGame() 
	{	// done!
		/**Todo: the handler for the button "New Game"
		 * 		 when the button "New Game" is clicked, this function will be called
		 */

		/* 1. load the map information from the file "MapData.txt":
		 * 		game.gameMap.loadGameMap("MapData.txt");
		 * */
		/* 2. load the player information from the file "PlayersData.txt":
		 * 		game.loadPlayersData("PlayersData.txt");
		 * */
		/* 3. call the loadTownMap() to plot the Map; */

		/*
		 * 4. initialize the humanPlayer and the computerPlayers: 
		 *    the first user in the "PlayersData.txt" is the humanPlayer;
		 * */
		try {
			printResult.add("-------------Game begins!-------------");
			this.game.gameMap.loadGameMap("MapData.txt");
			this.game.loadPlayersData("PlayersData.txt");
			this.loadTownMap();
			this.humanPlayer = (Player)this.game.players.get(0);
			int i = 1;
			while (i < this.game.players.size()) {
				this.computerPlayers.add((Player)this.game.players.get(i));
				++i;
			}
			this.updateListViewCityItems();
			this.updateListViewGeneralItems();
			this.updateMyGold();
		}
		catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
			return;
		}
		this.putSceneOnStage(1);
		this.startTurn();
	}

	private void updateListViewRandomEvent()
	{
		//Update the listview of random event
		//Since the listview is updated outside the UI thread, Platform.runLater() is used here.

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listViewRandomEventItems.clear();
				for(int i = 0; i < RandomEventResult.size(); i++)
				{
					listViewRandomEventItems.add(RandomEventResult.get(i));
				}


			}
		}); 
	}

	private void updateListViewMessageItems()
	{
		//Update the listview of command messages
		listViewMessageItems.clear();
		for(int i = 0;i < printResult.size();i++)
		{
			listViewMessageItems.add(printResult.get(i));
		}



	}
	private void updateListViewNeiItems()
	{
		//Update the listview of Neighbor City
		listViewNeiItems.clear();

		Town town = null;

		if(listViewSelectedCity!=null)
		{
			String selectedCityName = listViewSelectedCity.split(" ")[0];
			town = getTown(selectedCityName);


			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
			for(Town neiTown:neiTowns)
			{
				int wall = 0;
				if(neiTown instanceof Metropolis)
				{
					wall = ((Metropolis) neiTown).walls;
				}
				listViewNeiItems.add(neiTown.name + "        " + neiTown.population + "         " + 
						neiTown.armySize + "       " + town.cropYield + "         " + wall);
			}
		}
	}
	private void updateListViewCityItems()
	{
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's cities.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewCityItems.add() to add the information of cities
		 */
		//Update the listview of My City
		listViewCityItems.clear();


		ArrayList<Town> myTowns = humanPlayer.getTownList();
		for(Town myTown:myTowns)
		{
			int wall = 0;
			if(myTown instanceof Metropolis)
			{
				wall = ((Metropolis) myTown).walls;
			}
			listViewCityItems.add(myTown.name + "        " + myTown.population + "         " + myTown.armySize + "       " + myTown.cropYield + "         " + wall);
		}

	}

	private void updateListViewGeneralItems() {
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's generals.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewGeneralItems.add() to add the information of generals.
		 */
		//Update the listview of Gerneral 

		listViewGeneralItems.clear();

		ArrayList<General> generals = humanPlayer.getGeneralList();
		for(General g:generals)
		{
			listViewGeneralItems.add(g.getName() + "        " + g.getCombatPoint() + "         " 
					+ g.getLeadershipPoint() + "       " + g.getWisdomPoint() + "         " + (g.isReady()?"R":"D"));
		}
	}


	//update the listview in another thread
	private void updateListView()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the human player's generals, cities and neighbor cities.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				updateListViewCityItems();
				updateListViewGeneralItems();
				updateListViewNeiItems();
				updateMyGold();
			}
		}); 
	}

	//update the listview in another thread
	private void updateMessage()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the command information
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				updateListViewMessageItems();
			}
		}); 
	}

	//update the gold of the human player
	private void updateMyGold(){
		int gold = humanPlayer.getGold();
		lbMyGold.setText("My Gold: " + gold);
	}

	private void initWelcomeSceneHandler() {
	}


	private void initListView()
	{
		/**
		 * Initialize the listeners for all listview
		 */

		listViewGeneral.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					//when a row in the listview is clicked, the listViewSelectedGeneral will be set as the content of that row
					listViewSelectedGeneral = new_val;

				}
			}
		});


		listViewCity.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedCity = new_val;
					updateListViewNeiItems();

				}
			}
		});


		listViewNei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedNei = new_val;

				}
			}
		});
	}

	private void initStartGameSceneHandler()
	{
		/**
		 *  Todo: 
		 *  To initialize the handle for all listView and button in the start game pane.
		 *  1. call the initListView(); to initialize the listview
		 *  2. call setOnAction for each button
		 */
		initListView();


	}

	private void showGameOver()
	{
		/**
		 * Todo: 
		 * TO Show Game Over using Dialog box. 
		 * When the game is over, this function will be called.
		 * Hint: 
		 * 1. Use another thread to handle it. You may use Platform.runLater in this function.
		 * 2. Show "YOU LOSE!" if the human player loses the game, otherwise "YOU WIN!"
		 * 3. You can use 
		 * 		Alert alert = new Alert(AlertType.CONFIRMATION, result, ButtonType.YES, 
		 * 		ButtonType.NO);
		 * 		where result is the String to be presented
		 */
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				String gameResult;
				if(humanPlayer.getTownList().size() == 0) {
					gameResult = "YOU LOSE!";
				}
				else {
					gameResult = "YOU WIN!";
				}
				Alert alert = new Alert(AlertType.CONFIRMATION, gameResult + "\nDo you want to exit game?", ButtonType.YES, ButtonType.NO);

				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					Platform.exit();
				}
			}


		});		

	}

	private void handleReStartGame() {
		printResult.add("Restart Game!");
		updateListViewMessageItems();
		gameOver = true;
		game.players.clear();
		computerPlayers.clear();
		try{
			game.gameMap.loadGameMap("MapData.txt");
			game.loadPlayersData("PlayersData.txt");
			loadTownMap();
			
			
		}
		catch(IOException e){
			System.out.println("Load data unsuccefully! Please check data file!");
		}
		humanPlayer = game.players.get(0);
		for(int i = 1; i < game.players.size(); i++) {
			computerPlayers.add(game.players.get(i));
		}
		updateListView();
		updateMessage();
		
		startTurn();
	}



	private void startTurn() {
		/**
		 * TODO: 
		 * 1. This function is to start the thread playerThread.
		 * 2. the function processPlayerTurns() will be run in the playerThread.
		 */

		playerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				processPlayerTurns();
			}
		});

		playerThread.start();	
	}
	
	public void processPlayerTurns()
	{
		/**
		 * TODO: 
		 * 1. This function is for processing the commands of computer players.
		 * 2. All the computer players have to wait until all the generals of human player are done.
		 * 3. When the game over or when the human player chooses to restart the game, the function will be terminated.
		 * 4. After all the computer players finish their commands, all the generals of human players will be ready.
		 * 5. Each computer player in each round will randomly select a general, a town/city/metropolis and a command.
		 * 6. The same as the PA1, there are five command"
		 * (1)collect tax;(2)upgrade town;(3)improve crop yield;(4)recruit;(5)send troops 
		 * 7. All the output message will be added in the arraylist printResult and print in the log window.
		 * 8. When the game is over, there will be a dialog box to print the result and ask whether to exit the game.
		 * 	  To achieve this goal, you can call showGameOver();
		 * 9. When the button "Restart" is clicked, this function will be over. The variable gameOver can be used here.
		 * 10. If you want to change the UI outside the UI-thread, you should use Platform.runLater(), an example is provides in the updateMessage().
		 */
		//Ensure gameOver is false;
		gameOver = false;

		while(!game.isGameOver() && !gameOver){


			listViewSelectedNei = listViewSelectedCity = listViewSelectedGeneral = null;
			//System.out.println("haha");
			humanPlayer.readyAllGenerals();
			//System.out.println("hehe");
			updateListView();

			printResult.add("-----------------" + humanPlayer.getName() + "-----------------");
			updateMessage();

			try {
				while(!game.isGameOver() && humanPlayer.hasReadyGenerals());
			}
			catch(Exception e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						updateListView();
						updateMessage();

					}
				}); 
			}



			if(gameOver || game.isGameOver()) {
				break;
			}


			for(Player computerPlayer: computerPlayers) {

				printResult.add("-----------------" + computerPlayer.getName() + "-----------------");
				updateMessage();

				computerPlayer.readyAllGenerals();
				Random random = new Random();
				General selectedGeneral;
				int cityIndex;
				int generalIndex;
				while(computerPlayer.hasReadyGenerals()) {

					int commandIndex = random.nextInt(5);
					
					generalIndex = random.nextInt(computerPlayer.getGeneralList().size());
					selectedGeneral = computerPlayer.getGeneralList().get(generalIndex);
					
					
					if(selectedGeneral.isReady()==false) {
						continue;
					}
					cityIndex = random.nextInt(computerPlayer.getTownList().size());
					Town selectedTown = computerPlayer.getTownList().get(cityIndex);


					switch(commandIndex) {
						case 0: 
						selectedTown.collectTax(computerPlayer, selectedGeneral);
						updateMessage();
						break;
						
						case 1:
							if(computerPlayer.getGold()>=50){
								computerPlayer.upgradeTown(selectedTown, selectedGeneral);
							}
							else {
								selectedTown.collectTax(computerPlayer, selectedGeneral);
							}
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									loadTownMap();
								}
							});
							updateMessage();
							break;
						case 2:
							selectedTown.improveCropYield(selectedGeneral);
							updateMessage();
							break;
						case 3:
							int budget = random.nextInt(computerPlayer.getGold());
							selectedTown.recruitArmy(computerPlayer, selectedGeneral, budget);
							updateMessage();
							break;
						case 4:
							ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(selectedTown);
							int targetTownIndex = random.nextInt(neiTowns.size());
							Town targetTown = neiTowns.get(targetTownIndex);
							
							int troopSize = (int)(selectedTown.armySize * 0.8);
							if (troopSize <= 0) {continue;}
							if(!(selectedTown instanceof City) && !(selectedTown instanceof Metropolis)) {continue;}
							
							game.processSendTroopsCommand(computerPlayer, selectedTown, targetTown, selectedGeneral, troopSize);

							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									loadTownMap();
								}
							});
							
							updateMessage();
							break;
						}
				}
			}
			for (Town town : game.gameMap.getAllTownList()) {
				town.processTurn(Math.random());
				updateListView();
				updateListViewRandomEvent();
				updateMessage();
			}

		}
		if (game.isGameOver() && !gameOver) {
			showGameOver();
		}
	}



	//return the general object given the general's name
	private General getGeneral(String generalName)
	{
		for(General general:humanPlayer.getGeneralList())
		{
			if(general.getName().equals(generalName))
			{
				return general;
			}
		}
		return null;

	}

	//return the town object given the town's name
	private Town getTown(String townName)
	{
		for(Town town:humanPlayer.getTownList())
		{
			if(townName.equals(town.name))
			{
				return town;
			}
		}
		return null;
	}

	//return the general selected by the user
	private General selectGeneral()
	{
		General selectedGeneral = null;
		if(listViewSelectedGeneral!=null)
		{
			String generalName = listViewSelectedGeneral.split(" ")[0];
			selectedGeneral = getGeneral(generalName);
			//check whether generalName is ready
			if(!selectedGeneral.isReady())
			{
				listViewSelectedGeneral = null;	
				String result = "You have to select a ready general";
				printResult.add(result);
				updateListViewMessageItems();
				return null;
			}

		}
		return selectedGeneral;
	}

	// return the town selected by the user 
	private Town selectTown()
	{
		Town selectedTown = null;
		if(listViewSelectedCity!=null)
		{
			String cityName = listViewSelectedCity.split(" ")[0];
			selectedTown = getTown(cityName);


		}
		return selectedTown;


	}

	//return the neighbor town selected by the user
	private Town selectTargetTown(Town selectedTown)
	{
		Town targetTown = null;
		if(listViewSelectedNei != null && selectedTown != null)
		{
			String cityName = listViewSelectedNei.split(" ")[0];
			System.out.println(cityName);
			System.out.println(selectedTown.name);
			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(selectedTown);
			for(Town town:neiTowns)
			{
				System.out.println(town.name);
				if(cityName.equals(town.name))
				{
					targetTown = town;
					break;
				}
			}
			listViewSelectedNei = null;
		}

		return targetTown;
	}

	private void handleSendTroop() {
		/**
		 * When the button send troop is clicked, this function will be executed.
		 */

		General selectedGeneral = null;
		Town selectedTown = null;
		Town targetTown = null;
		//get troopSize
		String tfText = tfSendTroop.getText();
		int troopSize;
		try
		{
			troopSize = Integer.parseInt(tfText);
		}catch(NumberFormatException e)
		{
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();

			return;
		}
		if (troopSize < 0) {
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			return ;	    	
		}


		//get general
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");
		}
		//get Town
		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		//get TargetTown
		targetTown = selectTargetTown(selectedTown);

		if(selectedGeneral== null || selectedTown == null || targetTown == null)
		{

			String result = "ERROR: You have to select a general, a town and a target town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}

		if (!(selectedTown instanceof City || selectedTown instanceof Metropolis)) 
		{
			System.out.println("ERROR: Invalid town type");
			String result = "ERROR: Invalid town type";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		if (humanPlayer.getTownList().contains(targetTown)) 
		{
			((City)selectedTown).transferArmy(targetTown, selectedGeneral, troopSize);
			updateListViewMessageItems();

		} 
		else 
		{

			if (((City)selectedTown).attackTown(targetTown, selectedGeneral, troopSize)) {
				updateListViewMessageItems();
				if (targetTown.getArmySize()<=0) {
					game.gameMap.getTownOwner(targetTown).surrenderTown(targetTown, humanPlayer);
					updateListViewMessageItems();

				}

				selectedGeneral.endTurn();//modified by GY	
			}
		}
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		updateMyGold();
		loadTownMap();
	}

	private void handleUpgrade() {
		/**
		 * Todo: 
		 * When the button upgrade is clicked, this function will be called.
		 * 1. call selectGeneral() to obtain the selected general
		 * 2. call selectTown() to obtain the selected town
		 * 3. call upgradeTown(selectedTown, selectedGeneral) to upgrade
		 * 4. if upgrade successfully, call loadTownMap() to redraw the map
		 * 5. update all the listview and myGold
		 */

		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		if (selectedGeneral == null || selectedTown == null) {
			if(selectedGeneral == null && selectedTown == null)
				printResult.add(new String("You have to select a general and a town"));
			else if(selectedGeneral == null) {
				printResult.add(new String("You have to select a general"));
			}
			else {
				printResult.add(new String("You have to select a town"));
			}
			updateListViewMessageItems();
			return;
		}
		if(humanPlayer.upgradeTown(selectedTown, selectedGeneral)==true) {
			loadTownMap();
			updateListViewCityItems();
			updateListViewGeneralItems();
			updateListViewMessageItems();
			updateMyGold();
		}
		listViewSelectedNei = null;
		listViewSelectedCity = null;
		listViewSelectedGeneral = null;
	}

	private void handleRecruit() {
		/**
		 * Todo: 
		 * When the button recruit is clicked, this function will be executed.
		 * 1. Obtain the input budget from the TextField using tfRecruit.getText();
		 * 2. call selectGeneral() and selectTown() to obtain the selected general and town
		 * 3. call selectedTown.recruitArmy(humanPlayer, selectedGeneral, budget) to execute the command
		 * 4. Update all the listview and myGold
		 */
		int budget = 0;
		try {
			budget = Integer.parseInt(tfRecruit.getText());
		}
		catch (NumberFormatException e) {
			printResult.add(new String("ERROR: invalid input"));
			updateListViewMessageItems();
			return;
		}
		if (budget <= 0) {
			printResult.add(new String("ERROR: invalid input"));
			updateListViewMessageItems();
			return;
		}
		
		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		
		if (selectedGeneral == null || selectedTown == null) {
			if(selectedGeneral == null && selectedTown == null)
				printResult.add(new String("You have to select a general and a town"));
			else if(selectedGeneral == null) {
				printResult.add(new String("You have to select a general"));
			}
			else {
				printResult.add(new String("You have to select a town"));
			}
			updateListViewMessageItems();
			return;
		}
		
		if(selectedTown.recruitArmy(humanPlayer, selectedGeneral, budget)==true) {
			updateListViewCityItems();
			updateListViewGeneralItems();
			updateListViewMessageItems();
			updateMyGold();
		}
		listViewSelectedNei = null;
		listViewSelectedCity = null;
		listViewSelectedGeneral = null;

	}

	private void handleCollectTax() {
		/**
		 * Todo: 
		 * When the button collect tax is clicked, this function is called.
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.collectTax(humanPlayer, selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 * 		
		 */
		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		
		if (selectedGeneral == null || selectedTown == null) {
			if(selectedGeneral == null && selectedTown == null)
				printResult.add(new String("You have to select a general and a town"));
			else if(selectedGeneral == null) {
				printResult.add(new String("You have to select a general"));
			}
			else {
				printResult.add(new String("You have to select a town"));
			}
			updateListViewMessageItems();
			return;
		}
		
		
		if(selectedTown.collectTax(humanPlayer, selectedGeneral)==true) {
			updateListViewCityItems();
			updateListViewGeneralItems();
			updateListViewMessageItems();
			updateMyGold();
		}
		listViewSelectedNei = null;
		listViewSelectedCity = null;
		listViewSelectedGeneral = null;
	}


	private void handleImproveCrop() {
		/**
		 * Todo: 
		 * When the button improve crop is clicked, this function will be called.
		 * Hints:
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.improveCropYield(selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 */
		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		
		if (selectedGeneral == null || selectedTown == null) {
			if(selectedGeneral == null && selectedTown == null)
				printResult.add(new String("You have to select a general and a town"));
			else if(selectedGeneral == null) {
				printResult.add(new String("You have to select a general"));
			}
			else {
				printResult.add(new String("You have to select a town"));
			}
			updateListViewMessageItems();
			return;
		}
		
		if(selectedTown.improveCropYield(selectedGeneral)==true) {
			updateListViewCityItems();
			updateListViewGeneralItems();
			updateListViewMessageItems();
			updateMyGold();
		}
		listViewSelectedNei = null;
		listViewSelectedCity = null;
		listViewSelectedGeneral = null;
	}

	private void initEventHandlers() {

		initWelcomeSceneHandler();
		initStartGameSceneHandler();
	}


	private void initScenes() 
	{
		scenes[SCENE_WELCOME] = new Scene(paneWelcome(), 1800,1000);
		scenes[SCENE_STARTGAME] = new Scene(paneStartGame(), 1800,1000);

		for (int i = 0; i < SCENE_NUM; i++)
		{
			scenes[i].getStylesheets().add("menu_and_css/styles.css"); // share stylesheet for all scenes
		}
	}

	private void putSceneOnStage(int sceneID) 
	{
		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
		{
			return;
		}

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.show();
	}



	@Override
	public void start(Stage primaryStage) throws Exception {

		initScenes();
		initEventHandlers();
		stage = primaryStage;
		putSceneOnStage(SCENE_WELCOME);

	}
	public static void main(String[] args)
	{
		launch(args);
	}


}
