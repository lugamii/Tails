package dev.lugami.tails.handler;

import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CustomLocation;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PacketHandler extends ChannelDuplexHandler
{

    private Player player;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.handleSentPacket((Packet<?>) msg);
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.handleReceivedPacket((Packet<?>) msg);
        super.channelRead(ctx, msg);
    }

    public void handleReceivedPacket(final Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInFlying")) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            handleFlyPacket((PacketPlayInFlying) packet, profile);
        }
    }

    private void handleSentPacket(final Packet<?> packet) {
        try {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutEntityTeleport"))
                handleTeleportPacket((PacketPlayOutEntityTeleport) packet, profile, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleTeleportPacket(final PacketPlayOutEntityTeleport packet, final Profile playerData, final Player player) {
        Map<String, Object> map = getAllFieldsFromPacket(packet);
        final Entity targetEntity = ((CraftPlayer)player).getHandle().getWorld().a((Integer) map.get("a"));
        if (targetEntity instanceof EntityPlayer) {
            final Player target = (Player)targetEntity.getBukkitEntity();
            double x = (Integer) map.get("b") / 32.0;
            final double y = (Integer) map.get("c") / 32.0;
            double z = (Integer) map.get("d") / 32.0;
            final float yaw = (Byte) map.get("e") * 360.0f / 256.0f;
            final float pitch = (Byte) map.get("f") * 360.0f / 256.0f;
            if (playerData.getMisplace() != 0.0) {
                final CustomLocation lastLocation = playerData.getLastMovePacket();
                final float entityYaw = this.getAngle(x, z, lastLocation);
                final double addX = Math.cos(Math.toRadians(entityYaw + 90.0f)) * playerData.getMisplace();
                final double addZ = Math.sin(Math.toRadians(entityYaw + 90.0f)) * playerData.getMisplace();
                x -= addX;
                z -= addZ;
                try {
                    Field field = packet.getClass().getDeclaredField("b");
                    field.setAccessible(true);
                    field.set(packet, MathHelper.floor(x * 32.0));

                    field = packet.getClass().getDeclaredField("d");
                    field.setAccessible(true);
                    field.set(packet, MathHelper.floor(z * 32.0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            playerData.addPlayerPacket(target.getUniqueId(), new CustomLocation(x, y, z, yaw, pitch));
        }
    }

    public static Map<String, Object> getAllFieldsFromPacket(Object packet) {
        Map<String, Object> map = new HashMap<>();

        // Get the class of the packet object
        Class<?> clazz = packet.getClass();

        // Get all declared fields, including private ones
        Field[] fields = clazz.getDeclaredFields();

        // Iterate over all the fields
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            try {
                Object value = field.get(packet);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private void handleFlyPacket(final PacketPlayInFlying packet, final Profile playerData) {
        final CustomLocation customLocation = new CustomLocation(packet.a(), packet.b(), packet.c(), packet.d(), packet.e());
        final CustomLocation lastLocation = playerData.getLastMovePacket();
        if (lastLocation != null) {
            if (!packet.g()) {
                customLocation.setX(lastLocation.getX());
                customLocation.setY(lastLocation.getY());
                customLocation.setZ(lastLocation.getZ());
            }
            if (!packet.h()) {
                customLocation.setYaw(lastLocation.getYaw());
                customLocation.setPitch(lastLocation.getPitch());
            }
        }
        playerData.setLastMovePacket(customLocation);
    }

    private float getAngle(final double posX, final double posZ, final CustomLocation location) {
        final double x = posX - location.getX();
        final double z = posZ - location.getZ();
        float newYaw = (float )Math.toDegrees(-Math.atan(x / z));
        double Z_X = Math.toDegrees(Math.atan(z / x));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Z_X);
        }
        else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Z_X);
        }
        return newYaw;
    }

}
