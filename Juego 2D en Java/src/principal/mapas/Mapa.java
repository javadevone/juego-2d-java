package principal.mapas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import principal.Constantes;
import principal.ElementosPrincipales;
import principal.control.GestorControles;
import principal.entes.Enemigo;
import principal.entes.RegistroEnemigos;
import principal.herramientas.CargadorRecursos;
import principal.herramientas.DibujoDebug;
import principal.inventario.ContenedorObjetos;
import principal.sprites.HojaSprites;
import principal.sprites.Sprite;

public class Mapa {

    private String[] partes;

    private final int ancho;
    private final int alto;

    private final Point posicionInicial;
    private final Point puntoSalida;

    private Rectangle zonaSalida;

    private String siguienteMapa;

    private final Sprite[] paleta;

    private final boolean[] colisiones;

    public final ArrayList<Rectangle> areasColision = new ArrayList<Rectangle>();
    public ArrayList<ContenedorObjetos> objetosMapa;
    public final ArrayList<Enemigo> enemigos;

    private final int[] sprites;

    private final int MARGEN_X = Constantes.ANCHO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;
    private final int MARGEN_Y = Constantes.ALTO_JUEGO / 2 - Constantes.LADO_SPRITE / 2;

    public Mapa(final String ruta) {

        String contenido = CargadorRecursos.leerArchivoTexto(ruta);

        partes = contenido.split("\\*");

        ancho = Integer.parseInt(partes[0]);
        alto = Integer.parseInt(partes[1]);

        String hojasUtilizadas = partes[2];
        String[] hojasSeparadas = hojasUtilizadas.split(",");

        // Lectura de la paleta de sprites
        String paletaEntera = partes[3];
        String[] partesPaleta = paletaEntera.split("#");

        // asignar sprites aqui
        paleta = asignarSprites(partesPaleta, hojasSeparadas);

        String colisionesEnteras = partes[4];
        colisiones = extraerColisiones(colisionesEnteras);

        String spritesEnteros = partes[5];
        String[] cadenasSprites = spritesEnteros.split(" ");

        sprites = extraerSprites(cadenasSprites);

        String posicion = partes[6];
        String[] posiciones = posicion.split("-");

        posicionInicial = new Point();
        posicionInicial.x = Integer.parseInt(posiciones[0]) * Constantes.LADO_SPRITE;
        posicionInicial.y = Integer.parseInt(posiciones[1]) * Constantes.LADO_SPRITE;

        String salida = partes[7];
        String[] datosSalida = salida.split("-");

        puntoSalida = new Point();
        puntoSalida.x = Integer.parseInt(datosSalida[0]);
        puntoSalida.y = Integer.parseInt(datosSalida[1]);
        siguienteMapa = datosSalida[2];

        zonaSalida = new Rectangle(0, 0, 0, 0);


        String informacionObjetos = partes[8];
        objetosMapa = asignarObjetos(informacionObjetos);

        String informacionEnemigos = partes[9];
        enemigos = asignarEnemigos(informacionEnemigos);
    }

    private ArrayList<Enemigo> asignarEnemigos(final String informacionEnemigos) {
        ArrayList<Enemigo> enemigos = new ArrayList<>();

        String[] informacionEnemigosSeparada = informacionEnemigos.split("#");
        for (int i = 0; i < informacionEnemigosSeparada.length; i++) {
            String[] informacionEnemigoActual = informacionEnemigosSeparada[i].split(":");
            String[] coordenadas = informacionEnemigoActual[0].split(",");
            String idEnemigo = informacionEnemigoActual[1];

            Enemigo enemigo = RegistroEnemigos.obtenerEnemigo(Integer.parseInt(idEnemigo));
            enemigo.establecerPosicion(Double.parseDouble(coordenadas[0]), Double.parseDouble(coordenadas[1]));
            enemigos.add(enemigo);
        }
        return enemigos;
    }

