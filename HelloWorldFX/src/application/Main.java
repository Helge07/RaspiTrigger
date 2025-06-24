package application;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public  class Main extends Application {
	
	  @Override
	  public void start(Stage primaryStage) {
      primaryStage.setTitle("My First JavaFX App");

      Label label = new Label("Hello World, JavaFX !");
      label.setAlignment(javafx.geometry.Pos.CENTER);
      Scene scene = new Scene(label, 400, 150);
      primaryStage.setScene(scene);

      primaryStage.show();
  }

    public static void main(String[] args) {
        launch(args);
    }
}