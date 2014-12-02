/*
 * HCI & GUI Programming Project
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
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

		// TODO modify (copy/pasted from notes)
		mb_menubar = new MenuBar();
		menu_file = new Menu("Menu");
		mb_menubar.getMenus().addAll(menu_file);
		vb_mainlayout.getChildren().addAll(mb_menubar, hb_line1, hb_line2, 
				sp_mainlayout);
		VBox.setVgrow(sp_mainlayout, Priority.ALWAYS);

		// add in the menu items and check menu items to the file menu to the
		// menus
		mi_new = new MenuItem("New Game");
		mi_save = new MenuItem("Save Game");
		mi_load = new MenuItem("Load Game");
		mi_quit = new MenuItem("Quit");
		menu_file.getItems().addAll(mi_new, mi_save, mi_load, mi_quit);

		player1_name = "Player 1";
		player2_name = "Player 2";
		tf_player1_name = new TextField(player1_name);
		tf_player2_name = new TextField(player2_name);
		// initialise scores at 8
		player1_score = 12; player2_score = 12;
		lbl_player1_score = new Label("Score: "+player1_score);
		lbl_player2_score = new Label("Score: "+player2_score);
		// initialise string activePlayer
		activePlayer = 1;
		lbl_playerturn = new Label(player1_name+" - Black's turn");
		//lbl_playerturn.textProperty().bind(activePlayer);
		btn_draw = new Button();
		btn_draw.setText("Draw");

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
				/*
				 * TODO implement saveGame()
				startSaveDialog();

				tf_saveName.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						File saveFile = new File(tf_saveName.getText());
						try ( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( saveFile ))) {
							out.writeObject (dr_control.getBoard());
						} catch (IOException ioe) {
							// do something if there is an error, at least this so you
							// know if something went wrong
							ioe.printStackTrace();
						}
					}
				});
			*/
			}
		});
		mi_load.setOnAction(new EventHandler<ActionEvent>() {
			// overridden method to handle an event for this menu item
			@Override
			public void handle(ActionEvent event) {
				// TODO loadGame();
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
		tf_player1_name.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				player1_name = tf_player1_name.getText();
				if(activePlayer == 1) lbl_playerturn.setText(player1_name+" - Black's Turn");
			}
		});
		tf_player2_name.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				player2_name = tf_player2_name.getText();
				if(activePlayer == 2) lbl_playerturn.setText(player2_name+" - Red's Turn");
			}
		});
	}

	// overridden start method
	@Override
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
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

	public void startDialogStage() {
		dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setScene(new Scene(VBoxBuilder.create().
				children(new Text("Do both players agree to a draw?"), btnYes, btnNo).
				alignment(Pos.CENTER).padding(new Insets(5)).build()));
		dialogStage.show();
	}

	/*
	public void startSaveDialog() {
		saveDialog = new Stage();
		saveDialog.initModality(Modality.WINDOW_MODAL);
		saveDialog.setScene(new Scene(VBoxBuilder.create().
				children(tf_saveName).
				alignment(Pos.CENTER).padding(new Insets(5)).build()));
		saveDialog.show();
	}
	*/

	public void exit() {
		stop();
		Platform.exit();
	}


	private Stage dialogStage; 
	// private Stage saveDialog;
	private StackPane sp_mainlayout;
	private int activePlayer;
	private String player1_name, player2_name;
	private HBox hb_line1, hb_line2;
	private Button btn_draw;
	private Region r_line1, r_line2;
	private DraughtsControl dr_control;
	private Label lbl_player1_score, lbl_player2_score, lbl_playerturn;
	private TextField tf_player1_name, tf_player2_name;
	// private TextField tf_saveName = new TextField("Enter Save Name");
	private MenuBar mb_menubar;
	private Menu menu_file;
	private MenuItem mi_new, mi_save, mi_load, mi_quit;
	private VBox vb_mainlayout;
	// TODO might need to move these
	private int player1_score, player2_score;
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

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			// overridden method to handle a mouse event
			@Override
			public void handle(MouseEvent event) {
				dr_board.placePiece(event.getX(), event.getY());
			}
		});
	}

	public DraughtsBoard getBoard() {
		return dr_board;
	}

	// overridden version of the resize method
	@Override
	public void resize(double width, double height) {
		// call the super class method and resize the board
		super.resize(width, height);
		dr_board.resize(width, height);
	}

	// private fields of a reversi board
	DraughtsBoard dr_board;
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
		horizontal = new Line[8];
		vertical = new Line[8];
		horizontal_t = new Translate[8];
		vertical_t = new Translate[8];
		can_jump = new boolean[3][3];

		// initialise the grid lines and background, renders and reset the game
		initialiseRender();
		resetGame();
	}

	// public method that will try to place a piece in the given x,y coordinate
	public void placePiece(final double x, final double y) {}

	// overridden version of the resize method to give the board the correct size
	@Override
	public void resize(double width, double height) {
		// call the superclass method
		super.resize(width, height);

		// get a new cell width and cell height
		cell_width = width / 8.0;
		cell_height = height / 8.0;

		// resize the background. resize the lines and reposition them
		// resize and relocate all pieces that are in the board
		pieceResizeRelocate();
	}

	// public method for resetting the game
	public void resetGame() {
		// reset the board state
		resetRenders();

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
		player1_score = player2_score = 12;

	}


	private void swapPlayers() {
		if(current_player == 1) {
			current_player = 2;
			opposing = 1;
		} else {
			current_player = 1;
			opposing = 2;
		}
	}

	private void updateScores() {
		player1_score = 0;
		player2_score = 0;

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(render[i][j].getPiece() == 1)
					player1_score++;
				else if(render[i][j].getPiece() == 2)
					player2_score++;
			}
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


	// private method that will reset the renders
	private void resetRenders() {
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				render[i][j].setPiece(0);
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

	public void inPlay(boolean newInPlay) {
		in_play = newInPlay;

	}

	// arrays for the lines that makeup the horizontal and vertical grid lines
	private Line[] horizontal;
	private Line[] vertical;
	// arrays holding translate objects for the horizontal and vertical grid lines
	private Translate[] horizontal_t;
	private Translate[] vertical_t;
	// arrays for the internal representation of the board and the pieces that are
	// in place
	private DraughtsPiece[][] render;
	// the current player who is playing and who is his opposition
	private int current_player;
	private int opposing;
	// is the game currently in play
	private boolean in_play;
	// current scores of player 1 and player 2
	private int player1_score;
	private int player2_score;
	// the width and height of a cell in the board
	private double cell_width;
	private double cell_height;
	// 3x3 array that determines if a reverse can be made in any direction
	private boolean[][] can_jump;
}

