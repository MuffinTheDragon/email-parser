package emailparser;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	private EmailModel em;
	private View view;
	
	public static void main (String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		this.em = new EmailModel();
		this.view = new View(this.em, arg0);
		
		
	}

}
