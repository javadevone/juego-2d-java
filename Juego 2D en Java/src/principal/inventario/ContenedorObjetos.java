package principal.inventario;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;

public class ContenedorObjetos {

	private static final BufferedImage sprite = CargadorRecursos
			.cargarImagenCompatibleTranslucida("/imagenes/saco.png");

	private Point posicion;
	private Objeto[] objetos;

	public ContenedorObjetos(final Point posicion, final int[] objetos, final int[] cantidades) {

		this.posicion = posicion;
		this.objetos = new Objeto[objetos.length];

		for (int i = 0; i < objetos.length; i++) {
			this.objetos[i] = RegistroObjetos.obtenerObjeto(objetos[i]);
			this.objetos[i].incrementarCantidad(cantidades[i]);
		}
	}

	public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
		DibujoDebug.dibujarImagen(g, sprite, puntoX, puntoY);
	}

	public Point obtenerPosicion() {
		return posicion;
	}

	public Objeto[] obtenerObjetos() {
		return objetos;
	}
}