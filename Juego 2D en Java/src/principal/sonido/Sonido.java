package principal.sonido;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import principal.herramientas.CargadorRecursos;

public class Sonido {

	final private Clip sonido;

	public Sonido(final String ruta) {
		sonido = CargadorRecursos.cargarSonido(ruta);
	}
	
	public void reproducir() {
		sonido.stop();
		sonido.flush();
		sonido.setMicrosecondPosition(0);
		sonido.start();
	}
	
	public void repetir() {
		sonido.stop();
		sonido.flush();
		sonido.setMicrosecondPosition(0);
		
		sonido.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public long obtenerDuracion() {
		return sonido.getMicrosecondLength();
	}
}