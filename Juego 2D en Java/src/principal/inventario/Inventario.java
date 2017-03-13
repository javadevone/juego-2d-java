package principal.inventario;

import java.util.ArrayList;

import principal.inventario.armas.Arma;
import principal.inventario.consumibles.Consumible;

public class Inventario {

	public final ArrayList<Objeto> objetos;

	public Inventario() {
		objetos = new ArrayList<Objeto>();
	}

	public void recogerObjetos(final ContenedorObjetos co) {
		for (Objeto objeto : co.obtenerObjetos()) {
			if (objetoExiste(objeto)) {
				incrementarObjeto(objeto, objeto.obtenerCantidad());
			} else {
				objetos.add(objeto);
			}
		}
	}
	
	public void recogerObjetos(final ObjetoUnicoTiled objetoUnico) {
		if (objetoExiste(objetoUnico.obtenerObjeto())) {
			incrementarObjeto(objetoUnico.obtenerObjeto(), objetoUnico.obtenerObjeto().obtenerCantidad());
		} else {
			objetos.add(objetoUnico.obtenerObjeto());
		}
	}

	public boolean incrementarObjeto(final Objeto objeto, final int cantidad) {
		boolean incrementado = false;

		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == objeto.obtenerId()) {
				objetoActual.incrementarCantidad(cantidad);
				incrementado = true;
				break;
			}
		}

		return incrementado;
	}

	public boolean objetoExiste(final Objeto objeto) {
		boolean existe = false;

		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == objeto.obtenerId()) {
				existe = true;
				break;
			}
		}

		return existe;
	}

	public ArrayList<Objeto> obtenerConsumibles() {
		ArrayList<Objeto> consumibles = new ArrayList<Objeto>();

		for (Objeto objeto : objetos) {
			if (objeto instanceof Consumible) {
				consumibles.add(objeto);
			}
		}

		return consumibles;
	}

	public ArrayList<Objeto> obtenerArmas() {
		ArrayList<Objeto> armas = new ArrayList<Objeto>();

		for (Objeto objeto : objetos) {
			if (objeto instanceof Arma) {
				armas.add(objeto);
			}
		}

		return armas;
	}

	public Objeto obtenerObjeto(final int id) {
		for (Objeto objetoActual : objetos) {
			if (objetoActual.obtenerId() == id) {
				return objetoActual;
			}
		}

		return null;
	}
}