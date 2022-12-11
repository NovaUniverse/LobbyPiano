package net.novauniverse.LobbyPiano;

import net.zeeraa.novacore.spigot.utils.VectorArea;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class LobbyPiano extends JavaPlugin implements Listener {
    VectorArea pianoArea = new VectorArea(
            new Vector(-158, 64, -129),
            new Vector(-173, 64, -132)
    );

    @EventHandler
    public void PlayerOnKeyEvent(PlayerMoveEvent event) {
        Location moveEventLocation = event.getFrom();
        Block blockBelowPlayer = new Location(moveEventLocation.getWorld(), moveEventLocation.getX(), moveEventLocation.getBlockY() - 1, moveEventLocation.getZ()).getBlock();

        if (pianoArea.isInside(blockBelowPlayer.getLocation().toVector())) {

        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println(ChatColor.YELLOW + "UwU" + ChatColor.WHITE + " - Piano Plugin");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
