import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.JOptionPane;

import processing.core.PApplet;

public class InterfaceA extends Interface {

	public Shot lev;// the level to parse
	public Tileset tileset;// the resulting tileset
	public Map map;// the level map
	public MappingStats stat;// the level stats
	int selectedTile;// Type to set tiles
	int gotoI = 0;// 0=this, 1=InterfaceB

	public Shot getLev() {
		return lev;
	}

	public void setLev(Shot lev) {
		this.lev = lev;
	}

	public Tileset getTileset() {
		return tileset;
	}

	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public MappingStats getStat() {
		return stat;
	}

	public void setStat(MappingStats stat) {
		this.stat = stat;
	}

	public int getSelectedTile() {
		return selectedTile;
	}

	public void setSelectedTile(int selectedTile) {
		this.selectedTile = selectedTile;
	}

	/*
	 * This interface analyzes a screenshot and the allows the user to define
	 * the tiles types on it.
	 */
	InterfaceA(PApplet p) {
		super(p);
	}

	public void setup() {
		Frame frame = new Frame();
		FileDialog fileDialog = new FileDialog(frame, "open screen",
				FileDialog.LOAD);
		fileDialog.setVisible(true);
		String userInput = JOptionPane
				.showInputDialog("Minimum tile size search : ");
		int sizeMin;// max nb of letters in a chain when reading
		sizeMin = Integer.parseInt(userInput);
		userInput = JOptionPane.showInputDialog("Maximum tile size search : ");
		int sizeMax;// max nb of letters in a chain when reading
		sizeMax = Integer.parseInt(userInput);
		String filePath = fileDialog.getDirectory() + fileDialog.getFile();
		lev = new Shot(parent, parent.dataPath(filePath));
		lev.investigateSetting(sizeMin, sizeMax);
		lev.chooseSettingFromList(lev.getlisteSetting(), -1);
		tileset = new Tileset(parent);
		tileset.addTiles(lev.getTileLists(), lev.gettS(), lev.getPalette());
		map = lev.generateMap(tileset);
		selectedTile = 1;
		// displays a first frame
		parent.background(0);
		tileset.displayTypesMap(map);
		fileDialog = new FileDialog(frame, "save as", FileDialog.SAVE);
		fileDialog.setVisible(true);
		filePath = fileDialog.getDirectory() + fileDialog.getFile();
		tileset.outputFile(filePath);
		map.outputPng(filePath);
		map.outputTxt(filePath);
		MappingStats stat = new MappingStats(parent);
		stat = map.generateStats();
		stat.outputTxt(filePath);
		afficheBoutons();
	}

	public void use() {
		if (parent.mousePressed) {
			if (fonctionSouris() == 1) {
				tileset.setTypeMouse(parent.mouseX, parent.mouseY, map,
						selectedTile);
				parent.background(0);
				tileset.displayTypesMap(map);
				afficheBoutons();
			}
			if (fonctionSouris() == 2) {
				selectedTile = PApplet.floor((float) parent.mouseX
						* tileset.getnbTypes() / parent.width);
				parent.background(0);
				tileset.displayTypesMap(map);
				afficheBoutons();
			}
			if (fonctionSouris() == 3) {
				// deletes invalid tiles
				deleteInvalid();
				// generates stats
				stat = map.generateStats();
				// stat.outputTxt();
				// then loads InterfaceB
				// gotoI = 1;
				// but since it doesn't work well, don't do it
			}
		}
	}

	public int getGotoI() {
		return gotoI;
	}

	public void setGotoI(int gotoI) {
		this.gotoI = gotoI;
	}

	void display() {
		parent.background(0);
		tileset.display();
	}

	void afficheBoutons() {
		int nbT = tileset.getnbTypes();
		for (int i = 0; i < nbT; i++) {
			ColorPx c = new ColorPx(Color.HSBtoRGB((float) i / nbT, 1, 1));
			parent.stroke(c.getR(), c.getG(), c.getB());
			if (selectedTile == i) {
				parent.stroke(0xFF);
			}
			parent.fill(c.getR(), c.getG(), c.getB(), 127);
			parent.rect(i * parent.width / nbT, map.gety() * tileset.gettS(),
					tileset.gettS(), tileset.gettS());
			parent.fill(255);
			float posX = i * parent.width / nbT;
			float posY = (map.gety() + 1) * tileset.gettS();
			if (i == 0) {
				parent.text("nothing", posX, posY);
			} else if (i == 1) {
				parent.text("to be deleted", posX, posY);
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
		if (parent.mouseY <= map.gety() * tileset.gettS()) {
			return 1;
		} else if (parent.mouseY <= (map.gety() + 1) * tileset.gettS()) {
			return 2;
		} else {
			return 3;
		}
	}

	private void deleteInvalid() {
		// deletes tiles of the type 1
		int[] toShift = tileset.deleteInvalid();
		map.shift(toShift);
		parent.background(0);
		tileset.displayTypesMap(map);
	}

}
