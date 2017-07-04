package gg.glyph;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlyphManager implements CommandExecutor {

	private List<Glyph> glyphs = new ArrayList<>();
	
	public GlyphManager(){
		
	}
	
	public boolean addGlyph(String name, String url){
		if(getGlyph(name) != null){
			return false;
		}
		Glyph glyph = new Glyph(name, url);
		if(glyph.generate()){
			this.glyphs.add(glyph);
			return true;
		}
		return false;
	}
	
	public Glyph getGlyph(String name){
		for(Glyph glyph : glyphs){
			if(glyph.getName().equalsIgnoreCase(name)){
				return glyph;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg1.getName().equalsIgnoreCase("createglyph")){
		if(arg3.length >= 2){
			if(addGlyph(arg3[0], arg3[1])){
				arg0.sendMessage(ChatColor.GREEN + "Success!");
			}else{
				arg0.sendMessage(ChatColor.RED + "Failure!");
			}
		}else{
			arg0.sendMessage(ChatColor.RED + "Invalid syntax! " + ChatColor.YELLOW + "/createglyph (name) (image url)");
		}
		return false;
		}
		if(arg1.getName().equalsIgnoreCase("glyph")){
			if(!(arg0 instanceof Player)){
				arg0.sendMessage(ChatColor.RED + "You are not a player!");
				return false;
			}
			Player player = (Player) arg0;
			if(arg3.length >= 1){
				Glyph glyph = getGlyph(arg3[0]);
				if(glyph != null){
					Location fake = player.getLocation().clone();
					fake.setPitch(0);
					glyph.display(player.getLocation().clone().add(fake.getDirection().normalize().multiply(8).toLocation(player.getWorld())).add(0,1,0), 10);
				}else{
					player.sendMessage(ChatColor.RED + "This glyph does not exist!");
				}
			}else{
				arg0.sendMessage(ChatColor.RED + "Invalid syntax! " + ChatColor.YELLOW + "/glyph (name)");
			}
		}
		return false;
	}
	
}
