package gg.glyph;

import gg.core.GlyphGenerator;
import gg.utils.ParticleEffect;
import gg.utils.ParticleEffect.OrdinaryColor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Glyph {
	
	private String urlString;
	private String name;
	private BufferedImage image;
	private OrdinaryColor[][] colorModule;
	
	private final static int MAXIMUM_IMAGE_HEIGHT = 32;
	private final static int MAXIMUM_IMAGE_WIDTH = 32;
	
	public Glyph(String name, String url){
		this.name = name;
		this.urlString = url;
	}
	
	public String getName(){
		return name;
	}
	
	public void display(Location location, int time, int rps){
		for(int i = 0; i < time*rps; i++){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GlyphGenerator.getInstance(), new Runnable(){

				@Override
				public void run() {
					display(location);
				}
				
			}, i*(20/rps));
		}
	}
	
	public void display(Location location, int time){
		display(location, time, 5);
	}
	
	public void display(Location location){
		for(int x = 0; x < MAXIMUM_IMAGE_WIDTH; x++){
			for(int y = 0; y < MAXIMUM_IMAGE_HEIGHT; y++){
				if(colorModule[x][y] != null){
					double xcomp = Math.cos(yawToRadians(location.getYaw()))*((MAXIMUM_IMAGE_WIDTH - x)/10.0  - MAXIMUM_IMAGE_WIDTH/20.0);
					double zcomp = Math.sin(yawToRadians(location.getYaw()))*((MAXIMUM_IMAGE_WIDTH - x)/10.0  - MAXIMUM_IMAGE_WIDTH/20.0);
					summonRedstoneParticle(colorModule[x][y], location.clone().add(xcomp,  (MAXIMUM_IMAGE_HEIGHT - y)/10.0  - MAXIMUM_IMAGE_HEIGHT/20.0, zcomp), 50);
				}
			}
		}
	}
	
	private double yawToRadians(double yaw){
		if(yaw < 0){
			yaw += 360;
		}
		return (Math.PI*yaw)/180.0;
	}
	
	private void summonRedstoneParticle(OrdinaryColor particlecolor, Location loc, int range) {
		ParticleEffect.REDSTONE.display(particlecolor, loc, range);
	}
	
	private void load(){
		colorModule = new OrdinaryColor[MAXIMUM_IMAGE_HEIGHT][MAXIMUM_IMAGE_WIDTH];
		double ratio = (double)image.getWidth()/image.getHeight();
		double ax = 0, ay = 0;
		if(ratio == 1){
			ax = ay = image.getWidth()/((double)MAXIMUM_IMAGE_WIDTH);
		}else if(ratio < 1){
			ay = image.getHeight()/((double)MAXIMUM_IMAGE_HEIGHT);
			ax = ay / ratio;
		}else if(ratio > 1){
			ax = image.getWidth()/((double)MAXIMUM_IMAGE_WIDTH);
			ay = ax * ratio;
		}
		int sx = 0, sy = 0;
		for(double x = 0; x < image.getWidth(); x+=ax){
			sx++;
			sy = 0;
			for(double y = 0; y < image.getHeight(); y+=ay){
				sy++;
				Color color = new Color(image.getRGB((int)x, (int)y), true);
				if(color.getAlpha() != 0){
					colorModule[sx-1][sy-1] = new OrdinaryColor(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
				}
			}
		}
	}
	
	public boolean generate(){
		try {
			URL url = new URL(urlString);
			image = ImageIO.read(url);
			load();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
