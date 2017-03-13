package principal.entes;


import principal.Constantes;
import principal.herramientas.DibujoDebug;
import principal.sonido.Sonido;
import principal.sprites.HojaSprites;

import java.awt.*;

public class Zombie extends Enemigo {

    private static HojaSprites hojaZombie;

    public Zombie(final int idEnemigo, final String nombre, final int vidaMaxima, final String rutaLamento) {
        super(idEnemigo, nombre, vidaMaxima, rutaLamento);

        if (hojaZombie == null) {
            hojaZombie = new HojaSprites(Constantes.RUTA_ENEMIGOS + idEnemigo + ".png",
                    Constantes.LADO_SPRITE, false);
        }
    }

    public void dibujar(final Graphics g, final int puntoX, final int puntoY) {
        DibujoDebug.dibujarImagen(g, hojaZombie.obtenerSprite(0).obtenerImagen(), puntoX, puntoY);
        super.dibujar(g, puntoX, puntoY);
    }
}