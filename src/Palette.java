import java.util.Vector;
import processing.core.PImage;

public class Palette {

	Vector<ColorPx> c = new Vector<ColorPx>();

	Palette() {

	}

	public void loadPalette(PImage im) {
		for (int x = 0; x < im.width; x++) {
			for (int y = 0; y < im.height; y++) {
				check(new ColorPx(im.get(x, y)));
			}
		}
	}

	private void check(ColorPx col) {
		// checks if a color exists and add it if not
		boolean trouve = false;
		for (int i = 0; i < c.size(); i++) {
			if (c.get(i).equals(col)) {
				trouve = true;
			}
		}
		if (!trouve) {
			c.add(col);
		}
	}

	public int[][] pictureToIndexes(PImage im) {
		int[][] pxIndex = new int[im.width][im.height];
		for (int x = 0; x < im.width; x++) {
			for (int y = 0; y < im.height; y++) {
				pxIndex[x][y] = indexOf(new ColorPx(im.get(x, y)));
			}
		}
		return pxIndex;
	}

	private int indexOf(ColorPx colorPx) {
		for (int i = 0; i < c.size(); i++) {
			if (c.get(i).equals(colorPx)) {
				return i;
			}
		}
		return -1;
	}

	public ColorPx colorOf(int col) {
		return c.get(col);
	}
}
