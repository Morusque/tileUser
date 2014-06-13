import java.util.Vector;
import processing.core.PApplet;
import processing.core.PImage;

public class Tileset {
	PApplet parent;
	Vector<Tile> tiles = new Vector<Tile>();
	int tS;
	Palette palette;
	int nbTypes;// number of different tile types

	Tileset(PApplet p) {
		parent = p;
		this.nbTypes = 5;
	}

	void addTiles(Tile[] tilesIn, int tS, Palette palette) {
		this.palette = palette;
		for (int i = 0; i < tilesIn.length; i++) {
			tiles.add(tilesIn[i]);
		}
		this.tS = tS;// size of one tile
	}

	void display() {
		int nbTX = parent.width / tS;// number of tiles in one line
		for (int i = 0; i < tiles.size(); i++) {
			int x = (i % nbTX) * tS;
			int y = (i / nbTX) * tS;
			Tile tile = (Tile) tiles.get(i);
			tile.display(x, y, palette);
		}
	}

	PImage display(int w, int h) {
		PImage display = new PImage(w, h);
		int nbTX = w / tS;// number of tiles in one line
		for (int i = 0; i < tiles.size(); i++) {
			// if the tile identifier isn't -1
			if (i != -1) {
				int x = (i % nbTX) * tS;
				int y = (i / nbTX) * tS;
				for (int xI = 0; xI < tS; xI++) {
					for (int yI = 0; yI < tS; yI++) {
						Tile tile = (Tile) tiles.get(i);
						int col = (palette.colorOf(tile.getCol(xI, yI)))
								.toInt();
						display.set(xI + x, yI + y, col);
					}
				}
			}
		}
		return display;
	}

	public int searchInstanceOf(Tile tile) {
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile2 = (Tile) tiles.get(i);
			if (tile.equals(tile2)) {
				return i;
			}
		}
		return -1;
	}

	void outputFile(String filePath) {
		PImage output = display(parent.width, parent.height);
		output.save(filePath+"Tileset.png");
	}

	int getnbTiles() {
		return tiles.size();
	}

	public void displayTypesMap(Map map) {
		// displays the level for the user to set types on the tiles
		for (int x = 0; x < map.getx(); x++) {
			for (int y = 0; y < map.gety(); y++) {
				if (map.getident(x, y) != -1) {
					tiles.get(map.getident(x, y)).display(x * tS, y * tS,
							palette);
					tiles.get(map.getident(x, y)).displayType(x * tS, y * tS,
							nbTypes);
				}
			}
		}
	}

	public void setTypeMouse(int mouseX, int mouseY, Map map, int type) {
		int x = PApplet.constrain(mouseX / tS, 0, map.getx() - 1);
		int y = PApplet.constrain(mouseY / tS, 0, map.gety() - 1);
		tiles.get(map.getident(x, y)).setType(type);
	}

	public int getnbTypes() {
		return nbTypes;
	}

	public float gettS() {
		return tS;
	}

	public int[] deleteInvalid() {
		// deletes invalid tiles
		Vector<Integer> toShift = new Vector<Integer>();
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getType() == 1) {
				tiles.remove(i);
				toShift.add(i);
				i--;
			}
		}
		int[] toShift2 = new int[toShift.size()];
		for (int i = 0; i < toShift.size(); i++) {
			toShift2[i] = toShift.get(i);
		}
		return toShift2;
	}

	public void shiftTypes(int sh) {
		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).setType(tiles.get(i).getType() - sh);
		}
		nbTypes -= sh;
	}

	public int getTypeOfTile(int ident) {
		if (ident != -1) {
			return tiles.get(ident).getType();
		} else {
			// if the tile is -1
			// returns "empty"
			return 1;
		}
	}

	public void displayTile(int id, int x, int y) {
		// displays a tile of the id at x y
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).inst == id) {
				tiles.get(id).display(x, y, palette);
			}
		}
	}

	public int[] possibleIdentsFor(int type) {
		// make a list of identifier that matches the type requested
		Vector<Integer> possible = new Vector<Integer>();
		for (int i = 0; i < tiles.size(); i++) {
			// if the type matches
			// or the requested type is "any"
			if (tiles.get(i).getType() == type || type == 0) {
				possible.add(i);
			}
		}
		// if no tile matches, then set the possibility of -1
		if (possible.size() == 0) {
			possible.add(-1);
		}
		int[] possible2 = new int[possible.size()];
		for (int i = 0; i < possible.size(); i++) {
			possible2[i] = possible.get(i);
		}
		return possible2;
	}
}
