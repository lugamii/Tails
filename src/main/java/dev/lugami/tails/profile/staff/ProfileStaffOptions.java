//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.staff;

public class ProfileStaffOptions {
    private boolean staffModeEnabled = false;
    private boolean staffChatModeEnabled = false;

    public ProfileStaffOptions() {
    }

    public boolean staffModeEnabled() {
        return this.staffModeEnabled;
    }

    public ProfileStaffOptions staffModeEnabled(final boolean staffModeEnabled) {
        this.staffModeEnabled = staffModeEnabled;
        return this;
    }

    public boolean staffChatModeEnabled() {
        return this.staffChatModeEnabled;
    }

    public ProfileStaffOptions staffChatModeEnabled(final boolean staffChatModeEnabled) {
        this.staffChatModeEnabled = staffChatModeEnabled;
        return this;
    }
}
