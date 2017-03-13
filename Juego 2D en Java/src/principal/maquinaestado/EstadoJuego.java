package principal.maquinaestado;

import java.awt.Graphics;

public interface EstadoJuego {
	void actualizar();

	void dibujar(final Graphics g);
}