//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.option;

public class ProfileOptions {
    private boolean publicChatEnabled = true;
    private boolean receivingNewConversations = true;
    private boolean playingMessageSounds = true;

    public ProfileOptions() {
    }

    public boolean publicChatEnabled() {
        return this.publicChatEnabled;
    }

    public ProfileOptions publicChatEnabled(final boolean publicChatEnabled) {
        this.publicChatEnabled = publicChatEnabled;
        return this;
    }

    public boolean receivingNewConversations() {
        return this.receivingNewConversations;
    }

    public ProfileOptions receivingNewConversations(final boolean receivingNewConversations) {
        this.receivingNewConversations = receivingNewConversations;
        return this;
    }

    public boolean playingMessageSounds() {
        return this.playingMessageSounds;
    }

    public ProfileOptions playingMessageSounds(final boolean playingMessageSounds) {
        this.playingMessageSounds = playingMessageSounds;
        return this;
    }
}
