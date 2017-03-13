package principal.herramientas;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class MedidorStrings {
	public static int medirAnchoPixeles(final Graphics g, final String s) {
		FontMetrics fm = g.getFontMetrics();

		return fm.stringWidth(s);
	}

	public static int medirAltoPixeles(final Graphics g, final String s) {
		FontMetrics fm = g.getFontMetrics();

		return (int) fm.getLineMetrics(s, g).getHeight();
	}

	public static Rectangle obtenerContorno(final Graphics g, final String s, final Point p) {
		FontMetrics fm = g.getFontMetrics();
		final int margen = 2;

		final int ancho = fm.stringWidth(s);
		final int alto = (int) fm.getLineMetrics(s, g).getHeight();

		return new Rectangle(p.x - margen, p.y - alto + margen, ancho + margen * 2, alto);
	}
}