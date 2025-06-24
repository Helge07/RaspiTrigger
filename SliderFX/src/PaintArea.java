

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;

/**
 * Die Klasse  PaintArea  ergänzt eine  Canvas  um Hilfsmittel für
 * die Größenveränderlichkeit sowie um die Möglichkeit, auf
 * Änderungen der Werte von Eigenschaften des application models
 * zu reagieren.
 * @author Projekte
 */
public class PaintArea extends Canvas implements ChangeListener <Object> {
    /**
     *  Diese Eigenschaften werden bei der Ausstattung des
     *  Fensters (in ApplicationView>>setupWindow) mit
     *  Eigenschaften des application modell verbunden.
     *  Eine Instanz von PantArea wird dadurch in die Lage
     *  versetzt, auf Eigenschaften des appliction models
     *  zuzugreifen.
     */
    private final SimpleDoubleProperty lineWidthProperty;
    private final SimpleBooleanProperty showMiddleLine;
    private final SimpleObjectProperty<StrokeLineCap> lineCapToUse;
    
    public PaintArea () {
        lineWidthProperty = new SimpleDoubleProperty();
        showMiddleLine = new SimpleBooleanProperty();
        lineCapToUse = new SimpleObjectProperty<>();
        this.configureInstance();
    }
    
    public SimpleDoubleProperty getLineWidthProperty() {
        return lineWidthProperty;
    }
    
    public SimpleBooleanProperty getDisplayMiddleLineProperty() {
        return showMiddleLine;
    }
    
    public SimpleObjectProperty<StrokeLineCap> getLineCapProperty() {
        return lineCapToUse;
    }
    private void configureInstance() {
        lineWidthProperty.addListener(this);
    }

    /**
     *  Die Methode zeichnet einen roten Strich von der linken
     *  oberen Ecke des Widgets in die rechte untere Ecke.
     *  Die Strichbreite und die Art der Strichenden werden
     *  aus Eigenschaften des application models übernommen.
     *
     *  Die Konstante  delta  wird für die Berechnung der
     *  Endpunkte des Strichs verwendet.
     *  Der Strich verbindet die Punkte (delta, delta)
     *  und  (width - delta, height - delta).  Durch diese
     *  Wahl der Endpunkt bleibt auch dann genug Platz für
     *  Darstellung der Strichenden, wenn mit einem breiten
     *  Strich gezeichnet wird.
     */
    public void paint(){
        final double delta = 20.0;
        GraphicsContext gc = this.getGraphicsContext2D();
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        gc.setStroke(Color.RED);
        gc.setLineWidth(lineWidthProperty.get());
        gc.setLineCap(lineCapToUse.get());
        gc.strokeLine(delta + 0.5, delta + 0.5,
                      canvasWidth - delta, canvasHeight - delta);
        if (showMiddleLine.get() && lineWidthProperty.get() > 3.0) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.0);
            gc.strokeLine(delta, delta,
                          canvasWidth - delta, canvasHeight - delta);
        }
    }


    /**
     * Die Methode wird gesendet, wenn sich der Wert des
     * Schiebereglers verändert hat , mit dem die Strichbreite
     * eingestellt wird.
     * Die Methode wird nur gesendet, weil sich die Instanz
     * von PaintArea als Listener der Eigenschaft
     * lineWidthProperty  registriert hat.
     */
    @Override
    public void changed(ObservableValue<? extends Object> observable,
                        Object oldValue,
                        Object newValue) {
        this.paint();
    }
    
    /** Canvas wird als ein in der Groesse unveraenderliches Bild
      * betrachtet. Die folgenden ueberschriebenen Methoden erlauben
      * dem Layoutmanager eine Groessenaenderung.
      * @return Immer <code>true</code>, weil die Groessenaenderung immer
      * 	    erlaubt ist.
      */
    @Override
    public boolean isResizable() {
            return true;
    }

    @Override
    public void resize(double width, double height) {
            super.resize(width, height);
            setWidth(width);
            setHeight(height);
            paint();
    }

    @Override
    public double maxHeight(double arg0) {
            return Double.MAX_VALUE;
    }

    @Override
    public double maxWidth(double arg0) {
            return Double.MAX_VALUE;
    }

    @Override
    public double minHeight(double arg0) {
            return 1;
    }

    @Override
    public double minWidth(double arg0) {
            return 1;
    }
}
