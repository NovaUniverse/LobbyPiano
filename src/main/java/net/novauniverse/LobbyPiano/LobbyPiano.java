package net.novauniverse.LobbyPiano;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.utils.VectorArea;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;

public final class LobbyPiano extends JavaPlugin implements Listener {
    private static FileConfiguration config;
    VectorArea pianoPlayArea;

    // DWCNIRTBN =  Direction Which Coloured Note Is Relative To BlackNote
    BlockFace DWCNIRTBN;

    float pianoVolume;

    @EventHandler
    public void PlayerOnKeyEvent(PlayerMoveEvent event) {
        if (event.getTo().getX() == event.getFrom().getX() || event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }

        Location moveToEventLocation = event.getTo();
        Location moveFromEventLocation = event.getFrom();

        World theWorldUwU = moveFromEventLocation.getWorld();

        Block lastBlockBelowPlayer = moveFromEventLocation.getBlock().getRelative(BlockFace.DOWN);
        Block currentBlockBelowPlayer = moveToEventLocation.getBlock().getRelative(BlockFace.DOWN);

        if (pianoPlayArea.isInside(currentBlockBelowPlayer.getLocation().toVector())) {
            if (getDyeColour(currentBlockBelowPlayer) == getDyeColour(lastBlockBelowPlayer)) {
                return;
            }

            switch(getDyeColour(currentBlockBelowPlayer)) {
                case BLUE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.0F);
                    break;
                case CYAN:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.12246F);
                    break;
                case PURPLE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.25992F);
                    break;
                case ORANGE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.33484F);
                    break;
                case RED:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.49831F);
                    break;
                case PINK:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.68179F);
                    break;
                case MAGENTA:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.88775F);
                    break;

                case BLACK:
                    playBlackNotes(theWorldUwU, currentBlockBelowPlayer);
            }
        }
    }

    private DyeColor getDyeColour(Block block) {
        DyeColor dyeColour = null;

        for(DyeColor colour : DyeColor.values()) {
            if (block.getType().toString().contains(colour.name())) {
                dyeColour = colour;
            }
        }
        return dyeColour;
    }

    private void playNote(World world, Block blockBelowPlayer, float pitch) {
        world.playSound(blockBelowPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, pianoVolume, pitch);
    }

    private void playBlackNote(World world, Block block, float pitch) {
        world.playSound(block.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, pianoVolume, pitch);
    }

    private void playBlackNotes(World world, Block blockBelowPlayer) {

        switch (getDyeColour(blockBelowPlayer.getRelative(DWCNIRTBN))) {
            case BLUE:
                playBlackNote(world, blockBelowPlayer, 1.77239F);
                break;
            case CYAN:
                playBlackNote(world, blockBelowPlayer, 1.8016F);
                break;
            case PURPLE:
                playBlackNote(world, blockBelowPlayer, 1.84524F);
                break;
            case ORANGE:
                playBlackNote(world, blockBelowPlayer, 1.89022F);
                break;
            case RED:
                playBlackNote(world, blockBelowPlayer, 1.94586F);
                break;
            case PINK:
                playBlackNote(world, blockBelowPlayer, 2.05146F);
                break;
            case MAGENTA:
                playBlackNote(world, blockBelowPlayer, 2.12244F);
                break;
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic.
        getServer().getPluginManager().registerEvents(this, this);

        // Load and Set config.
        File configFile = new File(getConfig().getCurrentPath());
        if (!configFile.exists()) saveDefaultConfig();

        config = getConfig();

        ConfigurationSection pianoPlayAreaConfig = config.getConfigurationSection("PianoPlayArea");
        ConfigurationSection corner1 = (ConfigurationSection) pianoPlayAreaConfig.get("Corner1");
        ConfigurationSection corner2 = (ConfigurationSection) pianoPlayAreaConfig.get("Corner2");

        pianoPlayArea = new VectorArea(
                new Vector(corner1.getInt("x"), corner1.getInt("y"), corner1.getInt("z")),
                new Vector(corner2.getInt("x"), corner2.getInt("y"), corner2.getInt("z"))
        );

        DWCNIRTBN = BlockFace.valueOf(config.getString("DirectionWhichColouredNoteIsRelativeToBlackNote"));

        pianoVolume = (float) config.getDouble("PianoVolume");

        // Start Up Message (was meant for debugging but let's just keep it in here :wink:)
        System.out.println("UwU - Piano Plugin");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
