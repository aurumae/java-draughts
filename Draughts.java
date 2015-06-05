/*
 * HCI & GUI Programming Project
 * Oisin McColgan and Ryan Clarke
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

// class definition for draughts game
@SuppressWarnings("deprecation")
public class Draughts extends Application {
	// overridden init method
	@Override
	public void init() {
		// initialise our widgets and layouts
		vb_mainlayout = new VBox(5);
		sp_mainlayout = new StackPane();
		hb_line1 = new HBox(5);
		hb_line2 = new HBox(5);
		dr_control = new DraughtsControl();
		sp_mainlayout.getChildren().add(dr_control);
		mb_menubar = new MenuBar();
		vb_mainlayout.getChildren().addAll(mb_menubar, hb_line1, hb_line2, 
				sp_mainlayout);
		VBox.setVgrow(sp_mainlayout, Priority.ALWAYS);

		// add in the menu items and add to menu_file
		menu_file = new Menu("Menu");
		mb_menubar.getMenus().addAll(menu_file);
		mi_new = new MenuItem("New Game");
		mi_save = new MenuItem("Save Game");
		mi_load = new MenuItem("Load Game");
		mi_quit = new MenuItem("Quit");
		menu_file.getItems().addAll(mi_new, mi_save, mi_load, mi_quit);
		btn_draw = new Button();
		btn_draw.setText("Draw");

		// moved the stuff for the text fields and labels down here
		// These text fields will hold the players' names
		tf_player1_name = new TextField();
		tf_player2_name = new TextField();
		// the text fields are bound to the String Properties in the draughts
		// board class. There is no reason for the binding to be bidirectional, but
		// I'm not sure how to implement a one directional binding
		Bindings.bindBidirectional(tf_player1_name.textProperty(),
				dr_control.getBoard().player1_name());
		Bindings.bindBidirectional(tf_player2_name.textProperty(),
				dr_control.getBoard().player2_name());

		// initialise 2 labels for player scores
		lbl_player1_score = new Label();
		lbl_player2_score = new Label();
		// again the labels are bound to IntegerProperties contained in DraughtsBoard
		lbl_player1_score.textProperty().bind(dr_control.getBoard().player1_score());
		lbl_player2_score.textProperty().bind(dr_control.getBoard().player2_score());


		lbl_playerturn = new Label();
		lbl_playerturn.textProperty().bind(dr_control.getBoard().getPlayerText());
		dr_control.getBoard().StringPropertyConstructor();

		// This block of code creates 2 "spring regions" - empty regions
		// which expand to fill empty space
		r_line1 = new Region();
		r_line2 = new Region();
		HBox.setHgrow(r_line1,Priority.ALWAYS);
		HBox.setHgrow(r_line2,Priority.ALWAYS);

		hb_line1.getChildren().addAll(tf_player1_name, lbl_player1_score,
				r_line1, lbl_playerturn);
		hb_line2.getChildren().addAll(tf_player2_name, lbl_player2_score,
				r_line2, btn_draw);

		hb_line1.setAlignment(Pos.CENTER);
		hb_line2.setAlignment(Pos.CENTER);

		// add in an event listener to a menu item, a check menu item and a
		// radio menu item
		mi_new.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dr_control.getBoard().resetGame();
			}
		});
		mi_save.setOnAction(new EventHandler<ActionEvent>() {
			// overridden method to handle an event for this menu item
			@Override
			public void handle(ActionEvent event) {
				try {
					dr_control.getBoard().save(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mi_load.setOnAction(new EventHandler<ActionEvent>() {
			// overridden method to handle an event for this menu item
			@Override
			public void handle(ActionEvent event) {
				try {
					dr_control.getBoard().load(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mi_quit.setOnAction(new EventHandler<ActionEvent>() {
			// overridden method to handle an event for this menu item
			@Override
			public void handle(ActionEvent event) {
				// quits the game
				exit();
			}
		});
		btn_draw.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				startDialogStage();

				btnYes.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dr_control.getBoard().inPlay(false);
						System.out.println("The game is a draw");
						dialogStage.close();
					}
				});

				btnNo.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dialogStage.close();
					}
				});
			}
		});
	}
	// overridden start method
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Draughts");
		primaryStage.setScene(new Scene(vb_mainlayout, 800, 800));
		primaryStage.show();
	}

	// overridden stop method
	public void stop() {}

	// entry point to our program to launch our application
	public static void main(String[] args) {
		launch(args);
	}
	// method to start the dialogue stage
	public void startDialogStage() {
		dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setScene(new Scene(VBoxBuilder.create().
				children(new Text("Do both players agree to a draw?"), btnYes, btnNo).
				alignment(Pos.CENTER).padding(new Insets(5)).build()));
		dialogStage.show();
	}

	// an exit method to leave the application
	public void exit() {
		stop();
		Platform.exit();
	}

	public Stage getStage() {
		return primaryStage;
	}
	// Declare what's needed to start
	private Stage dialogStage, primaryStage; 
	private StackPane sp_mainlayout;
	private HBox hb_line1, hb_line2;
	private Button btn_draw;
	private Region r_line1, r_line2;
	private DraughtsControl dr_control;
	private Label lbl_player1_score, lbl_player2_score, lbl_playerturn;
	private TextField tf_player1_name, tf_player2_name;
	private MenuBar mb_menubar;
	private Menu menu_file;
	private MenuItem mi_new, mi_save, mi_load, mi_quit;
	private VBox vb_mainlayout;
	private Button btnYes = new Button("Yes");
	private Button btnNo = new Button("No");
}

//class definition for a custom reversi control
class DraughtsControl extends Control {
	// constructor for the class
	public DraughtsControl() {
		// set the default skin and generate a reversi board
		setSkin(new DraughtsControlSkin(this));
		dr_board = new DraughtsBoard();
		getChildren().add(dr_board);

		clickedPiece=false;

		setOnMouseClicked(new EventHandler<MouseEvent>() {

			// overridden method to handle a mouse event
			@Override
			public void handle(MouseEvent event) {
				if(dr_board.in_play()==true) {
					//System.out.println("Mouse Handle");
					// change to = piece || crown
					// dr_board.getPiece(x/cell_width,y)>0 && 
					if(clickedPiece==false){ 
						// get starting startX startY
						startX=event.getX(); 
						startY=event.getY();
						if(dr_board.getPiece((int)(startX/(getWidth()/8.0)), (int)(startY/(getHeight()/8.0)))
								== dr_board.current_player()) {
							clickedPiece = true;
						}
					}
					else{
						// get ending endX endY
						endX=event.getX();
						endY=event.getY();

						if(dr_board.getPiece((int)(endX/(getWidth()/8.0)), (int)(endY/(getHeight()/8.0)))
								== 0) {
							// Code for handling jump chaining
							if(dr_board.jumped()) {
								dr_board.attemptJump(startX,endX,startY,endY);
							}
							else if(dr_board.can_jump()) {
								dr_board.attemptJump(startX,endX,startY,endY);
							}
							else {
								dr_board.attemptMove(startX,endX,startY,endY);
							}
						}
						clickedPiece = false;
					}
				}
			}

		});

	}
	public DraughtsBoard getBoard(){
		return dr_board;
	}
	// overridden version of the resize method
	@Override
	public void resize(double width, double height) {
		// call the super class method and resize the board
		super.resize(width, height);
		dr_board.resize(width, height);
	}

	// private fields of a draughts board
	DraughtsBoard dr_board;
	private boolean clickedPiece;
	private double startX, startY;
	private double endX , endY;
}
//class definition for a skin for the draughts control
class DraughtsControlSkin extends SkinBase<DraughtsControl> {
	// default constructor for the class
	public DraughtsControlSkin(DraughtsControl dc) {
		super(dc);
	}
}
//class definition for the reversi board
class DraughtsBoard extends Pane {
	// default constructor for the class
	public DraughtsBoard() {
		// initialise the arrays for the board and renders and lines
		// and translates
		render = new DraughtsPiece[8][8];
		can_jump = new boolean[8][8];

		// initialise renders and reset the game
		initialiseRender();
		resetGame();
	}
	public int getPiece(final int x, final int y) {
		// if the x value or y value is out of range then return -1 otherwise
		// return the board piece
		if(x < 0 || x >= 8 || y < 0 || y >= 8)
			return -1;
		else
			return render[x][y].getPiece();

	}
	// to check if a piece can make a valid move
	public void attemptMove(double X1, double X2,double Y1,double Y2){
		// get a new cell width and cell height
		cell_width = getWidth() / 8.0;
		cell_height = getHeight() / 8.0;

		int x1 = (int) (X1 / cell_width); 
		int x2 = (int) (X2 / cell_width);
		int y1 = (int) (Y1 / cell_height);
		int y2 = (int) (Y2 / cell_height);

		if(!render[x1][y1].isKing()){ 
			// get offsets
			if((current_player==1 && x2-x1==1 && y2-y1==1)
					||(current_player==1 && x2-x1==-1 && y2-y1==1)
					||(current_player==2 && x2-x1==-1 && y2-y1==-1)
					||(current_player==2 && x2-x1==1 && y2-y1==-1)) {
				if(getPiece(x2,y2)==0 && getPiece(x1,y1)==current_player) {
					if(y2== 0 && getPiece(x1,y1) == 2 && render[x1][y1].isKing()==false) {
						render[x2][y2].KingMe();
					}
					else if(y2==7 && getPiece(x1,y1) ==1 && render[x1][y1].isKing()==false) {
						render[x2][y2].KingMe();
					}
					// set original place to piece 0 which is empty
					render[x1][y1].setPiece(0);
					// set new position to the current player
					render[x2][y2].setPiece(current_player);
					// swap the players
					swapPlayers();
				}
			}
		}
		// else a King is moving which allows all 4 directions 
		else
			if((x2-x1==1 && y2-y1==1)
					||(x2-x1==-1 && y2-y1==1)
					||(x2-x1==-1 && y2-y1==-1)
					||(x2-x1==1 && y2-y1==-1)) {
				if(getPiece(x2,y2)==0) {
					render[x2][y2].KingMe();
					render[x1][y1].setPiece(0);
					render[x2][y2].setPiece(current_player);
					swapPlayers();
				}
			}
	}
	// method to attempt a jump
	public void attemptJump(double X1, double X2,double Y1,double Y2){
		// get a new cell width and cell height
		cell_width = getWidth() / 8.0;
		cell_height = getHeight() / 8.0;

		boolean wasKing = false;
		int x1 = (int) (X1 / cell_width); 
		int x2 = (int) (X2 / cell_width);
		int y1 = (int) (Y1 / cell_height);
		int y2 = (int) (Y2 / cell_height);

		if(render[x1][y1].isKing()) {
			wasKing = true;
		}
		// math.abs to get absolute value
		if(Math.abs(x2-x1) == 2 && Math.abs(y2-y1) == 2){
			if(can_jump[x2][y2] == true) {
				if(wasKing) render[x2][y2].KingMe();
				else if(y2== 0 && getPiece(x1,y1) == 2) {
					render[x2][y2].KingMe();
				}
				else if(y2==7 && getPiece(x1,y1) ==1) {
					render[x2][y2].KingMe();
				}
				render[x1][y1].setPiece(0);
				render[x1+((x2-x1)/2)][y1+((y2-y1)/2)].setPiece(0);
				render[x2][y2].setPiece(current_player);
				updateScores();
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						can_jump[i][j] = false;
					}
				}
				// if statements to check each condition.
				if(wasKing==false && render[x2][y2].isKing()==true) {
					jumped = false;
					swapPlayers();
				}
				// if no jump chain is available
				else if(!canStill_jump(x2,y2)) {
					jumped = false;
					swapPlayers();
				}
				else {
					jumped = true;
				}
			}
		}
	}
	/*
	 * The logic of this method: this method will determine if a jump is possible on the board. If one is possible it
	 * will return the square _to which_ a jump is possible. Attempt jump will lock you into selecting one of these squares
	 * 
	 */
	public boolean can_jump() {
		boolean found = false;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(render[i][j].getPiece() == current_player) {
					if(!render[i][j].isKing()) {
						if(current_player==1 && getPiece(i+1,j+1)==opposing && getPiece(i+2,j+2)==0) {
							can_jump[i+2][j+2] = true;
							found = true;
						}
						else if(current_player==1 && getPiece(i-1,j+1)==opposing && getPiece(i-2,j+2)==0) {
							can_jump[i-2][j+2] = true;
							found = true;
						}
						else if(current_player==2 && getPiece(i-1,j-1)==opposing && getPiece(i-2,j-2)==0) {
							can_jump[i-2][j-2] = true;
							found = true;
						}
						else if(current_player==2 && getPiece(i+1,j-1)==opposing && getPiece(i+2,j-2)==0) {
							can_jump[i+2][j-2] = true;
							found = true;
						}
					}
					else {
						if(getPiece(i+1,j+1)==opposing && getPiece(i+2,j+2)==0) {
							can_jump[i+2][j+2] = true;
							found = true;
						}
						else if(getPiece(i-1,j+1)==opposing && getPiece(i-2,j+2)==0) {
							can_jump[i-2][j+2] = true;
							found = true;
						}
						else if(getPiece(i-1,j-1)==opposing && getPiece(i-2,j-2)==0) {
							can_jump[i-2][j-2] = true;
							found = true;
						}
						else if(getPiece(i+1,j-1)==opposing && getPiece(i+2,j-2)==0) {
							can_jump[i+2][j-2] = true;
							found = true;
						}
					}
				}
			}
		}
		//System.out.println("can_jump ="+found);
		return found;
	}
	// method for jump chaining
	public boolean canStill_jump(int X, int Y) {
		boolean stillFound = false;

		int i = X;
		int j = Y;

		if(!render[i][j].isKing()) {
			if(current_player==1 && getPiece(i+1,j+1)==opposing && getPiece(i+2,j+2)==0) {
				can_jump[i+2][j+2] = true;
				stillFound = true;
			}
			else if(current_player==1 && getPiece(i-1,j+1)==opposing && getPiece(i-2,j+2)==0) {
				can_jump[i-2][j+2] = true;
				stillFound = true;
			}
			else if(current_player==2 && getPiece(i-1,j-1)==opposing && getPiece(i-2,j-2)==0) {
				can_jump[i-2][j-2] = true;
				stillFound = true;
			}
			else if(current_player==2 && getPiece(i+1,j-1)==opposing && getPiece(i+2,j-2)==0) {
				can_jump[i+2][j-2] = true;
				stillFound = true;
			}
		}
		else {
			if(getPiece(i+1,j+1)==opposing && getPiece(i+2,j+2)==0) {
				can_jump[i+2][j+2] = true;
				stillFound = true;
			}
			else if(getPiece(i-1,j+1)==opposing && getPiece(i-2,j+2)==0) {
				can_jump[i-2][j+2] = true;
				stillFound = true;
			}
			else if(getPiece(i-1,j-1)==opposing && getPiece(i-2,j-2)==0) {
				can_jump[i-2][j-2] = true;
				stillFound = true;
			}
			else if(getPiece(i+1,j-1)==opposing && getPiece(i+2,j-2)==0) {
				can_jump[i+2][j-2] = true;
				stillFound = true;
			}
		}

		//System.out.println("Boolean stillFound = "+stillFound);
		return stillFound;
	}
	// overridden version of the resize method to give the board the correct size
	@Override
	public void resize(double width, double height) {
		// call the superclass method
		super.resize(width, height);

		// get a new cell width and cell height
		cell_width = width / 8.0;
		cell_height = height / 8.0;

		// resize and relocate all pieces that are in the board
		pieceResizeRelocate();
	}
	// public method for resetting the game
	public void resetGame() {
		// reset the board state
		resetRenders();

		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++){
				render[i][j].unKing();
			}
		}

		// set the pieces for the starting of the game
		// player 1
		render[0][0].setPiece(1); render[2][0].setPiece(1); render[4][0].setPiece(1); render[6][0].setPiece(1);
		render[1][1].setPiece(1); render[3][1].setPiece(1); render[5][1].setPiece(1); render[7][1].setPiece(1);
		render[0][2].setPiece(1); render[2][2].setPiece(1); render[4][2].setPiece(1); render[6][2].setPiece(1);

		// player 2
		render[1][5].setPiece(2); render[3][5].setPiece(2); render[1][5].setPiece(2); render[3][5].setPiece(2);
		render[5][5].setPiece(2); render[7][5].setPiece(2);
		render[0][6].setPiece(2); render[2][6].setPiece(2); render[4][6].setPiece(2); render[6][6].setPiece(2);  
		render[1][7].setPiece(2); render[3][7].setPiece(2); render[5][7].setPiece(2); render[7][7].setPiece(2);  

		// set the game in play, current player to one, and that neither player has won
		// and both players have a score of two
		in_play = true;
		current_player = 2;
		opposing = 1;
		updateScores();
		updateNames("Player 1", "Player 2");
		StringPropertyConstructor();

	}
	// save method
	public void save(Stage saveStage) throws IOException {
		// use a file chooser to a file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text Files", "*.txt"));
		File selectedFile = fileChooser.showSaveDialog(saveStage);
		if (selectedFile != null) {
			PrintStream fileWriter = new PrintStream(selectedFile);
			try {
				// try to save the file
				for(int i = 0;i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						// saves the entire board
						fileWriter.println(Boolean.toString(render[i][j].isKing()));
						fileWriter.println(render[i][j].getPiece());
						fileWriter.println(Boolean.toString(can_jump[i][j]));
					}
				}
				// use a file writer write to a file
				fileWriter.println(player1_score);
				fileWriter.println(player2_score);
				fileWriter.println(player1_name.getValueSafe());
				fileWriter.println(player2_name.getValueSafe());
				fileWriter.println(current_player);
				fileWriter.println(opposing);
				fileWriter.println(Boolean.toString(in_play));
				fileWriter.println(Boolean.toString(jumped));
			}
			finally{
				// close the writer
				fileWriter.close();
			}
		}
	}
	// load method
	public void load(Stage saveStage) throws IOException {
		// use a file chooser to select the file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Save File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text Files", "*.txt"));
		File selectedFile = fileChooser.showOpenDialog(saveStage);
		if (selectedFile != null) {
			resetGame();
			Scanner scanner = new Scanner(new FileInputStream(selectedFile));
			try {
				// read in each line from the file
				while (scanner.hasNextLine()){
					for(int i = 0;i < 8; i++) {
						for(int j = 0; j < 8; j++) {
							// reads the entire board
							render[i][j].setKing(Boolean.parseBoolean(scanner.nextLine()));
							render[i][j].setPiece(Integer.parseInt(scanner.nextLine()));
							can_jump[i][j] = Boolean.parseBoolean(scanner.nextLine());
						}
					}
					// Reads the data from the file
					player1_score = Integer.parseInt(scanner.nextLine());
					player2_score = Integer.parseInt(scanner.nextLine());
					player1_name.setValue(scanner.nextLine());
					player2_name.setValue(scanner.nextLine());
					current_player = Integer.parseInt(scanner.nextLine());
					opposing = Integer.parseInt(scanner.nextLine());
					in_play = Boolean.parseBoolean(scanner.nextLine());
					jumped = Boolean.parseBoolean(scanner.nextLine());

				}
			}
			finally{
				// close the scanner
				scanner.close();
				// call methods to keep game up to date
				bindNames();
				updateScores();
			}
		}
	}
	// a method to swap the current players
	public void swapPlayers() {
		if(current_player == 1) {
			current_player = 2;
			opposing = 1;
			playerText.unbind();
			playerText.bind(Bindings.concat(player1_name.getValueSafe()).concat(" - Black's Turn"));;
		} else {
			current_player = 1;
			opposing = 2;
			playerText.unbind();
			playerText.bind(Bindings.concat(player2_name.getValueSafe()).concat(" - Red's Turn"));
		}
		// if a jump is available
		if(can_jump())
			if(current_player==2){
				// print out there is one available
				System.out.println(player1_name.getValueSafe()+" has a jump available");
			}
			else if(current_player==1){
				System.out.println(player2_name.getValueSafe()+" has a jump available");
			}
	}
	// a method to update the scores
	private void updateScores() {
		player1_score = 0;
		player2_score = 0;

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(render[i][j].getPiece() == 2)
					player1_score++;
				else if(render[i][j].getPiece() == 1)
					player2_score++;
			}
		}
		// set the score values
		playerScore1.setValue(player1_score);
		playerScore2.setValue(player2_score);
		
		// if either players score is 0 the game should be set to false and a winner will be chosen
		if(player1_score==0 || player2_score ==0) {
			inPlay(false);
			if(player1_score ==0) System.out.println("Congratulations "+player2_name.getValueSafe()+" you are the winner");
			else if(player2_score ==0) System.out.println("Congratulations "+player1_name.getValueSafe()+" you are the winner");
		}
	}
	private void pieceResizeRelocate() {
		// for each piece set a new position and a new size
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				render[i][j].resize(cell_width, cell_height);
				render[i][j].relocate(i * cell_width, j * cell_height);
				render[i][j].setBackground(i,j);
			}
		}
	}
	// update player names
	public void updateNames(String name1, String name2){
		player1_name.setValue(name1);
		player2_name.setValue(name2);
	}
	// private method that will reset the renders
	private void resetRenders() {
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				render[i][j].setPiece(0);
	}
	public ObservableStringValue getPlayerText(){
		return playerText;
	}
	public ObservableStringValue player1_score() {
		return obsPlayerScore1;
	}
	public ObservableStringValue player2_score() {
		return obsPlayerScore2;
	}
	// return scores
	public int score1() {
		return player1_score;
	}
	public int score2() {
		return player2_score;
	}
	// return string properties
	public StringProperty player1_name() {
		return player1_name;
	}
	public StringProperty player2_name() {
		return player2_name;
	}
	public int current_player() {
		return current_player;
	}
	public int opposing() {
		return opposing;
	}
	public boolean in_play() {
		return in_play;
	}
	public boolean jumped() {
		return jumped;
	}
	public boolean canJump(int i, int j) {
		return can_jump[i][j];
	}
	// private method that will initialise everything in the render array
	private void initialiseRender() {
		// initialise a render object to initialise all elements and add them to the scene graph
		// they should all have a type of zero to begin with
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				render[i][j] = new DraughtsPiece(0);
				getChildren().add(render[i][j]);
			}
		}
	}
	// public method inPlay to return the in_play value
	public void inPlay(Boolean newInPlay) {
		in_play = newInPlay;
	}

	// arrays for the internal representation of the board and the pieces that are
	// in place
	private DraughtsPiece[][] render;
	// the current player who is playing and who is his opposition
	private int current_player;
	private int opposing;
	// is the game currently in play
	private boolean in_play;
	private boolean jumped;
	// current scores of player 1 and player 2
	private int player1_score;
	private int player2_score;
	// these track the values of the player scores 
	private IntegerProperty playerScore1 = new SimpleIntegerProperty(12);
	private IntegerProperty playerScore2 = new SimpleIntegerProperty(12);
	// the width and height of a cell in the board
	private double cell_width;
	private double cell_height;
	// 3x3 array that determines if a reverse can be made in any direction
	private boolean[][] can_jump;
	// these track the players names
	private StringProperty player1_name = new SimpleStringProperty("Player 1");
	private StringProperty player2_name = new SimpleStringProperty("Player 2");
	private StringProperty playerText = new SimpleStringProperty(player1_name.getValueSafe()+" - Black's Turn");
	private StringProperty obsPlayerScore1 = new SimpleStringProperty("Score: "+playerScore1.getValue().toString());
	private StringProperty obsPlayerScore2 = new SimpleStringProperty("Score: "+playerScore2.getValue().toString());



	// these methods bind the names of the players to the associated fields in the window, which causes them to update in real time
	public void bindNames(){
		if(current_player==2){
			playerText.bind(Bindings.concat(player1_name).concat(" - Black's Turn"));
		}
		else{
			playerText.bind(Bindings.concat(player2_name).concat(" - Red's Turn"));
		}
	}
	public void StringPropertyConstructor() {
		if(current_player==2){
			playerText.bind(Bindings.concat(player1_name).concat(" - Black's Turn"));
		}
		else{
			playerText.bind(Bindings.concat(player2_name).concat(" - Red's Turn"));
		}
		obsPlayerScore1.bind(Bindings.concat("Score: ").concat(playerScore1));
		obsPlayerScore2.bind(Bindings.concat("Score: ").concat(playerScore2));
	}
}

