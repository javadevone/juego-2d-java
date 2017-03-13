package principal.inventario.armas;

import principal.entes.Jugador;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Desarmado extends Arma {

	public Desarmado(int id, String nombre, String descripcion, int ataqueMinimo, int ataqueMaximo) {
		super(id, nombre, descripcion, ataqueMinimo, ataqueMaximo, false, false, 0, "/sonidos/golpe.wav");
	}

	public ArrayList<Rectangle> obtenerAlcance(final Jugador jugador) {
		return null;
	}

}
