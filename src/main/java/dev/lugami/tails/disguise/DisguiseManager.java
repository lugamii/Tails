package dev.lugami.tails.disguise;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.lugami.tails.Tails;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.GameProfileUtil;
import dev.lugami.tails.util.UUIDFetcher;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DisguiseManager {

    @Getter
    private final Map<UUID, GameProfile> originalCache = new HashMap<>();

    private final Map<String, GameProfile> skinCache = new HashMap<>();

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void updateSkin(Player p) {
        try {
            if (!p.isOnline()){
                return;
            }
            CraftPlayer cp = (CraftPlayer) p;
            EntityPlayer ep = cp.getHandle();
            int entId = ep.getId();

            PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);

            PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);

            PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.getWorld().worldProvider.getDimension(),
                    ep.getWorld().getDifficulty(), ep.getWorld().worldData.getType(),
                    WorldSettings.EnumGamemode.getById(p.getGameMode().getValue()));

            PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0,
                    CraftItemStack.asNMSCopy(p.getItemInHand()));

            PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4,
                    CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));

            PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3,
                    CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));

            PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2,
                    CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));

            PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1,
                    CraftItemStack.asNMSCopy(p.getInventory().getBoots()));

            PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());

            for (Player inWorld : p.getWorld().getPlayers()) {
                final CraftPlayer craftOnline = (CraftPlayer) inWorld;
                PlayerConnection con = craftOnline.getHandle().playerConnection;
                if (inWorld.equals(p)) {
                    con.sendPacket(respawn);
                    con.sendPacket(slot);
                    craftOnline.updateScaledHealth();
                    craftOnline.getHandle().triggerHealthUpdate();
                    craftOnline.updateInventory();
                    Bukkit.getScheduler().runTask(Tails.get(), () -> craftOnline.getHandle().updateAbilities());
                    continue;
                }
                con.sendPacket(removeEntity);
                if (inWorld.canSee(p)){
                    con.sendPacket(addNamed);
                    con.sendPacket(itemhand);
                    con.sendPacket(helmet);
                    con.sendPacket(chestplate);
                    con.sendPacket(leggings);
                    con.sendPacket(boots);
                }
            }
        } catch (Exception e) { }

    }

    public boolean disguise(Player player, Rank rank, String displayName) {
        CompletableFuture<Map<String, UUID>> uuidFuture = CompletableFuture.supplyAsync(() -> {
            try {
                UUIDFetcher uuidFetcher = new UUIDFetcher(Collections.singletonList(displayName));
                return uuidFetcher.call();
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
                return Collections.emptyMap();
            }
        });

        // Continue with the rest of your logic asynchronously
        uuidFuture.thenAcceptAsync(fetched -> {
            try {
                GameProfile targetProfile;

                Optional<UUID> fetchedUuid = fetched.values().stream().findFirst();
                if (!fetchedUuid.isPresent()) {
                    // default skin to steve if doesn't exist
                    targetProfile = loadGameProfile(UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7"), "Steve");
                } else {
                    targetProfile = loadGameProfile(fetchedUuid.get(), displayName);
                }

                Profile profile = Profile.getByUuid(player.getUniqueId());

                profile.setDisguisedRank(rank);
                profile.setDisguisedName(displayName);

                // Make sure we don't cache another game profile that isn't actually theirs
                if (!originalCache.containsKey(player.getUniqueId())) {
                    originalCache.put(player.getUniqueId(), GameProfileUtil.clone(((CraftPlayer) player).getHandle().getProfile()));
                }

                new DisguiseTask(Tails.get(), player, targetProfile, displayName, rank).runTask(Tails.get());
                updateSkin(player);

                player.sendMessage(CC.WHITE + "You are now disguised as " + CC.GOLD + displayName + CC.WHITE + " with the rank " + rank.getColor() + rank.getDisplayName() + CC.WHITE + '.');
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
        });

        return true;
    }

    public void undisguise(Player player) {
        GameProfile originalProfile = this.originalCache.remove(player.getUniqueId());
        if (originalProfile != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile != null) {
                profile.setDisguisedRank(null);
                profile.setDisguisedName(null);
                new DisguiseTask(Tails.get(), player, originalProfile, originalProfile.getName(), profile.getActiveRank()).runTask(Tails.get());
            }

            player.setDisplayName(originalProfile.getName());
            updateSkin(player);
        }
    }

    private GameProfile loadGameProfile(UUID uniqueId, String skinName) {
        GameProfile profile = this.skinCache.get(skinName.toLowerCase());
        BufferedReader reader = null;

        try {
            if(profile == null || !profile.getProperties().containsKey("textures")) {
                URL url = new URL( "https://sessionserver.mojang.com/session/minecraft/profile/" + uniqueId.toString().replace("-", "") + "?unsigned=false");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("User-Agent", "Core");
                connection.setDoOutput(true);
                connection.connect();

                if(connection.getResponseCode() == 200) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    List<String> lines = Lists.newArrayList();

                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }

                    String response = Joiner.on("\n").join(lines);

                    JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                    skinName = object.get("name").getAsString();

                    if(profile == null) {
                        profile = new GameProfile(uniqueId, skinName);
                    }

                    JsonArray array = object.get("properties").getAsJsonArray();
                    for(Object obj : array) {
                        JsonObject property = (JsonObject) obj;
                        String propertyName = property.get("name").getAsString();

                        profile.getProperties().put(propertyName, new Property(propertyName, property.get("value").getAsString(), property.get("signature").getAsString()));
                    }

                    this.skinCache.put(skinName.toLowerCase(), profile);
                    MinecraftServer.getServer().getUserCache().a(profile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }

        return profile;
    }

    public boolean isDisguised(UUID uuid) {
        return this.originalCache.containsKey(uuid);
    }

    public boolean isDisguiseAllowed() {
        return true;
    }
}