

import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author
 */
public class ApplicationView  extends VBox {
    
    final private ApplicationModel model;
    public ApplicationView(ApplicationModel m) {
        model = m;
        this.setupWindow();
    }
    
    /**
     * Das Fenster wird mit einem Schieberegler, einer CheckBox,
     * einer ChoiceBox und einer Zeichenfläche ausgestattet.
     * Für die Platzierung der Darstellungselemente werden eine
     * HBox, eine VBox und eine weitere HBox verwendet.
     */
    private void setupWindow() {
        final int sliderWidth = 30;
        Slider slider = new Slider(0, 25, model.getSliderValueProperty().get());
        CheckBox cb = new CheckBox("Mittellinie");
        ChoiceBox<StrokeLineCap> lineCaps = new ChoiceBox<>();
        Insets buttonPaddings = new Insets(0.0, 0.0, 0.0, 5.0);  // top, right, bottom, left
        slider.setOrientation(Orientation.VERTICAL);
        slider.setMinWidth(sliderWidth);
        slider.setMaxWidth(sliderWidth);
        slider.valueProperty().bindBidirectional(model.getSliderValueProperty());
        cb.selectedProperty().bindBidirectional(model.getMiddlelineDisplayProperty());
        lineCaps.getItems().addAll(Arrays.asList(StrokeLineCap.values()));
        lineCaps.converterProperty().bind(model.getConverterProperty());
        lineCaps.valueProperty().bindBidirectional(model.getSelectedLineCapProperty());
       
        cb.setPadding(buttonPaddings);
        lineCaps.setPadding(buttonPaddings);
        cb.setMaxWidth(200000.0);
        lineCaps.setMaxWidth(200000.0);

        PaintArea paintRegion = new PaintArea();
        paintRegion.getLineWidthProperty().bind(model.getSliderValueProperty());
        paintRegion.getDisplayMiddleLineProperty().bind(model.getMiddlelineDisplayProperty());
        paintRegion.getLineCapProperty().bind(model.getSelectedLineCapProperty());
        HBox container = new HBox();
        VBox container2 = new VBox();
        HBox container3 = new HBox();
        container.getStylesheets()
                 .add(getClass().getResource("SliderStyle.css")
                                .toExternalForm());
        container3.getChildren().addAll(cb, lineCaps);
        container2.getChildren().addAll(container3, paintRegion);
        container.getChildren().add(slider);
        container.getChildren().add(container2);
        
       
        HBox.setHgrow(cb, Priority.ALWAYS);
        HBox.setHgrow(lineCaps, Priority.ALWAYS);
        VBox.setVgrow(container, Priority.ALWAYS);
        HBox.setHgrow(container2, Priority.ALWAYS);
        VBox.setVgrow(paintRegion, Priority.ALWAYS);
        this.getChildren().add(container);
    }
}
