package principal.interfaz_usuario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.entes.Jugador;
import principal.herramientas.DibujoDebug;

public class MenuInferior {

	private Rectangle areaInventario;
	private Rectangle bordeAreaInventario;

	private Color negroDesaturado;
	private Color rojoClaro;
	private Color rojoOscuro;
	private Color azulClaro;
	private Color azulOscuro;
	private Color verdeClaro;
	private Color verdeOscuro;
	private Color rosaClaro;
	private Color rosaOscuro;

	public MenuInferior() {

		int altoMenu = 64;
		areaInventario = new Rectangle(0, Constantes.ALTO_JUEGO - altoMenu, Constantes.ANCHO_JUEGO, altoMenu);
		bordeAreaInventario = new Rectangle(areaInventario.x, areaInventario.y - 1, areaInventario.width, 1);

		negroDesaturado = new Color(23, 23, 23);
		rojoClaro = new Color(255, 0, 0);
		rojoOscuro = new Color(150, 0, 0);
		azulClaro = new Color(0, 200, 255);
		azulOscuro = new Color(0, 132, 168);
		verdeClaro = new Color(0, 255, 0);
		verdeOscuro = new Color(0, 150, 0);
		rosaClaro = new Color(255, 0, 150);
		rosaOscuro = new Color(128, 0, 74);
	}

	public void dibujar(final Graphics g) {
		dibujarAreaInventario(g);
		dibujarBarraVitalidad(g);
		dibujarBarraPoder(g);
		dibujarBarraResistencia(g);
		dibujarBarraExperiencia(g, 75);
		dibujarRanurasObjetos(g);
		dibujarPuntos(g);
	}
	
	private void dibujarPuntos(final Graphics g) {
		final int medidaVertical = 4;
		DibujoDebug.dibujarString(g, "Puntos: " + ElementosPrincipales.jugador.puntos, areaInventario.x + 10,
				areaInventario.y + medidaVertical * 15, Color.white);
	}

	private void dibujarAreaInventario(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario, negroDesaturado);
		DibujoDebug.dibujarRectanguloRelleno(g, bordeAreaInventario, Color.white);
	}

	private void dibujarBarraVitalidad(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical, anchoTotal,
				medidaVertical, rojoClaro);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 2,
				anchoTotal, medidaVertical, rojoOscuro);

		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "VIT", areaInventario.x + 10, areaInventario.y + medidaVertical * 3);
		DibujoDebug.dibujarString(g, "1000", anchoTotal + 45, areaInventario.y + medidaVertical * 3);
	}

	private void dibujarBarraPoder(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 4,
				anchoTotal, medidaVertical, azulClaro);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 5,
				anchoTotal, medidaVertical, azulOscuro);

		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "POW", areaInventario.x + 10, areaInventario.y + medidaVertical * 6);
		DibujoDebug.dibujarString(g, "1000", anchoTotal + 45, areaInventario.y + medidaVertical * 6);
	}

	private void dibujarBarraResistencia(final Graphics g) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;
		final int ancho = anchoTotal * ElementosPrincipales.jugador.obtenerResistencia() / Jugador.RESISTENCIA_TOTAL;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 7, ancho,
				medidaVertical, verdeClaro);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 8, ancho,
				medidaVertical, verdeOscuro);

		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "RST", areaInventario.x + 10, areaInventario.y + medidaVertical * 9);
		DibujoDebug.dibujarString(g, "" + ElementosPrincipales.jugador.obtenerResistencia(), anchoTotal + 45,
				areaInventario.y + medidaVertical * 9);
	}

	private void dibujarBarraExperiencia(final Graphics g, final int experiencia) {
		final int medidaVertical = 4;
		final int anchoTotal = 100;
		final int ancho = anchoTotal * experiencia / anchoTotal;

		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 10, ancho,
				medidaVertical, rosaClaro);
		DibujoDebug.dibujarRectanguloRelleno(g, areaInventario.x + 35, areaInventario.y + medidaVertical * 11, ancho,
				medidaVertical, rosaOscuro);

		g.setColor(Color.white);
		DibujoDebug.dibujarString(g, "EXP", areaInventario.x + 10, areaInventario.y + medidaVertical * 12);
		DibujoDebug.dibujarString(g, experiencia + "%", anchoTotal + 45, areaInventario.y + medidaVertical * 12);
	}

	private void dibujarRanurasObjetos(final Graphics g) {
		final int anchoRanura = 32;
		final int numeroRanuras = 10;
		final int espaciadoRanuras = 10;
		final int anchoTotal = anchoRanura * numeroRanuras + espaciadoRanuras * numeroRanuras;
		final int xInicial = Constantes.ANCHO_JUEGO - anchoTotal;
		final int anchoRanuraYEspacio = anchoRanura + espaciadoRanuras;

		g.setColor(Color.white);

		for (int i = 0; i < numeroRanuras; i++) {
			int xActual = xInicial + anchoRanuraYEspacio * i;

			Rectangle ranura = new Rectangle(xActual, areaInventario.y + 4, anchoRanura, anchoRanura);
			DibujoDebug.dibujarRectanguloRelleno(g, ranura);
			DibujoDebug.dibujarString(g, "" + i, xActual + 13, areaInventario.y + 54);
		}
	}
}