import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

abstract class Interface {

	PApplet parent;
	PFont font;

	Interface(PApplet p) {
		parent = p;
		font = parent.loadFont(parent.dataPath("font.vlw"));
		parent.textFont(font);
		parent.textAlign(PConstants.LEFT);
	}

	abstract void setup();

	abstract void use();

}