// class definition for a reversi piece
class DraughtsPiece extends Group {
	// default constructor for the class
	public DraughtsPiece(int player) {
		// take a copy of the player
		this.player = player;

		// generate the ellipse for the player. if this is player 1 then this should
		// be white otherwise it should be black
		piece = new Ellipse();
		t = new Translate();
		background = new Rectangle();
		background.getTransforms().add(t);
		getChildren().addAll(background);

		piece.getTransforms().add(t);

		if(player == 1)
			piece.setFill(Color.RED);
		else
			piece.setFill(Color.BLACK);
		getChildren().add(piece);

		// if the player is set to zero then hide this piece
		if(player == 0)
			piece.setVisible(false);
	}

	// overridden version of the resize method to give the piece the correct size
	@Override
	public void resize(double width, double height) {
		// call the superclass method
		super.resize(width, height);

		// resize and relocate the ellipse
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

	// returns the type of this piece
	public int getPiece() {  
		return player;
	}

	public void setBackground(int i, int j){
		if((i+j)%2==0){
			background.setFill(Color.WHITE);
		}
		else if((i+j)%2!=0){
			background.setFill(Color.GREEN);
		}

	}
	// private fields
	private int player;		// the player that this piece belongs to
	private Ellipse piece;	// ellipse representing the player's piece
	// rectangle that makes the background of the board
	private Rectangle background;
	private Translate t;	// translation for the player piece
}
