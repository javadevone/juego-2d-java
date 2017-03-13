package principal.mapas;

public abstract class CapaTiled {
	
	protected int ancho;
	protected int alto;
	protected int x;
	protected int y;
	
	public CapaTiled(int ancho, int alto, int x, int y) {
		this.ancho = ancho;
		this.alto = alto;
		this.x = x;
		this.y = y;
	}
}