//class definition for a reversi piece
class DraughtsPiece extends Group {
	// default constructor for the class
	public DraughtsPiece(int player) {
		// take a copy of the player
		this.player = player;

		// generate the ellipse for the player. if this is player 1 then this should
		// be white otherwise it should be black
		piece = new Ellipse();
		t = new Translate();
		background=new Rectangle();
		King = new Ellipse();
		King.getTransforms().add(t);
		background.getTransforms().add(t);
		getChildren().addAll(background);
		King.setFill(Color.WHITE);

		piece.getTransforms().add(t);

		if(player == 1) {
			piece.setFill(Color.RED);
		}
		else {
			piece.setFill(Color.BLACK);
		}

		getChildren().add(piece);
		getChildren().add(King);

		// if the player is set to zero then hide this piece
		if(player == 0) {
			piece.setVisible(false);
			King.setVisible(false);
		}
	}

	// overridden version of the resize method to give the piece the correct size
	@Override
	public void resize(double width, double height) {
		// call the superclass method
		super.resize(width, height);

		// resize and relocate the ellipse
		King.setCenterX(width / 2.0); King.setCenterY(height / 2.0);
		King.setRadiusX(width / 3.0); King.setRadiusY(height / 3.0);
		piece.setCenterX(width / 2.0); piece.setCenterY(height / 2.0);
		piece.setRadiusX(width / 2.0); piece.setRadiusY(height / 2.0);
		background.setWidth(width); background.setHeight(height);
	}

