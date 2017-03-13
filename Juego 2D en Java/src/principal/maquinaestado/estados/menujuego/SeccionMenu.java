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
import principal.herramientas.GeneradorTooltip;
import principal.herramientas.MedidorStrings;

public abstract class SeccionMenu {

	protected final String nombreSeccion;
	protected final Rectangle etiquetaMenu;
	protected final EstructuraMenu em;

	protected final int margenGeneral = 8;
	protected final Rectangle barraPeso;

	public SeccionMenu(final String nombreSeccion, final Rectangle etiquetaMenu, final EstructuraMenu em) {
		this.nombreSeccion = nombreSeccion;
		this.etiquetaMenu = etiquetaMenu;
		this.em = em;

		int anchoBarra = 100;

		barraPeso = new Rectangle(Constantes.ANCHO_JUEGO - anchoBarra - 12, em.BANNER_SUPERIOR.height + margenGeneral,
				ElementosPrincipales.jugador.limitePeso, 8);
	}

	public abstract void actualizar();

	public abstract void dibujar(final Graphics g, final SuperficieDibujo sd, final EstructuraMenu em);

	public void dibujarEtiquetaInactiva(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);
		DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);
	}

	public void dibujarEtiquetaActiva(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

		final Rectangle marcaActiva = new Rectangle(etiquetaMenu.x, etiquetaMenu.y, 5, etiquetaMenu.height);
		DibujoDebug.dibujarRectanguloRelleno(g, marcaActiva, new Color(0xff6700));

		DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);
	}

	public void dibujarEtiquetaInactivaResaltada(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

		DibujoDebug.dibujarRectanguloRelleno(g, new Rectangle(etiquetaMenu.x + etiquetaMenu.width - 10,
				etiquetaMenu.y + 5, 5, etiquetaMenu.height - 10), new Color(0x2a2a2a));

		DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);
	}

	public void dibujarEtiquetaActivaResaltada(final Graphics g) {
		DibujoDebug.dibujarRectanguloRelleno(g, etiquetaMenu, Color.white);

		final Rectangle marcaActiva = new Rectangle(etiquetaMenu.x, etiquetaMenu.y, 5, etiquetaMenu.height);
		DibujoDebug.dibujarRectanguloRelleno(g, marcaActiva, new Color(0xff6700));

		DibujoDebug.dibujarRectanguloRelleno(g, new Rectangle(etiquetaMenu.x + etiquetaMenu.width - 10,
				etiquetaMenu.y + 5, 5, etiquetaMenu.height - 10), new Color(0x2a2a2a));

		DibujoDebug.dibujarString(g, nombreSeccion, etiquetaMenu.x + 15, etiquetaMenu.y + 12, Color.black);
	}

	protected void dibujarLimitePeso(final Graphics g, EstructuraMenu em) {

		final Rectangle contenidoBarra = new Rectangle(barraPeso.x + 1, barraPeso.y + 1,
				barraPeso.width / (ElementosPrincipales.jugador.limitePeso / ElementosPrincipales.jugador.pesoActual),
				barraPeso.height - 2);

		DibujoDebug.dibujarString(g, "Peso", barraPeso.x - 30, barraPeso.y + 7, Color.black);
		DibujoDebug.dibujarRectanguloRelleno(g, barraPeso, Color.gray);
		DibujoDebug.dibujarRectanguloRelleno(g, contenidoBarra, em.COLOR_BANNER_SUPERIOR);
	}

	protected void dibujarTooltipPeso(final Graphics g, SuperficieDibujo sd, EstructuraMenu em) {

		final Point posicionRaton = sd.obtenerRaton().obtenerPuntoPosicion();
		final Point posicionTooltip = GeneradorTooltip.generarTooltip(posicionRaton);
		final String pistaPosicion = GeneradorTooltip.obtenerPosicionTooltip(posicionRaton);
		final Point posicionTooltipEscalada = EscaladorElementos.escalarPuntoAbajo(posicionTooltip);

		final String informacionPeso = ElementosPrincipales.jugador.pesoActual + "/"
				+ ElementosPrincipales.jugador.limitePeso;
		final int ancho = MedidorStrings.medirAnchoPixeles(g, informacionPeso);
		final int alto = MedidorStrings.medirAltoPixeles(g, informacionPeso);
		final int margenFuente = 2;

		Rectangle tooltip = null;

		switch (pistaPosicion) {
		case "no":
			tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y, ancho + margenFuente * 2,
					alto);
			break;
		case "ne":
			tooltip = new Rectangle(posicionTooltipEscalada.x - ancho, posicionTooltipEscalada.y,
					ancho + margenFuente * 2, alto);
			break;
		case "so":
			tooltip = new Rectangle(posicionTooltipEscalada.x, posicionTooltipEscalada.y - alto, ancho, alto);
			break;
		case "se":
			tooltip = new Rectangle(posicionTooltipEscalada.x - ancho, posicionTooltipEscalada.y - alto,
					ancho + margenFuente * 2, alto);
			break;
		}

		DibujoDebug.dibujarRectanguloRelleno(g, tooltip, Color.black);
		DibujoDebug.dibujarString(g, informacionPeso,
				new Point(tooltip.x + margenFuente, tooltip.y + tooltip.height - margenFuente), Color.white);
	}

	public String obtenerNombreSeccion() {
		return nombreSeccion;
	}

	public Rectangle obtenerEtiquetaMenu() {
		return etiquetaMenu;
	}

	public Rectangle obtenerEtiquetaMenuEscalada() {
		final Rectangle etiquetaEscalada = new Rectangle((int) (etiquetaMenu.x * Constantes.FACTOR_ESCALADO_X),
				(int) (etiquetaMenu.y * Constantes.FACTOR_ESCALADO_Y),
				(int) (etiquetaMenu.width * Constantes.FACTOR_ESCALADO_X),
				(int) (etiquetaMenu.height * Constantes.FACTOR_ESCALADO_Y));

		return etiquetaEscalada;
	}

}