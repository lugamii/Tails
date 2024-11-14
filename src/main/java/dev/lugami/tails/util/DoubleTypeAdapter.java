//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

public class DoubleTypeAdapter implements CommandTypeAdapter {
    public DoubleTypeAdapter() {
    }

    public <T> T convert(String string, Class<T> type) {
        return type.cast(Double.parseDouble(string));
    }
}
