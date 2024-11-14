//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.chat.event;

import dev.lugami.tails.chat.ChatAttempt;
import dev.lugami.tails.util.BaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ChatAttemptEvent extends BaseEvent implements Cancellable {
    private final Player player;
    private final ChatAttempt chatAttempt;
    private String chatMessage;
    private boolean cancelled;
    private String cancelReason = "";

    public ChatAttemptEvent(Player player, ChatAttempt chatAttempt, String chatMessage) {
        this.player = player;
        this.chatAttempt = chatAttempt;
        this.chatMessage = chatMessage;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ChatAttempt getChatAttempt() {
        return this.chatAttempt;
    }

    public String getChatMessage() {
        return this.chatMessage;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public void setChatMessage(final String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelReason(final String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