    private Sprite[] asignarSprites(final String[] partesPaleta, final String[] hojasSeparadas) {
        Sprite[] paleta = new Sprite[partesPaleta.length];

        HojaSprites hoja = new HojaSprites("/imagenes/hojasTexturas/" + hojasSeparadas[0] + ".png",
                Constantes.LADO_SPRITE, true);

        for (int i = 0; i < partesPaleta.length; i++) {
            String spriteTemporal = partesPaleta[i];

            String[] partesSprite = spriteTemporal.split("-");

            int indicePaleta = Integer.parseInt(partesSprite[0]);

            int indiceSpriteHoja = Integer.parseInt(partesSprite[2]);

            paleta[indicePaleta] = hoja.obtenerSprite(indiceSpriteHoja);
        }

        return paleta;
    }

    private boolean[] extraerColisiones(final String cadenaColisiones) {
        boolean[] colisiones = new boolean[cadenaColisiones.length()];

        for (int i = 0; i < cadenaColisiones.length(); i++) {
            if (cadenaColisiones.charAt(i) == '0') {
                colisiones[i] = false;
            } else {
                colisiones[i] = true;
            }
        }

        return colisiones;
    }

    private ArrayList<ContenedorObjetos> asignarObjetos(final String informacionObjetos) {
        final ArrayList<ContenedorObjetos> objetos = new ArrayList<ContenedorObjetos>();

        String[] contenedoresObjetos = informacionObjetos.split("#");

        for (String contenedoresIndividuales : contenedoresObjetos) {
            final ArrayList<Integer> idObjetos = new ArrayList<Integer>();
            final ArrayList<Integer> cantidadObjetos = new ArrayList<Integer>();

            final String[] divisionInformacionObjetos = contenedoresIndividuales.split(":");
            final String[] coordenadas = divisionInformacionObjetos[0].split(",");

            final Point posicionContenedor = new Point(Integer.parseInt(coordenadas[0]),
                    Integer.parseInt(coordenadas[1]));

            final String[] objetosCantidades = divisionInformacionObjetos[1].split("/");

            for (String objetoActual : objetosCantidades) {
                final String[] datosObjetoActual = objetoActual.split("-");

                idObjetos.add(Integer.parseInt(datosObjetoActual[0]));
                cantidadObjetos.add(Integer.parseInt(datosObjetoActual[1]));
            }

            final int[] idObjetosArray = new int[idObjetos.size()];
            final int[] cantidadObjetosArray = new int[cantidadObjetos.size()];

            for (int i = 0; i < idObjetosArray.length; i++) {
                idObjetosArray[i] = idObjetos.get(i);
                cantidadObjetosArray[i] = cantidadObjetos.get(i);
            }

            final ContenedorObjetos contenedor = new ContenedorObjetos(posicionContenedor, idObjetosArray,
                    cantidadObjetosArray);

            objetos.add(contenedor);
        }

        return objetos;
    }

    private int[] extraerSprites(final String[] cadenasSprites) {
        ArrayList<Integer> sprites = new ArrayList<Integer>();

        for (int i = 0; i < cadenasSprites.length; i++) {
            if (cadenasSprites[i].length() == 2) {
                sprites.add(Integer.parseInt(cadenasSprites[i]));
            } else {
                String uno = "";
                String dos = "";

                String error = cadenasSprites[i];

                uno += error.charAt(0);
                uno += error.charAt(1);

                dos += error.charAt(2);
                dos += error.charAt(3);

                sprites.add(Integer.parseInt(uno));
                sprites.add(Integer.parseInt(dos));
            }
        }

        int[] vectorSprites = new int[sprites.size()];

        for (int i = 0; i < sprites.size(); i++) {
            vectorSprites[i] = sprites.get(i);
        }

        return vectorSprites;
    }

    public void actualizar() {
        actualizarAreasColision();
        actualizarZonaSalida();
        actualizarRecogidaObjetos();
    }

    private void actualizarAreasColision() {
        if (!areasColision.isEmpty()) {
            areasColision.clear();
        }

        for (int y = 0; y < this.alto; y++) {
            for (int x = 0; x < this.ancho; x++) {
                int puntoX = x * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerPosicionXInt() + MARGEN_X;
                int puntoY = y * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerPosicionYInt() + MARGEN_Y;

                if (colisiones[x + y * this.ancho]) {
                    final Rectangle r = new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
                    areasColision.add(r);
                }
            }
        }
    }

