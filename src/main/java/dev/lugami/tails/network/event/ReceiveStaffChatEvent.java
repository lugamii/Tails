//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.network.event;

import dev.lugami.tails.util.BaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ReceiveStaffChatEvent extends BaseEvent implements Cancellable {
    private Player player;
    private boolean cancelled;

    public ReceiveStaffChatEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
