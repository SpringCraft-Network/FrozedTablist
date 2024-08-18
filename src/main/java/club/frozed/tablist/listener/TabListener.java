package club.frozed.tablist.listener;

import club.frozed.tablist.FrozedTablist;
import club.frozed.tablist.layout.TabLayout_v1_8;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

@AllArgsConstructor
public class TabListener implements Listener {

    private FrozedTablist instance;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TabLayout_v1_8 tabLayout_v1_8;
        boolean validate = false;

        if (Objects.requireNonNull(FrozedTablist.getInstance().getVersion()) == FrozedTablist.Version.v1_8_R3) {
            tabLayout_v1_8 = new TabLayout_v1_8(instance, player);
            if (TabLayout_v1_8.getLayoutMapping().containsKey(player.getUniqueId())) {
                validate = true;
            }

            if (TabLayout_v1_8.getLayoutMapping().get(player.getUniqueId()) != null) {
                validate = true;
            }

            if (!validate) {
                tabLayout_v1_8.create();
                tabLayout_v1_8.setHeaderAndFooter();
            }

            TabLayout_v1_8.getLayoutMapping().put(player.getUniqueId(), tabLayout_v1_8);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        instance.removePlayer(player);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        instance.removePlayer(player);
    }
}
