public class ColorPx {
	int a;
	int r;
	int g;
	int b;

	ColorPx(int a, int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	ColorPx(int argb) {
		this.a = argb >> 24 & 0xFF;
		this.r = argb >> 16 & 0xFF;
		this.g = argb >> 8 & 0xFF;
		this.b = argb >> 0 & 0xFF;
	}

	boolean equals(ColorPx col) {
		boolean equals = true;
		if (col.getA() != getA())
			equals = false;
		if (col.getR() != getR())
			equals = false;
		if (col.getG() != getG())
			equals = false;
		if (col.getB() != getB())
			equals = false;
		return equals;
	}

	int getA() {
		return a;
	}

	int getR() {
		return r;
	}

	int getG() {
		return g;
	}

	int getB() {
		return b;
	}

	int toInt() {
		int color = (a << 24) | (r << 16) | (g << 8) | (b << 0);
		return color;
	}
}
