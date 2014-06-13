import processing.core.PApplet;

public class MSTile {
	PApplet parent;

	MSNeighbors[][] neigh;// immediate neighbors

	// ident[1][1] is the identifier of the tile itself
	// occur[1][1] is the occurrences of the tile itself

	// 0,0 1,0 2,0
	// 0,1 1,1 2,1
	// 0,2 1,2 2,2

	MSTile(PApplet p, int id) {// Mapping statistics of one tile
		parent = p;
		neigh = new MSNeighbors[3][3];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				neigh[x][y] = new MSNeighbors(parent);
			}
		}
		neigh[1][1].addStat(id);
	}

	public void addNeighbors(int[] neigh2) {
		neigh[0][0].addStat(neigh2[0]);
		neigh[1][0].addStat(neigh2[1]);
		neigh[2][0].addStat(neigh2[2]);
		neigh[0][1].addStat(neigh2[3]);
		neigh[1][1].addStat(neigh[1][1].getident(0));
		neigh[2][1].addStat(neigh2[4]);
		neigh[0][2].addStat(neigh2[5]);
		neigh[1][2].addStat(neigh2[6]);
		neigh[2][2].addStat(neigh2[7]);
	}

	public int getIdent() {
		return this.neigh[1][1].getident(0);
	}

	public int getOccur() {
		return this.neigh[1][1].getoccur(0);
	}

	public boolean allowsIdAt(int i, int x, int y) {
		// returns true if the ident i exists at x y
		if (neigh[x][y].allows(i)) {
			return true;
		} else {
			return false;
		}
	}
}
