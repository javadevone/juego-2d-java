package principal.mapas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.dijkstra.Dijkstra;
import principal.entes.Enemigo;
import principal.entes.RegistroEnemigos;
import principal.herramientas.CalculadoraDistancia;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.herramientas.EscaladorElementos;
import principal.inventario.ContenedorObjetos;
import principal.inventario.Objeto;
import principal.inventario.ObjetoUnicoTiled;
import principal.inventario.RegistroObjetos;
import principal.inventario.armas.Desarmado;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class MapaTiled {
	
	private int anchoMapaEnTiles;
	private int altoMapaEnTiles;
	
	private Point puntoInicial;
	
	private ArrayList<CapaSprites> capasSprites;
	private ArrayList<CapaColisiones> capasColisiones;
	
	private ArrayList<Rectangle> areasColisionOriginales;
	public ArrayList<Rectangle> areasColisionPorActualizacion;
	
	private Sprite[] paletaSprites;
	
	private Dijkstra d;
	
	private ArrayList<ObjetoUnicoTiled> objetosMapa;
	private ArrayList<Enemigo> enemigosMapa;

	public MapaTiled(final String ruta) {
		String contenido = CargadorRecursos.leerArchivoTexto(ruta);
		
		//ANCHO, ALTO
		JSONObject globalJSON = obtenerObjetoJSON(contenido);
		anchoMapaEnTiles = obtenerIntDesdeJSON(globalJSON, "width");
		altoMapaEnTiles = obtenerIntDesdeJSON(globalJSON, "height");
		
		//PUNTO INICIAL
		JSONObject puntoInicial = obtenerObjetoJSON(globalJSON.get("start").toString());
		this.puntoInicial = new Point(obtenerIntDesdeJSON(puntoInicial, "x"), 
				obtenerIntDesdeJSON(puntoInicial, "y"));
		
		//CAPAS
		JSONArray capas = obtenerArrayJSON(globalJSON.get("layers").toString());
		
		this.capasSprites = new ArrayList<>();
		this.capasColisiones = new ArrayList<>();
		
		//INICIAR CAPAS
		for (int i = 0; i < capas.size(); i++) {
			JSONObject datosCapa = obtenerObjetoJSON(capas.get(i).toString());
			
			int anchoCapa = obtenerIntDesdeJSON(datosCapa, "width");
			int altoCapa = obtenerIntDesdeJSON(datosCapa, "height");
			int xCapa = obtenerIntDesdeJSON(datosCapa, "x");
			int yCapa = obtenerIntDesdeJSON(datosCapa, "y");
			String tipo = datosCapa.get("type").toString();
			
			switch(tipo) {
			case "tilelayer":
				JSONArray sprites = obtenerArrayJSON(datosCapa.get("data").toString());
				int[] spritesCapa = new int[sprites.size()];
				for (int j = 0; j< sprites.size(); j++) {
					int codigoSprite = Integer.parseInt(sprites.get(j).toString());
					spritesCapa[j] = codigoSprite - 1;
				}
				this.capasSprites.add(new CapaSprites(anchoCapa, altoCapa, xCapa, yCapa, spritesCapa));
				break;
			case "objectgroup":
				JSONArray rectangulos = obtenerArrayJSON(datosCapa.get("objects").toString());
				Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];
				for (int j = 0; j < rectangulos.size(); j++) {
					JSONObject datosRectangulo = obtenerObjetoJSON(rectangulos.get(j).toString());
					
					int x = obtenerIntDesdeJSON(datosRectangulo, "x");
					int y = obtenerIntDesdeJSON(datosRectangulo, "y");
					int ancho = obtenerIntDesdeJSON(datosRectangulo, "width");
					int alto = obtenerIntDesdeJSON(datosRectangulo, "height");
					
					if (x == 0) x = 1;
					if (y == 0) y = 1;
					if (ancho == 0) ancho = 1;
					if (alto == 0) alto = 1;
					
					Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
					rectangulosCapa[j] = rectangulo;
				}
				this.capasColisiones.add(new CapaColisiones(anchoCapa, altoCapa, xCapa, yCapa, rectangulosCapa));
				
				break;
			}
		}
		
		//COMBINAR COLISIONES EN UN SOLO ARRAYLIST POR EFICIENCIA
		areasColisionOriginales = new ArrayList<>();
		for (int i = 0; i < capasColisiones.size(); i++) {
			Rectangle[] rectangulos = capasColisiones.get(i).obtenerColisionables();
			
			for (int j = 0; j < rectangulos.length; j++) {
				areasColisionOriginales.add(rectangulos[j]);
			}
		}
		
		d = new Dijkstra(new Point(10, 10), anchoMapaEnTiles, altoMapaEnTiles, areasColisionOriginales);
		
		//AVERIGUAR TOTAL DE SPRITES EXISTENTES EN TODAS LAS CAPAS
		JSONArray coleccionesSprites = obtenerArrayJSON(globalJSON.get("tilesets").toString());
		int totalSprites = 0;
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = obtenerObjetoJSON(coleccionesSprites.get(i).toString());
			totalSprites += obtenerIntDesdeJSON(datosGrupo, "tilecount");
		}
		paletaSprites = new Sprite[totalSprites];
		
		//ASIGNAR SPRITES NECESARIOS A LA PALETA A PARTIR DE LAS CAPAS
		for (int i = 0; i < coleccionesSprites.size(); i++) {
			JSONObject datosGrupo = obtenerObjetoJSON(coleccionesSprites.get(i).toString());
			
			String nombreImagen = datosGrupo.get("image").toString();
			int anchoTiles = obtenerIntDesdeJSON(datosGrupo, "tilewidth");
			int altoTiles = obtenerIntDesdeJSON(datosGrupo, "tileheight");
			HojaSprites hoja = new HojaSprites("/imagenes/hojasTexturas/" + nombreImagen,
					anchoTiles, altoTiles, false);
			
			int primerSpriteColeccion = obtenerIntDesdeJSON(datosGrupo, "firstgid") - 1;
			int ultimoSpriteColeccion = primerSpriteColeccion + obtenerIntDesdeJSON(datosGrupo, "tilecount") - 1;
			
			for (int j = 0; j < this.capasSprites.size(); j++) {
				CapaSprites capaActual = this.capasSprites.get(j);
				int[] spritesCapa = capaActual.obtenerArraySprites();
				
				for (int k = 0; k < spritesCapa.length; k++) {
					int idSpriteActual = spritesCapa[k];
					if (idSpriteActual >= primerSpriteColeccion && idSpriteActual <= ultimoSpriteColeccion) {
						if (paletaSprites[idSpriteActual] == null) {
							paletaSprites[idSpriteActual] = hoja.obtenerSprite(idSpriteActual - primerSpriteColeccion);
						}
					}
				}
			}	
		}
		
		//OBTENER OBJETOS
		objetosMapa = new ArrayList<>();
		JSONArray coleccionObjetos = obtenerArrayJSON(globalJSON.get("objetos").toString());
		for (int i = 0; i < coleccionObjetos.size(); i++) {
			JSONObject datosObjeto = obtenerObjetoJSON(coleccionObjetos.get(i).toString());
			
			int idObjeto = obtenerIntDesdeJSON(datosObjeto, "id");
			int cantidadObjeto = obtenerIntDesdeJSON(datosObjeto, "cantidad");
			int xObjeto = obtenerIntDesdeJSON(datosObjeto, "x");
			int yObjeto = obtenerIntDesdeJSON(datosObjeto, "y");
			
			Point posicionObjeto = new Point(xObjeto, yObjeto);
			Objeto objeto = RegistroObjetos.obtenerObjeto(idObjeto);
			ObjetoUnicoTiled objetoUnico = new ObjetoUnicoTiled(posicionObjeto, objeto);
			objetosMapa.add(objetoUnico);
		}
		
		//OBTENER ENEMIGOS
		enemigosMapa = new ArrayList<>();
		JSONArray coleccionEnemigos = obtenerArrayJSON(globalJSON.get("enemigos").toString());
		for (int i = 0; i < coleccionEnemigos.size(); i++) {
			JSONObject datosEnemigo = obtenerObjetoJSON(coleccionEnemigos.get(i).toString());
			
			int idEnemigo = obtenerIntDesdeJSON(datosEnemigo, "id");
			int xEnemigo = obtenerIntDesdeJSON(datosEnemigo, "x");
			int yEnemigo = obtenerIntDesdeJSON(datosEnemigo, "y");
			
			Point posicionEnemigo = new Point(xEnemigo, yEnemigo);
			Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(idEnemigo);
			enemigo.establecerPosicion(posicionEnemigo.x, posicionEnemigo.y);
			
			enemigosMapa.add(enemigo);
		}
		
		areasColisionPorActualizacion = new ArrayList<>();
	}
	
	public void actualizar() {
		actualizarAreasColision();
		actualizarRecogidaObjetos();
		actualizarEnemigos();
		actualizarAtaques();
		
		Point punto = new Point(ElementosPrincipales.jugador.obtenerPosicionXInt(),
				ElementosPrincipales.jugador.obtenerPosicionYInt());
		Point puntoCoincidente = d.obtenerCoordenadasNodoCoincidente(punto);
		d.reiniciarYEvaluar(puntoCoincidente);
	}
	
	private void actualizarAreasColision() {
		if (!areasColisionPorActualizacion.isEmpty()) {
			areasColisionPorActualizacion.clear();
		}
		
		for (int i = 0; i < areasColisionOriginales.size(); i++) {
			Rectangle rInicial = areasColisionOriginales.get(i);
			
			int puntoX = rInicial.x - (int) ElementosPrincipales.jugador.obtenerPosicionX() + Constantes.MARGEN_X;
			int puntoY = rInicial.y - (int) ElementosPrincipales.jugador.obtenerPosicionY() + Constantes.MARGEN_Y;
			
			final Rectangle rFinal = new Rectangle(puntoX, puntoY, rInicial.width, rInicial.height);
			
			areasColisionPorActualizacion.add(rFinal);
		}
	}
	
	private void actualizarRecogidaObjetos() {
		if (!objetosMapa.isEmpty()) {
            final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.obtenerPosicionXInt(),
                    ElementosPrincipales.jugador.obtenerPosicionYInt(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            for (int i = 0; i < objetosMapa.size(); i++) {
                final ObjetoUnicoTiled objetoActual = objetosMapa.get(i);

                final Rectangle posicionObjetoActual = new Rectangle(
                        objetoActual.obtenerPosicion().x,
                        objetoActual.obtenerPosicion().y, Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE);

                if (areaJugador.intersects(posicionObjetoActual) && GestorControles.teclado.recogiendo) {
                    ElementosPrincipales.inventario.recogerObjetos(objetoActual);
                    objetosMapa.remove(i);
                }
            }
        }
	}
	
	private void actualizarEnemigos() {
		if (!enemigosMapa.isEmpty()) {
			for (Enemigo enemigo : enemigosMapa) {
				enemigo.cambiarSiguienteNodo(d.encontrarSiguienteNodoParaEnemigo(enemigo));
				enemigo.actualizar(enemigosMapa);
			}
		}
	}
	
	private void actualizarAtaques() {

        if (enemigosMapa.isEmpty()
                || ElementosPrincipales.jugador.obtenerAlcanceActual().isEmpty()
                || ElementosPrincipales.jugador.obtenerAlmacenEquipo().obtenerArma1() instanceof Desarmado) {
            return;
        }
       

        if (GestorControles.teclado.atacando) {     	 
            ArrayList<Enemigo> enemigosAlcanzados = new ArrayList<>();

            if (ElementosPrincipales.jugador.obtenerAlmacenEquipo().obtenerArma1().esPenetrante()) {
                for (Enemigo enemigo : enemigosMapa) {
                	System.out.println(ElementosPrincipales.jugador.obtenerAlcanceActual().get(0));
                	System.out.println(enemigo.obtenerAreaPosicional());
                    if (ElementosPrincipales.jugador.obtenerAlcanceActual().get(0).intersects(enemigo.obtenerArea())) {
                        System.out.println("impacto");
                    	enemigosAlcanzados.add(enemigo);
                    }
                }
            } else {
                Enemigo enemigoMasCercano = null;
                Double distanciaMasCercana = null;

                for (Enemigo enemigo : enemigosMapa) {
                    if (ElementosPrincipales.jugador.obtenerAlcanceActual().get(0).intersects(enemigo.obtenerArea())) {
                        Point puntoJugador = new Point((int) ElementosPrincipales.jugador.obtenerPosicionX() / 32, (int) ElementosPrincipales.jugador.obtenerPosicionY() / 32);
                        Point puntoEnemigo = new Point((int) enemigo.obtenerPosicionX(), (int) enemigo.obtenerPosicionY());

                        Double distanciaActual = CalculadoraDistancia.obtenerDistanciaEntrePuntos(puntoJugador, puntoEnemigo);

                        if (enemigoMasCercano == null) {
                            enemigoMasCercano = enemigo;
                            distanciaMasCercana = distanciaActual;
                        } else if (distanciaActual < distanciaMasCercana) {
                            enemigoMasCercano = enemigo;
                            distanciaMasCercana = distanciaActual;
                        }

                    }
                }
                enemigosAlcanzados.add(enemigoMasCercano);
            }
            System.out.println(enemigosAlcanzados.size());
            ElementosPrincipales.jugador.obtenerAlmacenEquipo().obtenerArma1().atacar(enemigosAlcanzados);
        }

        Iterator<Enemigo> iterador = enemigosMapa.iterator();

        while (iterador.hasNext()) {
            Enemigo enemigo = iterador.next();

            if (enemigo.obtenerVidaActual() <= 0) {
                iterador.remove();
                ElementosPrincipales.jugador.puntos += 100;
            }
        }
    }
	
	public void dibujar(Graphics g) {
		for (int i = 0; i < capasSprites.size(); i++) {
			int[] spritesCapa = capasSprites.get(i).obtenerArraySprites();
			
			for (int y = 0; y < altoMapaEnTiles; y++) {
				for (int x = 0; x < anchoMapaEnTiles; x++) {
					int idSpriteActual = spritesCapa[x + y * anchoMapaEnTiles];
					if (idSpriteActual != -1) {
						int puntoX = x * Constantes.LADO_SPRITE
								- (int) ElementosPrincipales.jugador.obtenerPosicionX() + Constantes.MARGEN_X;
						int puntoY = y * Constantes.LADO_SPRITE
								- (int) ElementosPrincipales.jugador.obtenerPosicionY() + Constantes.MARGEN_Y;
						
						if (puntoX < 0 - Constantes.LADO_SPRITE ||
							puntoX > Constantes.ANCHO_JUEGO ||
							puntoY < 0 - Constantes.LADO_SPRITE ||
							puntoY > Constantes.ALTO_JUEGO - 65) {
							continue;
						}
						
						DibujoDebug.dibujarImagen(g, paletaSprites[idSpriteActual].obtenerImagen(),
								puntoX, puntoY);
					}
				}
			}
		}
		
		for (int i = 0; i < objetosMapa.size(); i++) {
			ObjetoUnicoTiled objetoActual = objetosMapa.get(i);
			
			int puntoX = objetoActual.obtenerPosicion().x
					- (int) ElementosPrincipales.jugador.obtenerPosicionX() + Constantes.MARGEN_X;
			int puntoY = objetoActual.obtenerPosicion().y
					- (int) ElementosPrincipales.jugador.obtenerPosicionY() + Constantes.MARGEN_Y;
			
			if (puntoX < 0 - Constantes.LADO_SPRITE ||
					puntoX > Constantes.ANCHO_JUEGO ||
					puntoY < 0 - Constantes.LADO_SPRITE ||
					puntoY > Constantes.ALTO_JUEGO - 65) {
					continue;
				}
			
			DibujoDebug.dibujarImagen(g, objetoActual.obtenerObjeto().obtenerSprite().obtenerImagen(),
					puntoX, puntoY);
		}
		
		for (int i = 0; i < enemigosMapa.size(); i++) {
			Enemigo enemigo = enemigosMapa.get(i);
			int puntoX = (int) enemigo.obtenerPosicionX()
					- (int) ElementosPrincipales.jugador.obtenerPosicionX() + Constantes.MARGEN_X;
			int puntoY = (int) enemigo.obtenerPosicionY()
					- (int) ElementosPrincipales.jugador.obtenerPosicionY() + Constantes.MARGEN_Y;
			
			if (puntoX < 0 - Constantes.LADO_SPRITE ||
					puntoX > Constantes.ANCHO_JUEGO ||
					puntoY < 0 - Constantes.LADO_SPRITE ||
					puntoY > Constantes.ALTO_JUEGO - 65) {
					continue;
				}
			
			enemigo.dibujar(g, puntoX, puntoY);
			//DibujoDebug.dibujarRectanguloContorno(g, puntoX, puntoY, 32, 32);
		}
	}
	
	private JSONObject obtenerObjetoJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONObject objetoJSON = null;
		
		try {
			Object recuperado = lector.parse(codigoJSON);
			objetoJSON = (JSONObject) recuperado;
		} catch(ParseException e) {
			System.out.println("Posicion: " + e.getPosition());
			System.out.println(e);
		}
		
		return objetoJSON;
	}
	
	private JSONArray obtenerArrayJSON(final String codigoJSON) {
		JSONParser lector = new JSONParser();
		JSONArray arrayJSON = null;
		
		try {
			Object recuperado = lector.parse(codigoJSON);
			arrayJSON = (JSONArray) recuperado;
		} catch(ParseException e) {
			System.out.println("Posicion: " + e.getPosition());
			System.out.println(e);
		}
		
		return arrayJSON;
	}
	
	private int obtenerIntDesdeJSON(final JSONObject objetoJSON, final String clave) {
		return Integer.parseInt(objetoJSON.get(clave).toString());
	}
	
	public Point obtenerPosicionInicial() {
		return puntoInicial;
	}
	
	public Rectangle obtenerBordes(final int posicionX, final int posicionY) {
		int x = Constantes.MARGEN_X - posicionX + ElementosPrincipales.jugador.obtenerAncho();
        int y = Constantes.MARGEN_Y - posicionY + ElementosPrincipales.jugador.obtenerAlto();
        
        int ancho = this.anchoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerAncho() * 2;
        int alto = this.altoMapaEnTiles * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerAlto() * 2;

        return new Rectangle(x, y, ancho, alto);
	}
}