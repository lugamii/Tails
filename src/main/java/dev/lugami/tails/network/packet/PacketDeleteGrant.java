package dev.lugami.tails.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lugami.tails.profile.grant.Grant;
import dev.lugami.tails.util.json.JsonChain;
import java.util.UUID;

import lombok.Getter;

public class PacketDeleteGrant implements Packet {

	@Getter private UUID playerUuid;
	@Getter private Grant grant;

	public PacketDeleteGrant() {

	}

	public PacketDeleteGrant(UUID playerUuid, Grant grant) {
		this.playerUuid = playerUuid;
		this.grant = grant;
	}

	@Override
	public int id() {
		return 3;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("playerUuid", playerUuid.toString())
				.add("grant", Grant.SERIALIZER.serialize(grant))
				.get();
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		playerUuid = UUID.fromString(jsonObject.get("playerUuid").getAsString());
		grant = Grant.DESERIALIZER.deserialize(jsonObject.get("grant").getAsJsonObject());
	}

}
