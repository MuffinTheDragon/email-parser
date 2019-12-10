package emailparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This module is the main view module and creates the visualization after 
 * parsing an e-mail.
 * 
 * @author Dhaval
 *
 */
public class View implements EventHandler<ActionEvent> {

	private EmailModel model;
	private Stage stage;

	private Label addressTo;
	private Label addressFrom;
	private Label subject;
	private Label body;

	public View(EmailModel model, Stage stage) {
		this.addressTo = new Label("To: ");
		this.addressFrom = new Label("From: ");
		this.subject = new Label("Subject: ");
		this.body = new Label("Body: ");

		this.model = model;
		this.stage = stage;
		stageView(stage);
	}

	private void setModel(EmailModel em) {
		this.model = em;
		rewrite();
	}
	
	private void rewrite() {
		String[] info = this.model.getInfo();

		this.addressFrom.setText("From: " + info[0]);
		this.addressTo.setText("To: " + info[1]);
		this.subject.setText("Subject: " + info[2]);
		this.body.setText(info[3]);
	}

	private void stageView(Stage stage) {
		VBox layout = new VBox(10);

		HBox addressTo = new HBox();
		addressTo.getChildren().addAll(this.addressTo);
		addressTo.setAlignment(Pos.TOP_LEFT);

		HBox addressFrom = new HBox();
		addressFrom.getChildren().addAll(this.addressFrom);
		addressFrom.setAlignment(Pos.TOP_LEFT);

		HBox subject = new HBox();
		subject.getChildren().addAll(this.subject);
		subject.setAlignment(Pos.TOP_LEFT);

		HBox body = new HBox();
		body.getChildren().addAll(this.body);
		body.setAlignment(Pos.TOP_LEFT);

		HBox menuBar = new HBox();
		menuBar.getChildren().addAll(createMenuBar());

		layout.getChildren().addAll(menuBar, addressTo, addressFrom, subject, body);
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.setMinHeight(500);
		stage.setMinWidth(500);
		stage.show();
	}

	private MenuBar createMenuBar() {

		MenuBar menuBar = new MenuBar();
		MenuItem menuItem;
		Menu menu = new Menu("File");

		menuItem = new MenuItem("Open");
		menuItem.setOnAction(this);
		menu.getItems().add(menuItem);

		menuItem = new MenuItem("Save");
		menuItem.setOnAction(this);
		menu.getItems().add(menuItem);

		menu.getItems().add(new SeparatorMenuItem());

		menuItem = new MenuItem("Exit");
		menuItem.setOnAction(this);
		menu.getItems().add(menuItem);

		menuBar.getMenus().add(menu);

		return menuBar;
	}

	@Override
	public void handle(ActionEvent arg0) {
		String input = ((MenuItem) arg0.getSource()).getText();

		if (input.equals("Open")) {
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(this.stage);

			if (file != null) {
				System.out.println("Opening: " + file.getName() + "." + "\n");
				BufferedReader bufferedReader;

				try {
					bufferedReader = new BufferedReader(new FileReader(file));
					EmailModel em = new EmailModel();
					Parser parser = new Parser();
					if (parser.parse(bufferedReader, em)) {
						this.setModel(em);
					} else {
						// display a pop-up window
						AlertBox.display(parser.getErrorMessage());
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Command cancelled." + "\n");
			}
		} else if (input.equals("Exit")) {
			this.stage.close();
		}
	}
}
