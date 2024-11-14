//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util.duration;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

public class DurationTypeAdapter implements CommandTypeAdapter {
    public DurationTypeAdapter() {
    }

    public <T> T convert(String string, Class<T> type) {
        return type.cast(Duration.fromString(string));
    }
}
