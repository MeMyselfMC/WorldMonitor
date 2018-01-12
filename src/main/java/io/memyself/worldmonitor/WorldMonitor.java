package io.memyself.worldmonitor;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;
import org.bukkit.World;

import io.memyself.worldmonitor.bstats.MetricsLite;

public class WorldMonitor
  extends JavaPlugin {
	
	@Override
	public void onEnable() {
		if(!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
			
			reloadConfig();
		}
		new Utilities(this);
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		getCommand("worldmonitor").setExecutor(new CommandManager(this));
		
		if(getConfig().getBoolean("options.metrics")) {
			if(getConfig().getBoolean("options.debug")) getLogger().info("[DEBUG] Will be attempting to submit statistics to bStats.org.");
			
			new MetricsLite(this);
		}
		
		getLogger().info("WorldMonitor v" + getDescription().getVersion() + " has been enabled!");
		
		if(!Utilities.getOversizeWorlds().isEmpty()){
			if(getConfig().getString("locale.world-warnings.console.on-plugin-load") != null && !getConfig().getString("locale.world-warnings.console.on-plugin-load").isEmpty()) {
				String humanReadableOversizeWorldNamesSequence = new String();
				
				for(World world : Utilities.getOversizeWorlds()) {
					humanReadableOversizeWorldNamesSequence = humanReadableOversizeWorldNamesSequence + world.getName() + " (" + Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), getConfig().getBoolean("options.si")) + ")";
					
					if(Utilities.getOversizeWorlds().size() > Utilities.getOversizeWorlds().lastIndexOf(world) + 1) humanReadableOversizeWorldNamesSequence = humanReadableOversizeWorldNamesSequence + ", ";
				}
				
				getLogger().warning(ChatColor.translateAlternateColorCodes('&', getConfig().getString("locale.world-warnings.console.on-plugin-load").replaceAll("%oversize_worlds%", humanReadableOversizeWorldNamesSequence)));
			}
		}
	}
	
	@Override
	public void onDisable() {
		getLogger().info("WorldMonitor v" + getDescription().getVersion() + " has been disabled.");
	}
	
}