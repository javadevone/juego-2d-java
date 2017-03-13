package principal;

import principal.entes.Jugador;
import principal.inventario.Inventario;
import principal.mapas.Mapa;
import principal.mapas.MapaTiled;

public class ElementosPrincipales {
	
	public static MapaTiled mapa = new MapaTiled("/mapas/mapa-apocaliptico.json");
	//public static Mapa mapa = new Mapa(Constantes.RUTA_MAPA);
	public static Jugador jugador = new Jugador();
	public static Inventario inventario = new Inventario();

}