import java.util.Vector;
import processing.core.PApplet;

public class MappingStats {
	PApplet parent;

	Vector<MSTile> tiles = new Vector<MSTile>();

	// list of all the tiles and their neighbors, and stats about it
	MappingStats(PApplet p) {
		parent = p;
	}

	public void tileIn(int id, int[] neigh) {
		boolean trouve = false;
		// is true if the tile already exists on the stats
		for (int i = 0; i < tiles.size() && !trouve; i++) {
			if (tiles.get(i).getIdent() == id) {
				trouve = true;
				MSTile tile = tiles.get(i);
				tile.addNeighbors(neigh);
				tiles.set(i, tile);
			}
		}
		if (!trouve) {
			MSTile tile = new MSTile(parent, id);
			tile.addNeighbors(neigh);
			tiles.add(tile);
		}
	}

	public void outputTxt(String filePath) {
		Vector<String> output = new Vector<String>();
		for (int i = 0; i < tiles.size(); i++) {
			output.add("!");
			output.add(String.valueOf(tiles.get(i).getIdent()));
			output.add(String.valueOf(tiles.get(i).getOccur()));
		}
		String[] output2 = new String[output.size()];
		for (int i = 0; i < output.size(); i++) {
			output2[i] = (String) output.get(i);
		}
		parent.saveStrings(filePath + "statOutput.txt", output2);
	}

	public int nbDisturbanceIn(int[][] neighbors) {
		// one disturbance for each tile ident
		// that doesn't fit with the central one
		int dist = 0;
		// don't check empty tiles
		if (neighbors[1][1] != -1) {
			for (int xI2 = 0; xI2 < 3; xI2++) {
				for (int yI2 = 0; yI2 < 3; yI2++) {
					// don't check the tile itself
					if (xI2 != 1 || yI2 != 1) {
						// don't check empty tiles
						if (neighbors[xI2][yI2] != -1) {
							if (!tiles.get(neighbors[1][1]).allowsIdAt(
									neighbors[xI2][yI2], xI2, yI2)) {
								dist++;
							}
						}
					}
				}
			}
		}
		return dist;
	}
}

class TileFinderReport {
	boolean[][] allowed = new boolean[3][3];
	int ident;

	public TileFinderReport(int i) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				allowed[x][y] = true;
			}
		}
		this.ident = i;
	}

	public TileFinderReport() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				allowed[x][y] = true;
			}
		}
	}

	public boolean[][] getAllowed() {
		return allowed;
	}

	public void setAllowed(boolean[][] allowed) {
		this.allowed = allowed;
	}

	public int getIdent() {
		return ident;
	}

	public void setIdent(int ident) {
		this.ident = ident;
	}

	public boolean correct() {
		boolean correct = true;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				correct = correct && allowed[x][y];
			}
		}
		return correct;
	}

}
