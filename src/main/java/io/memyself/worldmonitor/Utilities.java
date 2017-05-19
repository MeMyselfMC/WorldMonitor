package io.memyself.worldmonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;

import org.bukkit.World.Environment;

import org.apache.commons.io.FileUtils;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;

public class Utilities {
	
	private static WorldMonitor wm;
	
	public Utilities(WorldMonitor instance) {
		wm = instance;
	}
	
	static long getDirectorySize(String path) {
		if(path != null) return FileUtils.sizeOfDirectory(new File(path));
		else return Long.MIN_VALUE;
	}
	
	static List<World> getOversizeWorlds() {
		List<World> oversizeWorlds = new ArrayList<World>();
		
		if(wm.getConfig().getLong("options.world-warnings.minimum-size-on-disk") >= 0) {
			for(World world : Bukkit.getWorlds()) {
				if(getDirectorySize(world.getWorldFolder().getAbsolutePath()) >= wm.getConfig().getLong("options.world-warnings.minimum-size-on-disk")) oversizeWorlds.add(world);
			}
		} else wm.getLogger().warning("Invalid minimum warning world size given in config.yml!");
		
		return oversizeWorlds;
	}
	
	static String getWorldBorderSize(World world) {
		if(Bukkit.getPluginManager().isPluginEnabled("WorldBorder")) {
			BorderData borderData = Config.Border(world.getName());
			
			if(borderData != null) {
				return String.valueOf("X: " + borderData.getRadiusX() + ", Z: " + borderData.getRadiusZ());
			} else return "-";
		} else return "-";
	}
	
	// http://programming.guide/java/formatting-byte-size-to-human-readable-format.html
	static String getHumanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		
		if(bytes < unit) return bytes + " B";
		
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	static String getHumanReadableEnvironmentName(Environment env) {
		if(env.equals(Environment.NORMAL)) return "Normal";
		else if(env.equals(Environment.NETHER)) return "Nether";
		else if(env.equals(Environment.THE_END)) return "End";
		else return "N/A";
	}
	
	static String getHumanReadableWorldType(WorldType type) {
		if(type.equals(WorldType.NORMAL)) return "Normal";
		else if(type.equals(WorldType.FLAT)) return "Flat";
		else if(type.equals(WorldType.LARGE_BIOMES)) return "Large Biomes";
		else if(type.equals(WorldType.AMPLIFIED)) return "Amplified";
		else if(type.equals(WorldType.CUSTOMIZED)) return "Customized";
		else if(type.equals(WorldType.VERSION_1_1)) return "Version 1.1";
		else return "N/A";
	}
	
	static String getHumanReadableDifficultyName(Difficulty diff) {
		if(diff.equals(Difficulty.PEACEFUL)) return "Peaceful";
		else if(diff.equals(Difficulty.EASY)) return "Easy";
		else if(diff.equals(Difficulty.NORMAL)) return "Normal";
		else if(diff.equals(Difficulty.HARD)) return "Hard";
		else return "N/A";
	}
	
}