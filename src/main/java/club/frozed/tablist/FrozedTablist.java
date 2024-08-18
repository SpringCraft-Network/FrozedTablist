package club.frozed.tablist;

import club.frozed.tablist.adapter.TabAdapter;
import club.frozed.tablist.layout.TabLayout_v1_8;
import club.frozed.tablist.listener.TabListener;
import club.frozed.tablist.packet.TabPacket_v1_8;
import club.frozed.tablist.runnable.TabRunnable_v1_8;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class FrozedTablist {

    /*
    Forked from Hatsur API
    Links:
    -> https://github.com/norxir/seventab
    -> https://github.com/norxir/eighttab
     */

    @Getter private static FrozedTablist instance;

    private final TabAdapter adapter;

    private Version version;

    public FrozedTablist(JavaPlugin plugin, TabAdapter adapter, int delay1, int delay2) {
        instance = this;
        this.adapter = adapter;

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            this.version = Version.valueOf(version);
            plugin.getLogger().info("[Tab] Using " + this.version.name() + " version.");
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }

        handlerPacket(plugin);

        Bukkit.getServer().getPluginManager().registerEvents(new TabListener(this), plugin);
        if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new TabRunnable_v1_8(adapter), delay1, delay2); //TODO: async to run 1 millis
        }
    }

    private void handlerPacket(JavaPlugin plugin) {
        if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
            new TabPacket_v1_8(plugin);
        }
    }

    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            removePlayer(player);
        }
    }

    public void removePlayer(Player player) {
        boolean continueAt = false;
        if (Objects.requireNonNull(this.version) == Version.v1_8_R3) {
            if (TabLayout_v1_8.getLayoutMapping().containsKey(player.getUniqueId())) {
                continueAt = true;
            }

            if (continueAt) {
                TabLayout_v1_8.getLayoutMapping().remove(player.getUniqueId());
            }
        }
    }

    public enum Version {
        v1_7_R4,
        v1_8_R3
    }
}
