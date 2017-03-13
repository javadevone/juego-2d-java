package principal.maquinaestado.estados.juego;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DatosDebug;
import principal.herramientas.DibujoDebug;
import principal.interfaz_usuario.MenuInferior;
import principal.mapas.Mapa;
import principal.maquinaestado.EstadoJuego;

public class GestorJuego implements EstadoJuego {

	BufferedImage logo;
	MenuInferior menuInferior;

	public GestorJuego() {
		menuInferior = new MenuInferior();
		logo = CargadorRecursos.cargarImagenCompatibleTranslucida(Constantes.RUTA_LOGOTIPO);

	}

	public void actualizar() {

		ElementosPrincipales.jugador.actualizar();
		ElementosPrincipales.mapa.actualizar();
	}

	public void dibujar(Graphics g) {
		ElementosPrincipales.mapa.dibujar(g);
		ElementosPrincipales.jugador.dibujar(g);
		menuInferior.dibujar(g);

		DibujoDebug.dibujarImagen(g, logo, 640 - logo.getWidth() - 5, 0 + 5);

		DatosDebug.enviarDato("X = " + ElementosPrincipales.jugador.obtenerPosicionXInt());
		DatosDebug.enviarDato("Y = " + ElementosPrincipales.jugador.obtenerPosicionYInt());
	}
}