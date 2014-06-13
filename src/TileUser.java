import processing.core.*;

public class TileUser extends PApplet {
	private static final long serialVersionUID = 1L;

	int phase = 0;// 0=A 1=B
	InterfaceA iA;
	InterfaceB iB;

	public void setup() {
		size(400, 400);
		iA = new InterfaceA(this);
		iA.setup();
	}

	public void draw() {
		if (phase == 0) {
			iA.use();
			if (iA.getGotoI() == 1) {
				iB = new InterfaceB(this, iA);
				iB.setup();
				phase = 1;
			}
		}
		if (phase == 1) {
			iB.use();
		}
	}

	public void keyPressed() {
		iB.displayMapTiles();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "TileUser" });
	}

}
