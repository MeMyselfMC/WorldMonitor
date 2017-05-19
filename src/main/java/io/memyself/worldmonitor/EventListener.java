package io.memyself.worldmonitor;

import org.bukkit.Bukkit;

import org.bukkit.World;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class EventListener
  implements Listener {
	
	private WorldMonitor wm;
	
	public EventListener(WorldMonitor instance) {
		wm = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!Utilities.getOversizeWorlds().isEmpty()) {
			Player player = event.getPlayer();
			
			if(player.hasPermission("worldmonitor.warn")) {
				if(wm.getConfig().getString("locale.world-warnings.player.on-join") != null && !wm.getConfig().getString("locale.world-warnings.player.on-join").isEmpty()) {
					String humanReadableOversizeWorldNamesSequence = new String();
					
					for(World world : Utilities.getOversizeWorlds()) {
						humanReadableOversizeWorldNamesSequence = humanReadableOversizeWorldNamesSequence + world.getName() + " (" + Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")) + ")";
						
						if(Utilities.getOversizeWorlds().size() > Utilities.getOversizeWorlds().lastIndexOf(world) + 1) humanReadableOversizeWorldNamesSequence = humanReadableOversizeWorldNamesSequence + ", ";
					}
					
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.world-warnings.player.on-join").replaceAll("%oversize_worlds%", humanReadableOversizeWorldNamesSequence)));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent event) {
		World world = event.getWorld();
		
		if(Utilities.getOversizeWorlds().contains(world)) {
			if(wm.getConfig().getString("locale.world-warnings.player.on-world-load") != null && !wm.getConfig().getString("locale.world-warnings.player.on-world-load").isEmpty()) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.hasPermission("worldmonitor.warn")) {
						String warningMsg = wm.getConfig().getString("locale.world-warnings.player.on-world-load");
						
						warningMsg = warningMsg.replaceAll("%world%", world.getName());
						warningMsg = warningMsg.replaceAll("%size_on_disk%", Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")));
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', warningMsg));
					}
				}
			}
			
			if(wm.getConfig().getString("locale.world-warnings.console.on-world-load") != null && !wm.getConfig().getString("locale.world-warnings.console.on-world-load").isEmpty()) {
				String consoleWarningMsg = wm.getConfig().getString("locale.world-warnings.console.on-world-load");
				
				consoleWarningMsg = consoleWarningMsg.replaceAll("%world%", world.getName());
				consoleWarningMsg = consoleWarningMsg.replaceAll("%size_on_disk%", Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")));
				
				wm.getLogger().warning(ChatColor.translateAlternateColorCodes('&', consoleWarningMsg));
			}
		}
	}
	
}