import processing.core.PApplet;

public class ShotSetting {
	int offsetX, offsetY;// position of the first 0 on the level
	int nbTiles;// number of tiles
	int tS;// tile size
	int totalPixels;// total amount of pixels in the resulting tileset
	int totalData;// pixels in the tileset + tiles in the map

	ShotSetting(int offsetX, int offsetY, int nbTiles, int tS, int width,
			int height) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.nbTiles = nbTiles;
		this.tS = tS;
		this.totalPixels = (int) (nbTiles * PApplet.sq(tS));
		int nbTX = ((width - offsetX) / tS);
		int nbTY = ((height - offsetY) / tS);
		this.totalData = totalPixels + (nbTX * nbTY);
	}

	ShotSetting(int totalData) {
		this.totalData = totalData;
		this.totalPixels = totalData;
	}

	int getTotalPixels() {
		return totalPixels;
	}

	int getoffsetX() {
		return offsetX;
	}

	int getoffsetY() {
		return offsetY;
	}

	int gettS() {
		return tS;
	}

	public boolean getvalid() {
		boolean valid = true;
		if (nbTiles == 0) {
			valid = false;
		}
		return valid;
	}

	public int getTotalData() {
		return totalData;
	}
}
