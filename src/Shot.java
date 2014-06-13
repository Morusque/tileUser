import processing.core.PApplet;
import processing.core.PImage;
import java.util.Vector;

public class Shot {
	PApplet parent;
	int tS;// tile size
	PImage im;// the image to parse
	int nbTX;// number of horizontal tiles
	int nbTY;// number of vertical tiles
	int offsetX;
	int offsetY;
	Vector<ShotSetting> listeSettings = new Vector<ShotSetting>();
	Palette palette;
	int[][] pxIndex;// index of the pixels, based on the palette

	Shot(PApplet p, String file) {
		parent = p;
		this.im = parent.loadImage(file);
		// the following sets default values that may be wrong
		// do investigateSetting() to correct it
		this.tS = 16;
		this.offsetX = 0;
		this.offsetY = 0;
		this.nbTX = ((im.width - offsetX) / tS);
		this.nbTY = ((im.height - offsetY) / tS);
		this.palette = new Palette();
		this.palette.loadPalette(im);
		this.pxIndex = palette.pictureToIndexes(im);
	}

	Shot(PApplet p, String file, int offsetX, int offsetY, int tS) {
		parent = p;
		this.tS = tS;
		im = parent.loadImage(file);
		// use this constructor in case
		// you already know the settings
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.nbTX = ((im.width - offsetX) / tS);
		this.nbTY = ((im.height - offsetY) / tS);
		this.palette = new Palette();
		this.palette.loadPalette(im);
		this.pxIndex = palette.pictureToIndexes(im);
	}

	void setTileSize(int tS) {
		this.tS = tS;
	}

	Map generateMap(Tileset tileset) {
		Map map = new Map(parent, nbTX, nbTY, tileset.getnbTiles());
		for (int y = 0; y < nbTY; y++) {
			for (int x = 0; x < nbTX; x++) {
				map.setIdent(x, y, tileset.searchInstanceOf(getTile(x * tS
						+ offsetX, y * tS + offsetY, tS)));
			}
		}
		return map;
	}

	Tile getTile(int x, int y, int s) {
		Tile tile = new Tile(parent, s);
		tile.loadTile(pxIndex, x, y);
		return tile;
	}

	Tile[] getTileLists() {
		return getTileLists(offsetX, offsetY);
	}

	Tile[] getTileLists(int offsetX, int offsetY) {
		return getTileLists(offsetX, offsetY, tS, -1, -1);
	}

	Tile[] getTileLists(int offsetX, int offsetY, int tS, int maxiT, int maxiD) {
		// maxiT and maxiP = -& if not used
		int nbTX = ((im.width - offsetX) / tS);
		int nbTY = ((im.height - offsetY) / tS);
		int nbTMap = nbTX * nbTY;
		Vector<Tile> tiles = new Vector<Tile>();
		for (int y = 0; y < nbTY; y++) {
			for (int x = 0; x < nbTX; x++) {
				Tile tile = getTile(x * tS + offsetX, y * tS + offsetY, tS);
				boolean existe = false;// check if it already exists
				for (int i = 0; i < tiles.size(); i++) {
					if (tile.equals((Tile) tiles.get(i))) {
						existe = true;
					}
				}
				if (!existe) {
					tiles.add(tile);
					if (maxiT != -1 && tiles.size() > maxiT) {
						return new Tile[0];
					}
					if (maxiD != -1
							&& tiles.size() * PApplet.sq(tS) + nbTMap > maxiD) {
						return new Tile[0];
					}
				}
			}
		}
		Tile[] tilesA = new Tile[tiles.size()];
		for (int i = 0; i < tilesA.length; i++) {
			tilesA[i] = (Tile) tiles.get(i);
			tilesA[i].setInstance(i);
		}
		return tilesA;
	}

	void investigateSetting(int minTS, int maxTS) {
		int maxSize = PApplet.min(im.width, im.height);
		//when set to -1, maxTS uses the maximum size
		if (maxTS==-1) maxTS=maxSize;
		Vector<ShotSetting> listeSettings = new Vector<ShotSetting>();
		int lessTData = im.width * im.height * 2;
		// lessTPData = the number of pixels in the better tileset so far
		ShotSetting best = new ShotSetting(lessTData);
		// tries with different tile sizes
		for (int tSI = PApplet.max(minTS, 2); tSI <= PApplet
				.min(maxSize, maxTS)
				&& PApplet.sq(tSI) < lessTData; tSI++) {
			System.out.println(tSI + " / " + PApplet.min(maxSize, maxTS));
			ShotSetting[][] allSettings = new ShotSetting[tSI][tSI];
			int nbTX = (im.width / tSI);
			int nbTY = (im.height / tSI);
			int lessTiles = nbTX * nbTY;
			// lessTiles = the number of tiles in the better tileset so far
			int xC = 0;// chosen x
			int yC = 0;// chosen y
			// checks every setting and
			// selects the better one
			for (int xI = 0; xI < tSI; xI++) {
				// creates a ShotSetting for all possible offsets
				for (int yI = 0; yI < tSI; yI++) {
					// returns Tile[0] if too long
					int essaiLength = getTileLists(xI, yI, tSI, lessTiles, -1).length;
					allSettings[xI][yI] = new ShotSetting(xI, yI, essaiLength,
							tSI, im.width, im.height);
					if (allSettings[xI][yI].getvalid()) {
						// checks if this setting is better than the previous
						// one
						int tPI = allSettings[xI][yI].getTotalPixels();
						if (tPI < allSettings[xC][yC].getTotalPixels()) {
							// stores the shorter length
							lessTiles = essaiLength;
							xC = xI;
							yC = yI;
						}
					}
				}
			}
			if (allSettings[xC][yC].getTotalData() < lessTData
					&& allSettings[xC][yC].getvalid()) {
				best = allSettings[xC][yC];
				lessTData = best.getTotalData();
				listeSettings.add(best);
			}
		}
		this.listeSettings = listeSettings;
	}

	void chooseSettingFromList(Vector<ShotSetting> listeSetting, int choice) {
		// if the choice is -1, then, it selects the tileset with the less
		// pixels in it
		if (choice == -1) {
			choice = 0;
			for (int i = 0; i < listeSettings.size(); i++) {
				if (listeSettings.get(i).getTotalPixels() < listeSettings.get(
						choice).getTotalPixels()) {
					choice = i;
				}
			}
		}
		this.tS = listeSetting.get(choice).gettS();
		this.offsetX = listeSetting.get(choice).getoffsetX();
		this.offsetY = listeSetting.get(choice).getoffsetY();
		this.nbTX = ((im.width - offsetX) / tS);
		this.nbTY = ((im.height - offsetY) / tS);
	}

	public int gettS() {
		return tS;
	}

	public Vector<ShotSetting> getlisteSetting() {
		return listeSettings;
	}

	public Palette getPalette() {
		return palette;
	}

}
