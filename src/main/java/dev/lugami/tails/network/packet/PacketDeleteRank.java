//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lugami.tails.util.json.JsonChain;
import java.util.UUID;

public class PacketDeleteRank implements Packet {
    private UUID uuid;

    public PacketDeleteRank() {
    }

    public int id() {
        return 4;
    }

    public JsonObject serialize() {
        return (new JsonChain()).addProperty("uuid", this.uuid.toString()).get();
    }

    public void deserialize(JsonObject jsonObject) {
        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
    }

    public PacketDeleteRank(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
