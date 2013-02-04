package nl.kozie.twodee.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.kozie.twodee.Manager;
import nl.kozie.twodee.entity.Entity;
import nl.kozie.twodee.gfx.Sprite;

public class World {
	
	public static final byte TILE_POS_BACK = 1;
	public static final byte TILE_POS_FRONT = 2;
	
	protected int width;
	protected int height;
	protected int tileWidth = 16;
	protected int tileHeight = 16;
	protected int scrollX = 0;
	protected int scrollY = 0;

	protected List<Block> bgTiles;
	protected List<Block> tiles;
	protected List<Entity> entities;
	
	protected Sprite baseTile;
	
	public World() {
		bgTiles = Collections.synchronizedList(new ArrayList<Block>());
		tiles = Collections.synchronizedList(new ArrayList<Block>());
		entities = Collections.synchronizedList(new ArrayList<Entity>());
	}
	
	public void init(int w, int h) {
		
		setWidth(w);
		setHeight(h);
		clear();
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
		int dw = Manager.getInstance().getDisplay().getWidth();
		int dh = Manager.getInstance().getDisplay().getHeight();
		
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
	
	public void addEntity(Entity ent) {
		entities.add(ent);
	}
	
	public void setBaseTile(Sprite s) {
		baseTile = s;
	}
	
	public void setTile(Block b) {
		setTile(b, TILE_POS_FRONT);
	}
	
	public void setTile(Block b, byte pos) {
		switch (pos) {
			case TILE_POS_BACK:
				bgTiles.add(b);
				break;
			
			case TILE_POS_FRONT:
				tiles.add(b);
				break;
		}
	}
	
	public void clear() {
		bgTiles.clear();
		tiles.clear();
		entities.clear();
		
		baseTile = null;
	}
}