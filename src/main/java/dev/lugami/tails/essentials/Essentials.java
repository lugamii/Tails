package dev.lugami.tails.essentials;

import dev.lugami.tails.Tails;
import dev.lugami.tails.essentials.event.SpawnTeleportEvent;
import dev.lugami.tails.util.LocationUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class Essentials {

	private Tails tails;
	private Location spawn;
	@Getter
	private List<List<String>> announcements = new ArrayList<>();

	public Essentials(Tails tails) {
		this.tails = tails;
		this.spawn = LocationUtil.deserialize(tails.getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
		setupTips();
	}

	public void setSpawn(Location location) {
		spawn = location;

		if (spawn == null) {
			tails.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
		} else {
			tails.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
		}

		try {
			tails.getMainConfig().getConfiguration().save(tails.getMainConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void teleportToSpawn(Player player) {
		Location location = spawn == null ? tails.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

		SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
		event.call();

		if (!event.isCancelled() && event.getLocation() != null) {
			player.teleport(event.getLocation());
		}
	}

	public int clearEntities(World world) {
		int removed = 0;

		for (Entity entity : world.getEntities()) {
			if (entity.getType() == EntityType.PLAYER) {
				continue;
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

	public int clearEntities(World world, EntityType... excluded) {
		int removed = 0;

		entityLoop:
		for (Entity entity : world.getEntities()) {
			if (entity instanceof Item) {
				removed++;
				entity.remove();
				continue entityLoop;
			}

			for (EntityType type : excluded) {
				if (entity.getType() == EntityType.PLAYER) {
					continue entityLoop;
				}

				if (entity.getType() == type) {
					continue entityLoop;
				}
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

	public void setupTips() {
		ConfigurationSection section = Tails.get().getMainConfig().getConfiguration().getConfigurationSection("BROADCASTS");

		if (section == null) return;

		this.announcements.clear();

		section.getKeys(false).forEach(key -> this.announcements.add(section.getStringList(key)));
	}

}
