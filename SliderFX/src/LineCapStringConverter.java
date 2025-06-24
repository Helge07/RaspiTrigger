

import javafx.scene.shape.StrokeLineCap;
import javafx.util.StringConverter;

/**
 * Dieser Converter wird für in ApplicationModel für die Versorgung der
 * ChoiceBox verwendet, mit der die Art der Strichenden ausgewählt wird.
 * @author
 */
public class LineCapStringConverter extends StringConverter<StrokeLineCap> {

    @Override
    public String toString(StrokeLineCap id) {
        return id.name();
    }

    @Override
    public StrokeLineCap fromString(String string) {
        return StrokeLineCap.valueOf(string);
    }
    
}
