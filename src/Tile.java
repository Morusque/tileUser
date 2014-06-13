import java.awt.Color;

import processing.core.PApplet;

public class Tile {
	PApplet parent;
	private int tS;// tile size
	int inst;// instance number
	int[][] col;// color of each pixel
	int type;// what kind of tile (wall, background, etc)

	Tile(PApplet p, int tS) {
		parent = p;
		this.tS = tS;
		this.col = new int[tS][tS];
		this.type = 0;
	}

	void setInstance(int inst) {
		this.inst = inst;
	}

	void loadTile(int[][] pxIndex, int xS, int yS) {
		for (int xI = 0; xI < tS; xI++) {
			for (int yI = 0; yI < tS; yI++) {
				col[xI][yI] = pxIndex[xS + xI][yS + yI];
			}
		}
	}

	boolean equals(Tile tile) {
		// is true if the tile is the same
		for (int xI = 0; xI < tS; xI++) {
			for (int yI = 0; yI < tS; yI++) {
				if (tile.getCol(xI, yI) != col[xI][yI]) {
					// if the pixel has a different color
					return false;
				}
			}
		}
		return true;
	}

	void display(int x, int y, Palette palette) {// displays the tile at x,y
		for (int xI = 0; xI < tS; xI++) {
			for (int yI = 0; yI < tS; yI++) {
				ColorPx col2 = palette.colorOf(col[xI][yI]);
				int a = col2.getA();
				int r = col2.getR();
				int g = col2.getG();
				int b = col2.getB();
				parent.stroke(r, g, b, a);
				parent.point(x + xI, y + yI);
			}
		}
	}

	public void displayType(int i, int j, int nbTypes) {
		if (type != 0) {
			ColorPx c = new ColorPx(Color.HSBtoRGB((float)type/nbTypes, 1, 1));
			parent.stroke(c.getR(),c.getG(),c.getB());
			parent.fill(c.getR(),c.getG(),c.getB(), 127);
			parent.rect(i, j, tS, tS);
		}
	}

	public int getCol(int x, int y) {
		return col[x][y];
	}

	int getInst() {
		return inst;
	}

	public void incrementType(int nbTypes) {
		type=(type+1)%nbTypes;
	}

	public void setType(int type2) {
		type=type2;
	}

	public int getType() {
		return type;
	}

}
