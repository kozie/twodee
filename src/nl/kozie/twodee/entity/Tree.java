package nl.kozie.twodee.entity;

import java.util.Random;

import nl.kozie.twodee.Resources;
import nl.kozie.twodee.gfx.Sprite;

public class Tree extends Entity {

	protected int bit;
	
	public Tree(int x, int y) {
		super();
		setX(x);
		setY(y);
	}
	
	@Override
	public void init() {
		
		Random r = new Random();
		bit = r.nextInt(3);
		
		Sprite spr = Resources.getSpritesheet("main").getTile(3, 11);
		
		setSprite(spr);
		setWidth(spr.getWidth());
		setHeight(spr.getHeight());
	}

	@Override
	public void tick(int delta) {
		
	}

	@Override
	public void draw() {
		sprite.draw(x, y, bit);
	}
}