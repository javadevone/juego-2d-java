package principal.herramientas;

import java.awt.Point;
import java.awt.Rectangle;

import principal.Constantes;

public class EscaladorElementos {

	public static Rectangle escalarRectanguloArriba(final Rectangle r) {
		return new Rectangle((int) (r.x * Constantes.FACTOR_ESCALADO_X), (int) (r.y * Constantes.FACTOR_ESCALADO_Y),
				(int) (r.width * Constantes.FACTOR_ESCALADO_X), (int) (r.height * Constantes.FACTOR_ESCALADO_Y));
	}

	public static Point escalarPuntoAbajo(final Point p) {
		return new Point((int) (p.x / Constantes.FACTOR_ESCALADO_X), (int) (p.y / Constantes.FACTOR_ESCALADO_Y));
	}

	public static Point escalarPuntoArriba(final Point p) {
		return new Point((int) (p.x * Constantes.FACTOR_ESCALADO_X), (int) (p.y * Constantes.FACTOR_ESCALADO_Y));
	}
}