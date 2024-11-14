//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lugami.tails.util.json.JsonChain;
import java.util.UUID;

public class PacketClearGrants implements Packet {
    private UUID uuid;

    public int id() {
        return 13;
    }

    public JsonObject serialize() {
        return (new JsonChain()).addProperty("uuid", this.uuid.toString()).get();
    }

    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
    }

    public PacketClearGrants(final UUID uuid) {
        this.uuid = uuid;
    }

    public PacketClearGrants() {
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
