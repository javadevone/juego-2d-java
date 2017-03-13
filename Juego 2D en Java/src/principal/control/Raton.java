package principal.control;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import principal.Constantes;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DatosDebug;

public class Raton extends MouseAdapter {

	private final Cursor cursor;
	private Point posicion;
	private boolean click;
	private boolean click2;

	public final int ladoCursor;

	public Raton(final SuperficieDibujo sd) {
		Toolkit configuracion = Toolkit.getDefaultToolkit();

		final BufferedImage iconoCargado = CargadorRecursos
				.cargarImagenCompatibleTranslucida(Constantes.RUTA_ICONO_RATON);

		ladoCursor = iconoCargado.getHeight();

		BufferedImage icono = iconoCargado;

		Point punta = new Point(0, 0);

		this.cursor = configuracion.createCustomCursor(icono, punta, "Cursor por defecto");

		posicion = new Point();
		actualizarPosicion(sd);

		click = false;
		click2 = false;
	}

	public void actualizar(final SuperficieDibujo sd) {
		actualizarPosicion(sd);

	}

	public void dibujar(final Graphics g) {
		DatosDebug.enviarDato("RX: " + posicion.getX());
		DatosDebug.enviarDato("RY: " + posicion.getY());
	}

	public Cursor obtenerCursor() {
		return this.cursor;
	}

	private void actualizarPosicion(final SuperficieDibujo sd) {
		final Point posicionInicial = MouseInfo.getPointerInfo().getLocation();

		SwingUtilities.convertPointFromScreen(posicionInicial, sd);

		posicion.setLocation(posicionInicial.getX(), posicionInicial.getY());
	}

	public Point obtenerPuntoPosicion() {
		return posicion;
	}

	public Rectangle obtenerRectanguloPosicion() {
		final Rectangle area = new Rectangle(posicion.x, posicion.y, 1, 1);

		return area;
	}

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			click = true;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			click2 = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			click = false;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			click2 = false;
		}
	}

	public boolean obtenerClick() {
		return click;
	}

	public boolean obtenerClick2() {
		return click2;
	}
}