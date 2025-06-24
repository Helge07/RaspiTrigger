package application;
	
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static java.lang.Math.random; 




public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400, Color.BLUE);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			Group circles = new Group();
			for (int i = 0; i < 35; i++) {
			   Circle circle = new Circle(150, Color.web("white", 0.25));
			   circle.setStrokeType(StrokeType.OUTSIDE);
			   circle.setStroke(Color.web("white", 0.06));
			   circle.setStrokeWidth(0.01);
			   circles.getChildren().add(circle);
			}
			Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight());
							colors.widthProperty().bind(scene.widthProperty());
							colors.heightProperty().bind(scene.heightProperty());
							Group blendModeGroup = 
								    new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
								        Color.CADETBLUE), circles), colors);
								colors.setBlendMode(BlendMode.COLOR_DODGE);
								root.getChildren().add(blendModeGroup);
							circles.setEffect(new BoxBlur(10, 10, 5));
							
							Timeline timeline = new Timeline();
							for (Node circle: circles.getChildren()) {
							    timeline.getKeyFrames().addAll(
							        new KeyFrame(Duration.ZERO, // set start position at 0
							            new KeyValue(circle.translateXProperty(), random() * 800),
							            new KeyValue(circle.translateYProperty(), random() * 600)
							        ),
							        new KeyFrame(new Duration(4000), // set end position at 40s
							            new KeyValue(circle.translateXProperty(), random() * 800),
							            new KeyValue(circle.translateYProperty(), random() * 600)
							        )
							    );
							}
							// play 40s of animation
							timeline.play();
							
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
