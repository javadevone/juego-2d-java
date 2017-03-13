package principal.inventario.armas;

import principal.Constantes;
import principal.entes.Jugador;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Pistola extends Arma {

    public Pistola(int id, String nombre, String descripcion, int ataqueMinimo, int ataqueMaximo, boolean automatica, boolean penetrante, double ataquesPorSegundo) {
        super(id, nombre, descripcion, ataqueMinimo, ataqueMaximo, automatica, penetrante, ataquesPorSegundo, "/sonidos/disparo.wav");
    }

    public ArrayList<Rectangle> obtenerAlcance(final Jugador jugador) {

        final ArrayList<Rectangle> alcance = new ArrayList<>();

        final Rectangle alcance1 = new Rectangle();

        if (jugador.obtenerDireccion() == 0 || jugador.obtenerDireccion() == 1) {
            alcance1.width = 1;
            alcance1.height = 10 * Constantes.LADO_SPRITE;
            alcance1.x = Constantes.CENTRO_VENTANA_X;
            if (jugador.obtenerDireccion() == 0) {
                alcance1.y = Constantes.CENTRO_VENTANA_Y - 9;
            } else {

                alcance1.y = Constantes.CENTRO_VENTANA_Y - 9 - alcance1.height;
            }
        } else {
            alcance1.width = 10 * Constantes.LADO_SPRITE;
            alcance1.height = 1;
            alcance1.y = Constantes.CENTRO_VENTANA_Y - 3;

            if (jugador.obtenerDireccion() == 3) {
                alcance1.x = Constantes.CENTRO_VENTANA_X - alcance1.width;
            } else {

                alcance1.x = Constantes.CENTRO_VENTANA_X;
            }
        }

        alcance.add(alcance1);

        return alcance;
    }
}