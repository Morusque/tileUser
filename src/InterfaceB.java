import java.awt.Color;

import processing.core.PApplet;

public class InterfaceB extends Interface {

	public Tileset tileset;// the used tileset
	public Map map;// the map to be created
	public MappingStats stat;// the level stats
	int selectedTile;// Type to set tiles
	float tX = 10;// tile size X
	float tY = 10;// tile size Y
	int tMapX = 5;// map size x
	int tMapY = 5;// map size y
	int gotoI = 0;// 0=this, 1=InterfaceB

	public int getGotoI() {
		return gotoI;
	}

	public void setGotoI(int gotoI) {
		this.gotoI = gotoI;
	}

	public InterfaceB(TileUser p, InterfaceA if1) {
		super(p);
		tileset = if1.getTileset();
		stat = if1.getStat();
		map = new Map(p, tMapX, tMapY, if1.getMap().getNbIdent());
		map.setNbTypes(if1.getTileset().getnbTypes());
		map.setAllTypesTo(0);
		map.setAllIdentsTo(-1);
	}

	void setup() {
		parent.background(0);
		map.displayTypes(tX, tY);
		afficheBoutons();
	}

	void use() {
		if (parent.mousePressed) {
			if (fonctionSouris() == 1) {
				map.setTypeMouse((int) (parent.mouseX / tX),
						(int) (parent.mouseY / tY), selectedTile);
				parent.background(0);
				map.displayTypes(tX, tY);
				afficheBoutons();
			}
			if (fonctionSouris() == 2) {
				selectedTile = PApplet.floor((float) parent.mouseX
						* tileset.getnbTypes() / parent.width);
				parent.background(0);
				map.displayTypes(tX, tY);
				afficheBoutons();
			}
			if (fonctionSouris() == 3) {
				// then loads InterfaceC
				gotoI = 2;
				// generates a map
				map.generateTiles(stat, tileset);
			}
		}
	}

	private void afficheBoutons() {
		int nbT = tileset.getnbTypes();
		for (int i = 0; i < nbT; i++) {
			ColorPx c = new ColorPx(Color.HSBtoRGB((float) i / nbT, 1, 1));
			parent.stroke(c.getR(), c.getG(), c.getB());
			if (selectedTile == i) {
				parent.stroke(0xFF);
			}
			parent.fill(c.getR(), c.getG(), c.getB(), 127);
			parent.rect(i * parent.width / nbT, map.gety() * tY, tX, tY);
			parent.fill(255);
			float posX = i * parent.width / nbT;
			float posY = (map.gety() + 1) * tY;
			if (i == 0) {
				parent.text("any", posX, posY);
			} else if (i == 1) {
				parent.text("empty", posX, posY);
			} else if (i == 2) {
				parent.text("wall", posX, posY);
			} else if (i == 3) {
				parent.text("background", posX, posY);
			} else if (i == 4) {
				parent.text("other", posX, posY);
			}
		}
	}

	private int fonctionSouris() {
		// what will the mouse do if the user clicks
		// 1 = sets tiles on the map
		// 2 = select the tile type
		// 3 = ends
		if (parent.mouseY <= map.gety() * tY) {
			return 1;
		} else if (parent.mouseY <= (map.gety() + 1) * tY) {
			return 2;
		} else if (parent.mouseX > parent.width / 2) {
			return 3;
		} else {
			return 4;
		}
	}

	public void displayMapTiles() {
		parent.background(0);
		map.displayTiles(tileset);
	}
}
