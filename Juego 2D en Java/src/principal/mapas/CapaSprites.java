package principal.mapas;

public class CapaSprites extends CapaTiled {
	
	private int[] sprites;
	
	public CapaSprites(int ancho, int alto, int x, int y, int[] sprites) {
		super(ancho, alto, x, y);
		this.sprites = sprites;
	}
	
	public int[] obtenerArraySprites() {
		return sprites;
	}

}
