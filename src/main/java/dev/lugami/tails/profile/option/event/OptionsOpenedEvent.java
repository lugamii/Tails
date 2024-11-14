//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.option.event;

import dev.lugami.tails.profile.option.menu.ProfileOptionButton;
import dev.lugami.tails.util.BaseEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OptionsOpenedEvent extends BaseEvent {
    private final Player player;
    private List<ProfileOptionButton> buttons = new ArrayList();

    public OptionsOpenedEvent(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<ProfileOptionButton> getButtons() {
        return this.buttons;
    }
}
