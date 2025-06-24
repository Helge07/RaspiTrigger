

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.StringConverter;

/**
 * Dieses Modell koordiniert die Einwirkungen eines Schiebereglers, einer
 * CheckBox und einer ChoiceBox auf eine Zeichenfläche.
 * Es werden die folgenden Eigenschaften bereitgestellt:
 *   Für den Schieberegler:
 *     getSliderValueProperty()          -- Strichbreite
 *   Für die Checkbox:
 *     getMiddleLineDisplayProperty()    -- für Anzeige der Mittellinie
 *   Für die ChoiceBox:
 *     getConverterProperty()            -- für die Anzeige der Namen der Strichenden
 *     getSelectedLineCapProperty()      -- für die ausgewählte Option
 * @author 
 */
public class ApplicationModel {
    
    private final SimpleDoubleProperty sliderValue
                  = new SimpleDoubleProperty(5.0);
    private final SimpleBooleanProperty drawMiddleLine
                  = new SimpleBooleanProperty(true);
    private final ObjectProperty<StringConverter<StrokeLineCap>>
            converterProperty
                  = new SimpleObjectProperty<>(new LineCapStringConverter());
    private final ObjectProperty<StrokeLineCap> selectedLineCapProperty
                  = new SimpleObjectProperty<>(StrokeLineCap.BUTT);
    
    public DoubleProperty getSliderValueProperty() {
        return sliderValue;
    }
    
    public BooleanProperty getMiddlelineDisplayProperty() {
        return drawMiddleLine;
    }
    
    public ObjectProperty<StringConverter<StrokeLineCap>>
           getConverterProperty() {
        return converterProperty;           
    }
    
    public ObjectProperty<StrokeLineCap> getSelectedLineCapProperty() {
        return selectedLineCapProperty;
    }
}
