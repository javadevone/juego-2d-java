package principal.maquinaestado.estados.menujuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.graficos.SuperficieDibujo;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.herramientas.MedidorStrings;
import principal.inventario.Objeto;

public class MenuInventario extends SeccionMenu {

	public MenuInventario(String nombreSeccion, Rectangle etiquetaMenu, EstructuraMenu em) {
		super(nombreSeccion, etiquetaMenu, em);

		int anchoBarra = 100;
	}

	public void actualizar() {
		actualizarPosicionesMenu();
	}

	private void actualizarPosicionesMenu() {
		if (ElementosPrincipales.inventario.obtenerConsumibles().isEmpty()) {
			return;
		}

		for (int i = 0; i < ElementosPrincipales.inventario.obtenerConsumibles().size(); i++) {
			final Point puntoInicial = new Point(em.FONDO.x + 16, barraPeso.y + barraPeso.height + margenGeneral);

			final int lado = Constantes.LADO_SPRITE;

			int idActual = ElementosPrincipales.inventario.obtenerConsumibles().get(i).obtenerId();

			ElementosPrincipales.inventario.obtenerObjeto(idActual).establecerPosicionMenu(
					new Rectangle(puntoInicial.x + i * (lado + margenGeneral), puntoInicial.y, lado, lado));
		}
	}

	public void dibujar(Graphics g, SuperficieDibujo sd, EstructuraMenu em) {
		dibujarLimitePeso(g, em);

		if (sd.obtenerRaton().obtenerRectanguloPosicion()
				.intersects(EscaladorElementos.escalarRectanguloArriba(barraPeso))) {
			dibujarTooltipPeso(g, sd, em);
		}

		dibujarElementosConsumibles(g, em);
		dibujarPaginador(g, em);
	}

	private void dibujarElementosConsumibles(final Graphics g, EstructuraMenu em) {

		if (ElementosPrincipales.inventario.obtenerConsumibles().isEmpty()) {
			return;
		}

		// final Point puntoInicial = new Point(titularPanel.x + margenGeneral,
		// titularPanel.y + titularPanel.height + margenGeneral);
		final Point puntoInicial = new Point(em.FONDO.x + 16, barraPeso.y + barraPeso.height + margenGeneral);

		final int lado = Constantes.LADO_SPRITE;

		for (int i = 0; i < ElementosPrincipales.inventario.obtenerConsumibles().size(); i++) {

			int idActual = ElementosPrincipales.inventario.obtenerConsumibles().get(i).obtenerId();
			Objeto objetoActual = ElementosPrincipales.inventario.obtenerObjeto(idActual);

			DibujoDebug.dibujarImagen(g, objetoActual.obtenerSprite().obtenerImagen(),
					objetoActual.obtenerPosicionMenu().x, objetoActual.obtenerPosicionMenu().y);

			g.setColor(Color.black);

			DibujoDebug.dibujarRectanguloRelleno(g, puntoInicial.x + i * (lado + margenGeneral) + lado - 12,
					puntoInicial.y + 32 - 8, 12, 8);

			g.setColor(Color.white);

			String texto = "";

			if (objetoActual.obtenerCantidad() < 10) {
				texto = "0" + objetoActual.obtenerCantidad();
			} else {
				texto = "" + objetoActual.obtenerCantidad();
			}

			g.setFont(g.getFont().deriveFont(10f));
			DibujoDebug.dibujarString(g, texto,
					puntoInicial.x + i * (lado + margenGeneral) + lado - MedidorStrings.medirAnchoPixeles(g, texto),
					puntoInicial.y + 31);
		}
		g.setFont(g.getFont().deriveFont(12f));
	}

	private void dibujarPaginador(final Graphics g, EstructuraMenu em) {
		final int lado = 32;
		final int alto = 16;

		final Rectangle anterior = new Rectangle(em.FONDO.x + em.FONDO.width - margenGeneral * 2 - lado * 2 - 4,
				em.FONDO.y + em.FONDO.height - margenGeneral - alto, lado, alto);
		final Rectangle siguiente = new Rectangle(anterior.x + anterior.width + margenGeneral, anterior.y, lado, alto);

		g.setColor(Color.orange);

		g.fillRect(anterior.x, anterior.y, anterior.width, anterior.height);
		g.fillRect(siguiente.x, siguiente.y, siguiente.width, siguiente.height);
	}
}