package principal.dijkstra;

import java.awt.Point;
import java.awt.Rectangle;

import principal.Constantes;

public class Nodo {
	
	private Point posicion;
	private double distancia;
	
	public Nodo(final Point posicion, final double distancia) {
		this.posicion = posicion;
		this.distancia = distancia;
	}
	
	public Rectangle obtenerAreaPixeles() {
		return new Rectangle(posicion.x * Constantes.LADO_SPRITE, posicion.y * Constantes.LADO_SPRITE,
				Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
	}
	
	public Rectangle obtenerArea() {
		return new Rectangle(posicion.x, posicion.y, Constantes.LADO_SPRITE, Constantes.LADO_SPRITE);
	}
	
	public Point obtenerPosicion() {
		return posicion;
	}
	
	public void cambiarDistancia(double distancia) {
		this.distancia = distancia;
	}
	
	public double obtenerDistancia() {
		return distancia;
	}
}