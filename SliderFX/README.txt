FXSliderGraphicsDemo04

Kurzbeschreibung:
-----------------

Das Beispiel zeigt ein Fenster, das mit einem Schieberegler, einer
Checkbox, einer Choicebox und einer Zeichengläche ausgestattet ist.

Mit Sieberegler, Checkbox und Choicebox können verschiedene
Eigenschaften des in der Zeichenfläche gezeichneten Strichs
eingestellt werden.

Bei der Strukturierung des Programms wurde Wert auf eine klare Trennung
von Datenmodell und Ansicht gelegt.


Erforderliche Software:
-----------------------

   sudo apt-get install openjdk-11-jdk
   sudo apt-get install ant
   sudo apt-get install openjfx

======================================================================

Verwendung von ant:
-------------------

Zu diesem Projekt gehört die Datei build.xml, die zur Verwendung mit
Apache ant bestimmt ist.  Apache ant automatisiert die Compilierung
und die Ausführung von Java-Programmen sowie die Herstellung einer
jar-Datei.

     ant help     
         Ausgabe dieser Anleitung.
     ant compile 
         Aufruf des Java Compilers.
     ant run
         Ausführung des compilierten Programms. Falls
         erforderlich wird vorher compiliert. Eine jar-Datei
         wird weder benötigt noch verwendet.
     ant jar
         Herstellung einer jar-Datei. Falls erforderlich
         wird vorher compiliert.
     ant run-jar
         Ausführung des als jar-Datei bereitgestellten
         Programms. Falls erforderlich wird vorher
         compiliert und eine jar-Datei erzeugt.
     ant clean
         Löschung aller vom Compiler erzeugten Klassen
         und Löschung der jar-Datei.
     ant distclean
         Löschung aller vom Compiler erzeugten Dateien.

Es wird geraten, ant mindestens für die Compilierung der Quellen
und für die Herstellung der jar-Datei tatsächlich zu verwenden.

===================================================================

Programmausführung:
-------------------

Für die Programmausführung ohne Verwendung von ant ist im Projektverzeichnis
ein Befehlszeilenfesnter zu öffnen.

Die Ausführung der compilierten Klassen geschieht mit:

  java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.graphics,javafx.fxml -classpath build/classes Main

Die Ausführung der jar-Datei geschieht mit:

  java -jar ./build/jar/demo.jar

 
geschrieben:         2020-11-04
letzmalig geändert:  2020-11-06
