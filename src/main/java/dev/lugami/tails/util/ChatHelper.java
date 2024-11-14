//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util;

import dev.lugami.tails.chat.util.ChatComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class ChatHelper {
    public ChatHelper() {
    }

    public static ClickEvent click(String command) {
        return new ClickEvent(Action.RUN_COMMAND, command);
    }

    public static ClickEvent suggest(String command) {
        return new ClickEvent(Action.SUGGEST_COMMAND, command);
    }

    public static HoverEvent hover(String text) {
        return new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ChatComponentBuilder("")).parse(text).create());
    }
}
