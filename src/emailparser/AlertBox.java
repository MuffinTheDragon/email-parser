package emailparser;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	/*
	 * This method displays a new pop-up window when the file parser receives an 
	 * invalid file.
	 */
	public static void display(String message) {
		Stage window = new Stage();
		window.setTitle("Alert! Can't Open File");
		window.setMinWidth(300);
		window.initModality(Modality.APPLICATION_MODAL); // block new user input 
		
		Button closeBtn = new Button("Ok");
		closeBtn.setOnAction(e -> window.close());
		
		Label errorMessage = new Label();
		errorMessage.setText(message);
		
		VBox layout = new VBox();
		layout.getChildren().addAll(errorMessage, closeBtn);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait(); // waits until the pop-up window is closed 
	}
}
