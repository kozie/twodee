package nl.kozie.twodee;

public interface Game {
	public void init();
	public void draw();
	public void tick(int delta);
}