import java.awt.Color;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;

public class Map {
	PApplet parent;
	int x;
	int y;
	int[][] ident;// identifier of each tile
	int[][] type;// type of each tile
	int nbIdent;// number of different identifiers
	int nbTypes;// number of different types

	public int getNbIdent() {
		return nbIdent;
	}

	public void setNbIdent(int nbIdent) {
		this.nbIdent = nbIdent;
	}

	Map(PApplet p, int x, int y, int nbIdent) {
		this.x = x;
		this.y = y;
		parent = p;
		ident = new int[x][y];
		this.nbIdent = nbIdent;
		type = new int[x][y];
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				type[xI][yI] = -1;
			}
		}
	}

	public void setIdent(int x, int y, int i) {
		ident[x][y] = i;
	}

	public void outputTxt(String filePath) {
		String[] output = new String[y];
		for (int yI = 0; yI < y; yI++) {
			output[yI] = "";
			for (int xI = 0; xI < x; xI++) {
				output[yI] += PApplet.str(ident[xI][yI]);
				output[yI] += "\t";
			}
		}
		parent.saveStrings(filePath+"mapOutput.txt", output);
	}

	public void outputPng(String filePath) {
		ColorPx[] col = new ColorPx[nbIdent];
		int maxColor = 0x00FFFFFF;
		for (int i = 0; i < nbIdent; i++) {
			col[i] = new ColorPx((int) (i * maxColor / nbIdent));
		}
		PImage map = new PImage(x, y);
		for (int yI = 0; yI < y; yI++) {
			for (int xI = 0; xI < x; xI++) {
				map.set(xI, yI, col[ident[xI][yI]].toInt());
			}
		}
		map.save(filePath+"map.png");
	}

	MappingStats generateStats() {
		MappingStats stats = new MappingStats(parent);
		// for each tile on the map
		for (int yI = 0; yI < y; yI++) {
			for (int xI = 0; xI < x; xI++) {
				int[] neigh = new int[8];// neighbors of the tile
				neigh[0] = tileIdentCheck(xI - 1, yI - 1);
				neigh[1] = tileIdentCheck(xI + 0, yI - 1);
				neigh[2] = tileIdentCheck(xI + 1, yI - 1);
				neigh[3] = tileIdentCheck(xI - 1, yI + 0);
				neigh[4] = tileIdentCheck(xI + 1, yI + 0);
				neigh[5] = tileIdentCheck(xI - 1, yI + 1);
				neigh[6] = tileIdentCheck(xI + 0, yI + 1);
				neigh[7] = tileIdentCheck(xI + 1, yI + 1);
				stats.tileIn(ident[xI][yI], neigh);
			}
		}
		return stats;
	}

	private int tileIdentCheck(int xC, int yC) {
		if (xC >= 0 && xC < x && yC >= 0 && yC < y) {
			return ident[xC][yC];
		} else {
			return -1;
		}
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public int getident(int x, int y) {
		return ident[x][y];
	}

	public void shift(int[] toShift) {
		for (int i = 0; i < toShift.length; i++) {
			nbIdent--;
			for (int yI = 0; yI < y; yI++) {
				for (int xI = 0; xI < x; xI++) {
					if (ident[xI][yI] > toShift[i]) {
						ident[xI][yI]--;
					} else if (ident[xI][yI] == toShift[i]) {
						ident[xI][yI] = -1;
					}
				}
			}
		}
	}

	public void displayTypes(float tx, float ty) {
		// displays a map in form of colored squares
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				ColorPx c = new ColorPx(Color.HSBtoRGB((float) type[xI][yI]
						/ nbTypes, 1, 1));
				parent.stroke(c.getR(), c.getG(), c.getB());
				parent.fill(c.getR(), c.getG(), c.getB(), 127);
				parent.rect(xI * tx, yI * ty, tx, ty);
			}
		}
	}

	public int getNbTypes() {
		return nbTypes;
	}

	public void setNbTypes(int nbTypes) {
		this.nbTypes = nbTypes;
	}

	public void setTypeMouse(int f, int g, int sType) {
		if (f >= 0 && f < x && g >= 0 && g < y) {
			type[f][g] = sType;
		}
	}

	public void generateTiles(MappingStats stat, Tileset tileset) {
		// generates a map based on tile types and stats
		// first resets all tiles identifiers
		setAllIdentsTo(-1);

		// first, set a list of possible idents for each tiles
		// only based on the type requested
		int[][][] possibleId = new int[x][y][];

		// define the possible ids
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				possibleId[xI][yI] = tileset.possibleIdentsFor(type[xI][yI]);
			}
		}

		// then, test every possibility
		// and choose the ones that have the least disturbance in it

		// stores only the better sets
		Vector<int[][]> betterSets = new Vector<int[][]>();
		int lessDisturbance = -1;

		// stores the current set of indexes of possibleId[][]
		int[][] idTest = new int[x][y];
		// sets all tiles to the first possibility
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				idTest[xI][yI] = 0;
			}
		}

		// the number of possible sets
		int nbTotalSets = nbSets(possibleId);
		int currentTested = 0;

		while (!allPossibleSetsStudyied(idTest, possibleId)
				|| betterSets.size() == 0) {
			// prints the current situation
			currentTested++;
			System.out.println((float) currentTested / (float) nbTotalSets
					* 100 + " %");
			// idTest contains the currently studied possibility;
			idTest = nextPossibleSet(idTest, possibleId);
			int currentDisturbance = nbOfDisturbanceIn(idTest, possibleId,
					stat, lessDisturbance);
			if (currentDisturbance < lessDisturbance || betterSets.size() == 0) {
				lessDisturbance = currentDisturbance;
				betterSets.clear();
				betterSets.add(indexToValues(idTest, possibleId));
				// picked
			} else if (currentDisturbance == lessDisturbance) {
				betterSets.add(indexToValues(idTest, possibleId));
				// added
			} else {
				// rejected
			}
		}

		// TODO
		//
		//
		//
		// then choose the one that matches best the statistics
		//

		// but for the moment, only pick the last occurrence in the list
		System.out.println(betterSets.size());
		ident = betterSets.get(betterSets.size() - 1);

	}

	private int nbSets(int[][][] possibleId) {
		int nbS = 1;
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				nbS *= possibleId[xI][yI].length;
			}
		}
		return nbS;
	}

	private int[][] indexToValues(int[][] indexes, int[][][] possibleId) {
		int[][] idents = new int[indexes.length][indexes[0].length];
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				idents[xI][yI] = possibleId[xI][yI][indexes[xI][yI]];
			}
		}
		return idents;
	}

	private int nbOfDisturbanceIn(int[][] idTest, int[][][] possibleId,
			MappingStats stat, int lessDisturbance) {
		// first convert the possibleId indexes to values
		idTest = indexToValues(idTest, possibleId);
		int nbDisturbance = 0;
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				int[][] neighbors = new int[3][3];
				for (int xI2 = 0; xI2 < 3; xI2++) {
					for (int yI2 = 0; yI2 < 3; yI2++) {
						int curX = xI + xI2 - 1;
						int curY = yI + yI2 - 1;
						// if it's inside the map
						if (curX >= 0 && curX < x && curY >= 0 && curY < y) {
							neighbors[xI2][yI2] = idTest[curX][curY];
						} else {
							neighbors[xI2][yI2] = -1;
						}
					}
				}
				nbDisturbance += stat.nbDisturbanceIn(neighbors);
				// return lessDisturbance + 1
				// if it's already too wrong
				if (nbDisturbance > lessDisturbance) {
					return lessDisturbance + 1;
				}
			}
		}
		return nbDisturbance;
	}

	private int[][] nextPossibleSet(int[][] idTest, int[][][] possibleId) {
		boolean breakHere = false;
		for (int xI = 0; xI < x && !breakHere; xI++) {
			for (int yI = 0; yI < y && !breakHere; yI++) {
				if (idTest[xI][yI] < possibleId[xI][yI].length - 1) {
					idTest[xI][yI]++;
					breakHere = true;
					// break here
				} else {
					idTest[xI][yI] = 0;
					// and continue
				}
			}
		}
		return idTest;
	}

	private boolean allPossibleSetsStudyied(int[][] idTest, int[][][] possibleId) {
		// returns true if all the indexes in idTest[][]
		// are the bottom of possibleId[][]
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				if (idTest[xI][yI] < possibleId[xI][yI].length - 1) {
					return false;
				}
			}
		}
		return true;
	}

	public void displayTiles(Tileset tileset) {
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				int tS = (int) tileset.gettS();
				tileset.displayTile(ident[xI][yI], xI * tS, yI * tS);
			}
		}
	}

	public void setAllTypesTo(int t) {
		// sets all tiles type to t
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				type[xI][yI] = t;
			}
		}
	}

	public void setAllIdentsTo(int id) {
		// sets all tiles idents to id
		for (int xI = 0; xI < x; xI++) {
			for (int yI = 0; yI < y; yI++) {
				ident[xI][yI] = id;
			}
		}
	}
}