	// overridden version of the relocate method to position the piece correctly
	@Override
	public void relocate(double x, double y) {
		// call the superclass method
		super.relocate(x, y);
		// update the translate with the new position
		t.setX(x); t.setY(y);
	}


	// method that will set the piece type
	public void setPiece(final int type) {
		player = type;

		// set the colour of the piece and if necessary make it visible
		if(isKing()==true){
			if(type == 1) {
				piece.setFill(Color.RED);
				piece.setVisible(true);
				King.setFill(Color.WHITE);
				King.setVisible(true);
			} else if (type == 2) {
				piece.setFill(Color.BLACK);
				piece.setVisible(true);
				King.setFill(Color.WHITE);
				King.setVisible(true);
			} else if (type == 0) {
				King.setVisible(false);
				piece.setVisible(false);
			}
		}
		else{
			King.setVisible(false);
			if(type == 1) {
				piece.setFill(Color.RED);
				piece.setVisible(true);
			} else if (type == 2) {
				piece.setFill(Color.BLACK);
				piece.setVisible(true);
			} else if (type == 0) {
				piece.setVisible(false);
			}
		}

	}

	// returns the type of this piece
	public int getPiece() {  
		return player;
	}
	// set the background
	public void setBackground(int i, int j){
		if((i+j)%2==0){
			background.setFill(Color.WHITE);
		}
		else if((i+j)%2!=0){
			background.setFill(Color.GREEN);
		}

	}
	// method to king player
	public void KingMe(){
		isKing = true;
		System.out.println("Hail to the King Baby!");

	}
	// boolean to check if player is King
	public boolean isKing(){
		return isKing;
	}
	// unking used to fix bug that forced old kings to be carried over to a new game
	public void unKing(){
		isKing = false;
	}
	// set the king value
	public void setKing(boolean value) {
		isKing = value;
	}
	// private fields
	private int player;		// the player that this piece belongs to
	private Ellipse piece;	// ellipse representing the player's piece
	// rectangle that makes the background of the board
	private Rectangle background;
	private Translate t;	// translation for the player piece
	private boolean isKing;
	private Ellipse King;
}
