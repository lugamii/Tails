//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util;

import dev.lugami.tails.Tails;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TipsTask extends BukkitRunnable {
    private int i = 0;

    public TipsTask() {
    }

    public void run() {
        List<List<String>> tips = Tails.get().getEssentials().getAnnouncements();
        if (!tips.isEmpty()) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                (tips.get(this.i)).forEach((tip) -> {
                    player.sendMessage(CC.translate(tip));
                });
            }

            ++this.i;
            if (this.i > tips.size() - 1) {
                this.i = 0;
            }

        }
    }
}
