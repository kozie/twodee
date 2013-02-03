package nl.kozie.twodee;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import nl.kozie.twodee.gfx.Spritesheet;

public class Resources {
	
	public static final byte RESOURCE_TYPE_IMAGE = 0x01;
	public static final byte RESOURCE_TYPE_FONT = 0x02;
	public static final byte RESOURCE_TYPE_SFX = 0x04;
	public static final byte RESOURCE_TYPE_MUSIC = 0x08;
	
	public static Map<String, Spritesheet> spritesheets;
	public static Resources instance;
	
	protected Resources() {
		spritesheets = Collections.synchronizedMap(new HashMap<String, Spritesheet>());
	}
	
	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
		}
		
		return instance;
	}
	
	public static Spritesheet getSpritesheet(String key) {
		if (spritesheets.containsKey(key)) {
			return spritesheets.get(key);
		}
		
		return null;
	}
	
	public static void setSpritesheet(String key, Spritesheet sheet) {
		spritesheets.put(key, sheet);
	}
	
	public static BufferedImage getImage(String image) {
		
		InputStream resource = getResource(RESOURCE_TYPE_IMAGE, image);
		
		if (resource != null) {
			return getImage(resource);
		}
		
		return null;
	}
	
	public static BufferedImage getImage(InputStream image) {
		
		try {
			BufferedImage img = ImageIO.read(image);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static InputStream getSfx(String sfx) {
		return getResource(RESOURCE_TYPE_SFX, sfx);
	}
	
	public static InputStream getMusic(String music) {
		return getResource(RESOURCE_TYPE_MUSIC, music);
	}
	
	public static InputStream getResource(byte type, String file) {
		
		String folder = "";
		switch (type) {
		
			case RESOURCE_TYPE_IMAGE:
				folder = "/images/";
				break;
				
			case RESOURCE_TYPE_FONT:
				folder = "/fonts/";
				break;
				
			case RESOURCE_TYPE_SFX:
				folder = "sfx/";
				break;
				
			case RESOURCE_TYPE_MUSIC:
				folder = "music/";
				break;
		}
		
		return Resources.class.getResourceAsStream(folder + file);
	}
}