    private void actualizarZonaSalida() {
        int puntoX = ((int) puntoSalida.getX()) * Constantes.LADO_SPRITE
                - ElementosPrincipales.jugador.obtenerPosicionXInt() + MARGEN_X;
        int puntoY = ((int) puntoSalida.getY()) * Constantes.LADO_SPRITE
                - ElementosPrincipales.jugador.obtenerPosicionYInt() + MARGEN_Y;

        zonaSalida = new Rectangle(puntoX, puntoY, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
    }

    private void actualizarRecogidaObjetos() {
        if (!objetosMapa.isEmpty()) {
            final Rectangle areaJugador = new Rectangle(ElementosPrincipales.jugador.obtenerPosicionXInt(),
                    ElementosPrincipales.jugador.obtenerPosicionYInt(), Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);

            for (int i = 0; i < objetosMapa.size(); i++) {
                final ContenedorObjetos contenedor = objetosMapa.get(i);

                final Rectangle posicionContenedor = new Rectangle(
                        contenedor.obtenerPosicion().x * Constantes.LADO_SPRITE,
                        contenedor.obtenerPosicion().y * Constantes.LADO_SPRITE, Constantes.LADO_SPRITE,
                        Constantes.LADO_SPRITE);

                if (areaJugador.intersects(posicionContenedor) && GestorControles.teclado.recogiendo) {
                    ElementosPrincipales.inventario.recogerObjetos(contenedor);
                    objetosMapa.remove(i);
                }
            }
        }
    }

    public void dibujar(Graphics g) {
    	
        for (int y = 0; y < this.alto; y++) {
            for (int x = 0; x < this.ancho; x++) {
                BufferedImage imagen = paleta[sprites[x + y * this.ancho]].obtenerImagen();

                int puntoX = x * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerPosicionXInt() + MARGEN_X;
                int puntoY = y * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerPosicionYInt() + MARGEN_Y;

                DibujoDebug.dibujarImagen(g, imagen, puntoX, puntoY);
            }
        }

        if (!objetosMapa.isEmpty()) {
            for (ContenedorObjetos contenedor : objetosMapa) {
                final int puntoX = contenedor.obtenerPosicion().x * Constantes.LADO_SPRITE
                        - ElementosPrincipales.jugador.obtenerPosicionXInt() + MARGEN_X;
                final int puntoY = contenedor.obtenerPosicion().y * Constantes.LADO_SPRITE
                        - ElementosPrincipales.jugador.obtenerPosicionYInt() + MARGEN_Y;

                contenedor.dibujar(g, puntoX, puntoY);
            }
        }

        if (!enemigos.isEmpty()) {
            for (Enemigo enemigo : enemigos) {
                final int puntoX = (int) enemigo.obtenerPosicionX() * Constantes.LADO_SPRITE
                        - (int) ElementosPrincipales.jugador.obtenerPosicionX() + MARGEN_X;
                final int puntoY = (int) enemigo.obtenerPosicionY() * Constantes.LADO_SPRITE
                        - (int) ElementosPrincipales.jugador.obtenerPosicionY() + MARGEN_Y;
                enemigo.dibujar(g, puntoX, puntoY);
            }
        }
    }

    public Rectangle obtenerBordes(final int posicionX, final int posicionY) {

        int x = MARGEN_X - posicionX + ElementosPrincipales.jugador.obtenerAncho();
        int y = MARGEN_Y - posicionY + ElementosPrincipales.jugador.obtenerAlto();
        
        int ancho = this.ancho * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerAncho() * 2;
        int alto = this.alto * Constantes.LADO_SPRITE - ElementosPrincipales.jugador.obtenerAlto() * 2;

        return new Rectangle(x, y, ancho, alto);
    }

    public Point obtenerPosicionInicial() {
        return posicionInicial;
    }

    public Point obtenerPuntoSalida() {
        return puntoSalida;
    }

    public String obtenerSiguienteMapa() {
        return siguienteMapa;
    }

    public Rectangle obtenerZonaSalida() {
        return zonaSalida;
    }
}