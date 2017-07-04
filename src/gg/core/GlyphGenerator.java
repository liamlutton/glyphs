package gg.core;

import gg.glyph.GlyphManager;

import org.bukkit.plugin.java.JavaPlugin;

public class GlyphGenerator extends JavaPlugin {

	private static GlyphGenerator instance;
	private GlyphManager glyphManager;
	
	@Override
	public void onEnable(){
		instance = this;
		this.glyphManager = new GlyphManager();
		
		getCommand("glyph").setExecutor(glyphManager);
		getCommand("createglyph").setExecutor(glyphManager);
	}
	
	public static GlyphGenerator getInstance(){
		return instance;
	}
	
	public GlyphManager getGlyphManager(){
		return glyphManager;
	}
	
}
