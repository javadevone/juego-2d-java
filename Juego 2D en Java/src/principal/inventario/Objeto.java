package principal.inventario;

import java.awt.Rectangle;

import principal.sprites.Sprite;

public abstract class Objeto {

	// public static HojaSprites hojaObjetos = new
	// HojaSprites(Constantes.RUTA_OBJETOS, Constantes.LADO_SPRITE, false);

	protected final int id;
	protected final String nombre;
	protected final String descripcion;

	protected int cantidad;
	protected int cantidadMaxima;

	protected Rectangle posicionMenu;
	protected Rectangle posicionFlotante;

	public Objeto(final int id, final String nombre, final String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;

		cantidad = 0;
		cantidadMaxima = 99;

		posicionMenu = new Rectangle(0, 0, 0, 0);
		posicionFlotante = new Rectangle(0, 0, 0, 0);
	}

	public Objeto(final int id, final String nombre, final String descripcion, final int cantidad) {
		this(id, nombre, descripcion);
		if (cantidad <= cantidadMaxima) {
			this.cantidad = cantidad;
		}
	}

	public abstract Sprite obtenerSprite();

	public boolean incrementarCantidad(final int incremento) {
		boolean incrementado = false;

		if (cantidad + incremento <= cantidadMaxima) {
			cantidad += incremento;
			incrementado = true;
		}

		return incrementado;
	}

	public boolean reducirCantidad(final int reduccion) {
		boolean reducido = false;

		if (cantidad - reduccion >= 0) {
			cantidad -= reduccion;
			reducido = true;
		}

		return reducido;
	}

	public int obtenerCantidad() {
		return cantidad;
	}

	public int obtenerId() {
		return id;
	}

	public String obtenerNombre() {
		return nombre;
	}

	public String obtenerDescripcion() {
		return descripcion;
	}

	public Rectangle obtenerPosicionMenu() {
		return posicionMenu;
	}

	public Rectangle obtenerPosicionFlotante() {
		return posicionFlotante;
	}

	public void establecerPosicionMenu(final Rectangle posicionMenu) {
		this.posicionMenu = posicionMenu;
	}

	public void establecerPosicionFlotante(final Rectangle posicionFlotante) {
		this.posicionFlotante = posicionFlotante;
	}
}
