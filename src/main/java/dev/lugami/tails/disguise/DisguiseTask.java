package dev.lugami.tails.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.lugami.tails.Tails;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.GameProfileUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

/**
 * @since 10/9/2017
 */
@RequiredArgsConstructor
public class DisguiseTask extends BukkitRunnable {

    private final Tails plugin;
    private final Player player;
    private final GameProfile newProfileData;
    private final String displayName;
    private final Rank rank;

    @Override
    public void run() {
        setPlayerNames();
        updatePlayerProfile();
        sendPlayerUpdate();
    }

    private void setPlayerNames() {
        player.setPlayerListName(displayName);
    }

    private void updatePlayerProfile() {
        final EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();

        try {
            Field field = EntityHuman.class.getDeclaredField("bH");
            field.setAccessible(true);

            GameProfile currentProfile = (GameProfile) field.get(entityPlayer);
            currentProfile.getProperties().clear();

            newProfileData.getProperties().values().forEach(property ->
                    currentProfile.getProperties().put(property.getName(), property));

            GameProfileUtil.setName(currentProfile, this.displayName);
            field.set(entityPlayer, currentProfile);

        } catch (Exception e) {
            // Log the exception using a logging framework
            e.printStackTrace();
        }
    }

    private void sendPlayerUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendUpdateToPlayer();

                plugin.getServer().getOnlinePlayers().stream()
                        .filter(other -> !other.equals(player))
                        .filter(other -> other.canSee(player))
                        .forEach(other -> {
                            other.hidePlayer(player);
                            other.showPlayer(player);
                        });

                Profile profile = Profile.getByUuid(player.getUniqueId());
                if (profile == null) {
                    return;
                }
            }
        }.runTask(this.plugin);
    }

    private void sendUpdateToPlayer() {
        final Entity vehicle = this.player.getVehicle();
        if (vehicle != null) {
            vehicle.eject();
        }

        this.sendPackets();

        this.player.updateInventory();
        this.player.setGameMode(this.player.getGameMode());

        PlayerInventory inventory = this.player.getInventory();
        inventory.setHeldItemSlot(inventory.getHeldItemSlot());

        double oldHealth = this.player.getHealth();

        int oldFood = this.player.getFoodLevel();
        float oldSat = this.player.getSaturation();
        this.player.setFoodLevel(20);
        this.player.setFoodLevel(oldFood);
        this.player.setSaturation(5.0F);
        this.player.setSaturation(oldSat);

        this.player.setMaxHealth(this.player.getMaxHealth());

        this.player.setHealth(20.0F);
        this.player.setHealth(oldHealth);

        float experience = this.player.getExp();
        int totalExperience = this.player.getTotalExperience();
        this.player.setExp(experience);
        this.player.setTotalExperience(totalExperience);

        this.player.setWalkSpeed(this.player.getWalkSpeed());
        this.player.setDisplayName(CC.translate(rank.getPrefix()) + rank.getColor() + this.displayName);
    }

    private void sendPackets() {
        final EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();
        Location previousLocation = this.player.getLocation().clone();

        entityPlayer.playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        entityPlayer.playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
        entityPlayer.playerConnection.sendPacket(
                new PacketPlayOutRespawn(entityPlayer.getWorld().worldProvider.getDimension(),
                        entityPlayer.getWorld().worldData.getDifficulty(),
                        entityPlayer.getWorld().worldData.getType(),
                        WorldSettings.EnumGamemode.valueOf(entityPlayer.getBukkitEntity().getGameMode().name())));
        this.player.teleport(previousLocation);
    }
}

