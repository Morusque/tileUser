import java.util.Vector;
import processing.core.PApplet;

public class MSNeighbors {
	PApplet parent;

	// ident and occur are based on the same i
	Vector<Integer> ident = new Vector<Integer>();// identifiers of the
	// neighbors
	Vector<Integer> occur = new Vector<Integer>();// number of time it occurs

	MSNeighbors(PApplet p) {// Mapping statistics of the neighbors on one side
		parent = p;
	}

	public void addStat(int identIn) {
		boolean trouve = false;// is true if the tile in already occurs in stats
		for (int i = 0; i < ident.size(); i++) {
			if ((int) ident.get(i) == identIn) {
				trouve = true;
				occur.set(i, (occur.get(i) + 1));
			}
		}
		if (!trouve) {
			ident.add(identIn);
			occur.add(1);
		}
	}

	public int getident(int i) {
		return ident.get(i);
	}

	public int getoccur(int i) {
		return occur.get(i);
	}

	public boolean allows(int id) {
		// checks if ident id occurs somewhere
		for (int i = 0; i < ident.size(); i++) {
			if (ident.get(i) == id) {
				return true;
			}
		}
		return false;
	}
}
