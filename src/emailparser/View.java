package emailparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This module is the main view module and creates the visualization after 
 * parsing an e-mail.
 * 
 * User has the ability to write an e-mail, save, and load.
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
	
	private TextArea addressToArea;
	private TextArea addressFromArea;
	private TextArea subjectArea;
	private TextArea bodyArea;
	

	public View(EmailModel model, Stage stage) {
		this.addressTo = new Label("To: ");
		this.addressFrom = new Label("From: ");
		this.subject = new Label("Subject: ");
		this.body = new Label("Body: ");
		
		// create instances for text area and assign any preferred properties
		this.addressToArea = new TextArea();
		this.addressToArea.setPrefRowCount(1);
		
		this.addressFromArea = new TextArea();
		this.addressFromArea.setPrefRowCount(1);
		
		this.subjectArea = new TextArea();
		this.subjectArea.setPrefRowCount(1);
		
		this.bodyArea= new TextArea();
		this.bodyArea.setWrapText(true);

		this.model = model;
		this.stage = stage;
		stageView(stage);
	}

	private void setModel(EmailModel em) {
		this.model = em;
		rewrite();
	}
	
	/**
	 * Write the contents of an e-mail in the text area's.
	 */
	private void rewrite() {
		String[] info = this.model.getInfo();

		// text areas stay editable to represent "draft" functionality
		this.addressFromArea.setText(info[0]);
		this.addressToArea.setText(info[1]);
		this.subjectArea.setText(info[2]);
		this.bodyArea.setText(info[3]);
		
	}
	
	/**
	 * Initialize the stage and add all view components 
	 */
	private void stageView(Stage stage) {
		VBox layout = new VBox(10);

		HBox addressTo = new HBox();
		addressTo.getChildren().addAll(this.addressTo, this.addressToArea);
		addressTo.setAlignment(Pos.TOP_LEFT);

		HBox addressFrom = new HBox();
		addressFrom.getChildren().addAll(this.addressFrom, this.addressFromArea);
		addressFrom.setAlignment(Pos.TOP_LEFT);

		HBox subject = new HBox();
		subject.getChildren().addAll(this.subject, this.subjectArea);
		subject.setAlignment(Pos.TOP_LEFT);

		HBox body = new HBox();
		body.getChildren().addAll(this.body, this.bodyArea);
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
		} else if (input.equals("Save")) {
			FileChooser fc = new FileChooser();
			File file = fc.showSaveDialog(this.stage);

			if (file != null) {
				this.model.setAddressFrom(this.addressFromArea.getText());
				this.model.setAddressTo(this.addressToArea.getText());
				this.model.setSubject(this.subjectArea.getText());
				this.model.setBody(this.bodyArea.getText());
				System.out.println("Saving: " + file.getName() + "." + "\n");
				PrintWriter writer;
				try {
					writer = new PrintWriter(file);
					View.save(writer, this.model);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Save cancelled." + "\n");
			}
			
		} else if (input.equals("Exit")) {
			this.stage.close();
		}
	}
	
	/**
	 * Save the contents stored in the current text area's to the open file 
	 * whenever user hits save.
	 * 
	 */
	private static void save(PrintWriter writer, EmailModel em) {
		String[] info = em.getInfo();
		
		writer.write("StartEmail" + "\n\n");
		writer.write("To: " + info[0] + "\n");
		writer.write("From: " + info[1] + "\n");
		writer.write("\n\n");
		writer.write("Subject: " + info[2] + "\n");
		writer.write("\t" + info[3] + "\n\n");
		
		writer.write("EndBody" + "\n\n");
		writer.write("EndEmail" + "\n");
		
		writer.close();
		
	}
}
