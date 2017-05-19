package io.memyself.worldmonitor;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager
  implements CommandExecutor {
	
	private WorldMonitor wm;
	
	public CommandManager(WorldMonitor instance) {
		wm = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("worldmonitor")) {
			if(args.length < 3) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
						if(sender.hasPermission("worldmonitor.reload")) {
							if(!new File(wm.getDataFolder(), "config.yml").exists()) {
								wm.saveDefaultConfig();
							}
							wm.reloadConfig();
							
							if(wm.getConfig().getBoolean("options.debug")) wm.getLogger().info("[DEBUG] Configuration reloaded!");
							
							if(wm.getConfig().getString("locale.config-reloaded") != null && !wm.getConfig().getString("locale.config-reloaded").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.config-reloaded")));
							}
						} else {
							if(wm.getConfig().getString("locale.error.no-permission") != null && !wm.getConfig().getString("locale.error.no-permission").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.no-permission")));
							}
						}
					} else if(args[0].equalsIgnoreCase("overall")) {
						if(sender.hasPermission("worldmonitor.overall")) {
							if(wm.getConfig().getString("locale.overall.header") != null && !wm.getConfig().getString("locale.overall.header").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.overall.header")));
							}
							
							for(World world : Bukkit.getWorlds()) {
								String entryMsg = wm.getConfig().getString("locale.overall.entry");
								
								if(entryMsg != null && !entryMsg.isEmpty()) {
									entryMsg = entryMsg.replaceAll("%world%", world.getName());
									entryMsg = entryMsg.replace("%size_on_disk%", Utilities.getOversizeWorlds().contains(world) ? ChatColor.valueOf(wm.getConfig().getString("options.oversize-world-size-formatting")) + Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")) : Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")));
									entryMsg = entryMsg.replaceAll("%loaded_chunk_count%", String.valueOf(world.getLoadedChunks().length));
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', entryMsg));
								}
							}
							
							if(wm.getConfig().getString("locale.overall.footer") != null && !wm.getConfig().getString("locale.overall.footer").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.overall.footer")));
							}
						} else {
							if(wm.getConfig().getString("locale.error.no-permission") != null && !wm.getConfig().getString("locale.error.no-permission").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.no-permission")));
							}
						}
					} else {
						if(wm.getConfig().getString("locale.error.invalid-arguments") != null && !wm.getConfig().getString("locale.error.invalid-arguments").isEmpty()) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.invalid-arguments")));
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("info")) {
						if(Bukkit.getWorld(args[1]) != null) {
							if(sender.hasPermission("worldmonitor.world.info")) {
								String infoMsg = wm.getConfig().getString("locale.info");
								
								if(infoMsg != null && !infoMsg.isEmpty()) {
									World world = Bukkit.getWorld(args[1]);
									
									infoMsg = infoMsg.replaceAll("%world%", world.getName());
									infoMsg = infoMsg.replaceAll("%uid%", world.getUID().toString());
									infoMsg = infoMsg.replaceAll("%size_on_disk%", Utilities.getOversizeWorlds().contains(world) ? ChatColor.valueOf(wm.getConfig().getString("options.oversize-world-size-formatting")) + Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")) : Utilities.getHumanReadableByteCount(Utilities.getDirectorySize(world.getWorldFolder().getAbsolutePath()), wm.getConfig().getBoolean("options.si")));
									infoMsg = infoMsg.replaceAll("%loaded_chunk_count%", String.valueOf(world.getLoadedChunks().length));
									infoMsg = infoMsg.replaceAll("%worldborder_size%", Utilities.getWorldBorderSize(world));
									infoMsg = infoMsg.replaceAll("%environment%", Utilities.getHumanReadableEnvironmentName(world.getEnvironment()));
									infoMsg = infoMsg.replaceAll("%type%", Utilities.getHumanReadableWorldType(world.getWorldType()));
									infoMsg = infoMsg.replaceAll("%seed%", String.valueOf(world.getSeed()));
									infoMsg = infoMsg.replaceAll("%difficulty%", Utilities.getHumanReadableDifficultyName(world.getDifficulty()));
									infoMsg = infoMsg.replaceAll("%entity_count%", String.valueOf(world.getEntities().size()));
									infoMsg = infoMsg.replaceAll("%living_entity_count%", String.valueOf(world.getLivingEntities().size()));
									infoMsg = infoMsg.replaceAll("%player_count%", String.valueOf(world.getPlayers().size()));
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', infoMsg));
								}
							} else {
								if(wm.getConfig().getString("locale.error.no-permission") != null && !wm.getConfig().getString("locale.error.no-permission").isEmpty()) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.no-permission")));
								}
							}
						} else {
							if(wm.getConfig().getString("locale.error.world-doesnt-exist") != null && !wm.getConfig().getString("locale.error.world-doesnt-exist").isEmpty()) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.world-doesnt-exist")));
							}
						}
					} else {
						if(wm.getConfig().getString("locale.error.invalid-arguments") != null && !wm.getConfig().getString("locale.error.invalid-arguments").isEmpty()) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.invalid-arguments")));
						}
					}
				} else {
					if(wm.getConfig().getString("locale.command-usage") != null && !wm.getConfig().getString("locale.command-usage").isEmpty()) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.command-usage")));
					}
				}
			} else {
				if(wm.getConfig().getString("locale.error.invalid-arguments") != null && !wm.getConfig().getString("locale.error.invalid-arguments").isEmpty()) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', wm.getConfig().getString("locale.error.invalid-arguments")));
				}
			}
		}
		return true;
	}
	
}