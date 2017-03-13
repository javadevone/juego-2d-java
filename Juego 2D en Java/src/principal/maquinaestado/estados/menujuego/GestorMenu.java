package principal.maquinaestado.estados.menujuego;

import java.awt.*;

import principal.graficos.SuperficieDibujo;
import principal.maquinaestado.EstadoJuego;

public class GestorMenu implements EstadoJuego {

	private final SuperficieDibujo sd;

	private final EstructuraMenu estructuraMenu;

	private final SeccionMenu[] secciones;

	private SeccionMenu seccionActual;

	public GestorMenu(final SuperficieDibujo sd) {
		this.sd = sd;

		estructuraMenu = new EstructuraMenu();

		secciones = new SeccionMenu[2];

		final Rectangle etiquetaInventario = new Rectangle(
				estructuraMenu.BANNER_LATERAL.x + estructuraMenu.MARGEN_HORIZONTAL_ETIQUETAS,
				estructuraMenu.BANNER_LATERAL.y + estructuraMenu.MARGEN_VERTICAL_ETIQUETAS,
				estructuraMenu.ANCHO_ETIQUETAS, estructuraMenu.ALTO_ETIQUETAS);

		secciones[0] = new MenuInventario("Inventario", etiquetaInventario, estructuraMenu);

		final Rectangle etiquetaEquipo = new Rectangle(
				estructuraMenu.BANNER_LATERAL.x + estructuraMenu.MARGEN_HORIZONTAL_ETIQUETAS,
				etiquetaInventario.y + etiquetaInventario.height + estructuraMenu.MARGEN_VERTICAL_ETIQUETAS,
				estructuraMenu.ANCHO_ETIQUETAS, estructuraMenu.ALTO_ETIQUETAS);

		secciones[1] = new MenuEquipo("Equipo", etiquetaEquipo, estructuraMenu);

		seccionActual = secciones[0];
	}

	public void actualizar() {
		for (int i = 0; i < secciones.length; i++) {
			if (sd.obtenerRaton().obtenerClick() && sd.obtenerRaton().obtenerRectanguloPosicion()
					.intersects(secciones[i].obtenerEtiquetaMenuEscalada())) {
                if (secciones[i] instanceof  MenuEquipo) {
                    MenuEquipo seccion = (MenuEquipo) secciones[i];
                    if (seccion.objetoSeleccionado != null) {
                        seccion.eliminarObjetoSeleccionado();
                    }
                }

				seccionActual = secciones[i];
			}
		}

		seccionActual.actualizar();
	}

	public void dibujar(final Graphics g) {
		estructuraMenu.dibujar(g);

		for (int i = 0; i < secciones.length; i++) {

			if (seccionActual == secciones[i]) {
				if (sd.obtenerRaton().obtenerRectanguloPosicion()
						.intersects(secciones[i].obtenerEtiquetaMenuEscalada())) {
					secciones[i].dibujarEtiquetaActivaResaltada(g);
				} else {
					secciones[i].dibujarEtiquetaActiva(g);
				}
			} else {
				if (sd.obtenerRaton().obtenerRectanguloPosicion()
						.intersects(secciones[i].obtenerEtiquetaMenuEscalada())) {
					secciones[i].dibujarEtiquetaInactivaResaltada(g);
				} else {
					secciones[i].dibujarEtiquetaInactiva(g);
				}
			}
		}

		seccionActual.dibujar(g, sd, estructuraMenu);
	}
}