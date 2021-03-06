package io.github.wolfleader116.playereffects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import io.github.wolfleader116.playereffects.commands.PlayerEffectsC;
import io.github.wolfleader116.playereffects.tabcompleters.PlayerEffectsTC;
import io.github.wolfleader116.wolfapi.ParticleEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerEffects extends JavaPlugin implements Listener {

	private static final Logger log = Logger.getLogger("Minecraft");

	public ArrayList<Projectile> arrows = new ArrayList<Projectile>();
	
	public HashMap<UUID, String> arrowdata = new HashMap<UUID, String>();

	public static PlayerEffects plugin;

	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		if (this.getConfig().getInt("Version") != 2) {
			File conf = new File(this.getDataFolder(), "config.yml");
			conf.delete();
			this.saveDefaultConfig();
			this.saveConfig();
			this.reloadConfig();
		}
		if (Bukkit.getServer().getPluginManager().getPlugin("WolfAPI") == null) {
			log.severe("WolfAPI was not found on the server! Disabling PlayerEffects!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("playereffects").setExecutor(new PlayerEffectsC());
		getCommand("playereffects").setTabCompleter(new PlayerEffectsTC());
		Set<String> effects = this.getConfig().getConfigurationSection("Effects").getKeys(false);
		for (final String effect : effects) {
			if (effect.endsWith("doublehelix")) {
				List<String> players = PlayerEffects.plugin.getConfig().getStringList("Effects." + effect);
				if (!(players.isEmpty())) {
					Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						public void run() {
							List<String> players = PlayerEffects.plugin.getConfig().getStringList("Effects." + effect);
							if (!(players.isEmpty())) {
								for (String player : players) {
									Config settings = new Config("../Settings/playerdata", PlayerEffects.plugin);
									Player p = Bukkit.getServer().getPlayer(player);
									if ((Bukkit.getServer().getPlayer(player) != null) && (settings.getConfig().getBoolean("effects." + p.getUniqueId().toString()) || settings.getConfig().getString("effects." + p.getUniqueId().toString()) == null)) {
										createDoubleHelix(Bukkit.getServer().getPlayer(player).getLocation(), ParticleEffect.valueOf((effect.replace("doublehelix", "")).toUpperCase()));
									}
								}
							}
						}
					}, 0, 12);
				}
			} else if (effect.endsWith("helix")) {
				List<String> players = PlayerEffects.plugin.getConfig().getStringList("Effects." + effect);
				if (!(players.isEmpty())) {
					Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						public void run() {
							List<String> players = PlayerEffects.plugin.getConfig().getStringList("Effects." + effect);
							if (!(players.isEmpty())) {
								for (String player : players) {
									Config settings = new Config("../Settings/playerdata", PlayerEffects.plugin);
									Player p = Bukkit.getServer().getPlayer(player);
									if ((Bukkit.getServer().getPlayer(player) != null) && (settings.getConfig().getBoolean("effects." + p.getUniqueId().toString()) || settings.getConfig().getString("effects." + p.getUniqueId().toString()) == null)) {
										createHelix(Bukkit.getServer().getPlayer(player).getLocation(), ParticleEffect.valueOf((effect.replace("helix", "")).toUpperCase()));
									}
								}
							}
						}
					}, 0, 12);
				}
			}
		}
	}

	@Override
	public void onDisable() {
		plugin = null;
	}
	
	public void createHelix(Location loc, final ParticleEffect effect) {
		int radius = 1;
		long da = (long) -1;
		for (double y = 0; y <= 2.5; y+=0.5) {
			da = (long) (da + 2.5);
			if (y > 4) {
				break;
			}
			double x = radius * Math.cos(y);
			double z = radius * Math.sin(y);
			final Location loca = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
			Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerEffects.plugin, new Runnable() {
				public void run() {
					try {
						effect.sendToPlayers(Bukkit.getOnlinePlayers(), loca, (float) 0, (float) 0, (float) 0, (float) 0, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, da);
		}
	}
	
	public void createDoubleHelix(Location loc, final ParticleEffect effect) {
		int radius = 1;
		long da = (long) -1;
		for (double y = 0; y <= 2.5; y+=0.5) {
			da = (long) (da + 2.5);
			if (y > 2.5) {
				break;
			}
			double x = radius * Math.cos(y);
			double z = radius * Math.sin(y);
			final Location loca = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
			Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerEffects.plugin, new Runnable() {
				public void run() {
					try {
						effect.sendToPlayers(Bukkit.getOnlinePlayers(), loca, (float) 0, (float) 0, (float) 0, (float) 0, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, da);
		}
		long db = (long) -1;
		for (double y = 0; y <= 2.5; y+=0.5) {
			db = (long) (db + 2.5);
			if (y > 2.5) {
				break;
			}
			double x = radius * Math.cos(y) * -1;
			double z = radius * Math.sin(y) * -1;
			final Location loca = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
			Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerEffects.plugin, new Runnable() {
				public void run() {
					try {
						effect.sendToPlayers(Bukkit.getOnlinePlayers(), loca, (float) 0, (float) 0, (float) 0, (float) 0, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, db);
		}
	}
}