package dev.lugami.tails.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.lugami.tails.Tails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

public class RedisCache {

	private Tails tails;

	public RedisCache(Tails tails) {
		this.tails = tails;
	}

	public UUID getUuid(String name) {
		if (tails.getServer().isPrimaryThread()) {
			throw new IllegalStateException("Cannot query on main thread (Redis profile cache)");
		}

		try (Jedis jedis = tails.getJedisPool().getResource()) {
			String uuid = jedis.hget("uuid-cache:name-to-uuid", name.toLowerCase());

			if (uuid != null) {
				return UUID.fromString(uuid);
			}
		} catch (Exception e) {
			Bukkit.getLogger().info("Could not connect to redis");
			e.printStackTrace();
		}

		try {
			UUID uuid = getFromMojang(name);

			if (uuid != null) {
				updateNameAndUUID(name, uuid);
				return uuid;
			}
		} catch (Exception e) {
			Bukkit.getLogger().info("Could not fetch from Mojang API");
			e.printStackTrace();
		}

		return null;
	}

	public void updateNameAndUUID(String name, UUID uuid) {
		if (Bukkit.isPrimaryThread()) {
			throw new IllegalStateException("Cannot query redis on main thread!");
		}

		try (Jedis jedis = tails.getJedisPool().getResource()) {
			jedis.hset("uuid-cache:name-to-uuid", name.toLowerCase(), uuid.toString());
			jedis.hset("uuid-cache:uuid-to-name", uuid.toString(), name);
		}
	}

	public RedisPlayerData getPlayerData(UUID uuid) {
		if (Bukkit.isPrimaryThread()) {
			throw new IllegalStateException("Cannot query redis on main thread!");
		}

		try (Jedis jedis = tails.getJedisPool().getResource()) {
			String data = jedis.hget("player-data", uuid.toString());

			if (data == null) {
				return null;
			}

			try {
				JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
				return new RedisPlayerData(dataJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public void updatePlayerData(RedisPlayerData playerData) {
		try (Jedis jedis = tails.getJedisPool().getResource()) {
			jedis.hset("player-data", playerData.getUuid().toString(), playerData.getJson().toString());
		}
	}

	private static UUID getFromMojang(String name) throws IOException, ParseException {
		URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = reader.readLine();

		if (line == null) {
			return null;
		}

		String[] id = line.split(",");

		String part = id[0];
		part = part.substring(7, 39);

		return UUID.fromString(part.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
	}

}
