package dev.lugami.tails.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lugami.tails.util.json.JsonChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketStaffReport implements Packet {

	private String sentBy;
	private String accused;
	private String reason;
	private String serverId;
	private String serverName;

	@Override
	public int id() {
		return 11;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("sentBy", sentBy)
				.addProperty("accused", accused)
				.addProperty("reason", reason)
				.addProperty("serverId", serverId)
				.addProperty("serverName", serverName)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		sentBy = object.get("sentBy").getAsString();
		accused = object.get("accused").getAsString();
		reason = object.get("reason").getAsString();
		serverId = object.get("serverId").getAsString();
		serverName = object.get("serverName").getAsString();
	}

}
