//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.chat.filter.impl;

import dev.lugami.tails.chat.filter.ChatFilter;

public class ContainsFilter extends ChatFilter {
    private final String phrase;

    public ContainsFilter(String phrase, String command) {
        super(command);
        this.phrase = phrase;
    }

    public boolean isFiltered(String message, String[] words) {
        String[] var3 = words;
        int var4 = words.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String word = var3[var5];
            if (word.contains(this.phrase)) {
                return true;
            }
        }

        return false;
    }
}
