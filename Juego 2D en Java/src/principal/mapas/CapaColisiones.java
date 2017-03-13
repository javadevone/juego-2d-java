package principal.mapas;

import java.awt.Rectangle;

public class CapaColisiones extends CapaTiled {

	private Rectangle[] colisionables;
	
	public CapaColisiones(int ancho, int alto, int x, int y, Rectangle[] colisionables) {
		super(ancho, alto, x, y);
		this.colisionables = colisionables;
	}
	
	public Rectangle[] obtenerColisionables() {
		return colisionables;
	}
}
