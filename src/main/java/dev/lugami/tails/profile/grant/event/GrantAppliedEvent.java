//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.grant.event;

import dev.lugami.tails.profile.grant.Grant;
import dev.lugami.tails.util.BaseEvent;
import org.bukkit.entity.Player;

public class GrantAppliedEvent extends BaseEvent {
    private Player player;
    private Grant grant;

    public GrantAppliedEvent(final Player player, final Grant grant) {
        this.player = player;
        this.grant = grant;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Grant getGrant() {
        return this.grant;
    }
}
