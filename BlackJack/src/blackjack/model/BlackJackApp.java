package blackjack.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BlackJackApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation( BlackJackApp.class.getResource("view/fxml.fxml"));

			BorderPane root = loader.load();

			Scene scene = new Scene(root);
			primaryStage.setTitle("Blackjack Application");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			//System.out.println("Fichier CSS ou FXML non disponible");
			//System.exit(1);
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
