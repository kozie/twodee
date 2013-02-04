package nl.kozie.twodee.world;

import java.util.ArrayList;
import java.util.List;

import nl.kozie.twodee.Manager;
import nl.kozie.twodee.entity.Entity;
import nl.kozie.twodee.gfx.Sprite;

public class World {
	
	protected int width;
	protected int height;
	protected int tileWidth = 16;
	protected int tileHeight = 16;
	protected int scrollX = 0;
	protected int scrollY = 0;

	protected List<Tile>[] tiles;
	protected List<Entity>[] entitiesInTiles;
	protected List<Entity> entities = new ArrayList<Entity>();
	
	protected Sprite baseTile;
	
	@SuppressWarnings("unchecked")
	public World(int w, int h) {
		width = w;
		height = h;
		
		tiles = new ArrayList[w * h];
		entitiesInTiles = new ArrayList[w * h];
		
		for (int i = 0; i < w * h; i++) {
			tiles[i] = new ArrayList<Tile>();
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
		
		init();
	}
	
	public void init() {
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public void scrollTo(int x, int y) {
		setScrollX(x);
		setScrollY(y);
	}
	
	public void scrollUp(int a) {
		setScrollY(scrollY - a);
	}
	
	public void scrollDown(int a) {
		setScrollY(scrollY + a);
	}

	public void scrollLeft(int a) {
		setScrollX(scrollX - a);
	}
	
	public void scrollRight(int a) {
		setScrollX(scrollX + a);
	}
	
	public void setScrollX(int x) {
	
		int dw = Manager.getInstance().getDisplay().getWidth();
		
		if (x > (width * tileWidth - dw)) { 
			scrollX = (width * tileWidth - dw);
		} else if (x < 0) {
			scrollX = 0;
		} else {
			scrollX = x;
		}
	}
	
	public void setScrollY(int y) {
		int dh = Manager.getInstance().getDisplay().getHeight();
		
		if (y > (height * tileHeight - dh)) { 
			scrollY = (height * tileHeight - dh);
		} else if (y < 0) {
			scrollY = 0;
		} else {
			scrollY = y;
		}
	}
	
	public void setTileSize(int s) {
		setTileSize(s, s);
	}
	
	public void setTileSize(int w, int h) {
		tileWidth = w;
		tileHeight = h;
	}
	
	public void draw() {
		int dw = Manager.getInstance().getDisplay().getOrigWidth();
		int dh = Manager.getInstance().getDisplay().getOrigHeight();
		
		if (baseTile != null) {
			for (int y = 0; y < ((int) dh / tileHeight + 1); y++) {
				for (int x = 0; x < ((int) dw / tileWidth + 1); x++) {
					int xx = x * tileWidth - (scrollX % tileWidth);
					int yy = y * tileHeight - (scrollY % tileHeight);
					
					baseTile.draw(xx, yy);
				}
			}
		}
	}
	
	public void tick(int delta) {
		
	}
	
	public Entity getEntityAt(int x, int y) {
		for (Entity e : entities) {
			if (e.getY() == y && e.getX() == x) {
				return e;
			}
		}
		
		return null;
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
	
	public void setBaseTile(Sprite s) {
		baseTile = s;
	}
	
	public void clear() {
		
		baseTile = null;
	}
}