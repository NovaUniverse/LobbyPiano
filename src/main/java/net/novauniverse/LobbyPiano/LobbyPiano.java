package net.novauniverse.LobbyPiano;

import jdk.jfr.internal.LogLevel;
import net.zeeraa.novacore.commons.log.Log;
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
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() || event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
            return;
        }

        Location moveToEventLocation = event.getTo().clone().add(0, 0.5, 0);

        if (pianoPlayArea.isInsideBlock(moveToEventLocation.toVector())) {
            Log.trace("you are inside the uwu zone.");

            Location moveFromEventLocation = event.getFrom().clone().add(0, 0.5, 0);

            World theWorldUwU = moveFromEventLocation.getWorld();

            Block lastBlockBelowPlayer = moveFromEventLocation.getBlock().getRelative(BlockFace.DOWN);
            Block currentBlockBelowPlayer = moveToEventLocation.getBlock().getRelative(BlockFace.DOWN);

            DyeColor dyeColourForBlockBelow = getDyeColour(currentBlockBelowPlayer);

            if (dyeColourForBlockBelow == getDyeColour(lastBlockBelowPlayer) || dyeColourForBlockBelow == null) {
                Log.trace("same or not coloured");
                return;
            }

            switch(getDyeColour(currentBlockBelowPlayer)) {
                case LIGHT_BLUE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 0.707107F);
                    break;
                case CYAN:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 0.793701F);
                    break;
                case PURPLE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 0.890899F);
                    break;
                case ORANGE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 0.945874F);
                    break;
                case RED:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.059463F);
                    break;
                case PINK:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.189207F);
                    break;
                case MAGENTA:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.334840F);
                    break;
                case BLUE:
                    playNote(theWorldUwU, currentBlockBelowPlayer, 1.414214F);

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
            case LIGHT_BLUE:
                playNote(world, blockBelowPlayer, 0.707107F);
                break;
            case CYAN:
                playNote(world, blockBelowPlayer, 0.793701F);
                break;
            case PURPLE:
                playNote(world, blockBelowPlayer, 0.890899F);
                break;
            case ORANGE:
                playNote(world, blockBelowPlayer, 0.945874F);
                break;
            case RED:
                playNote(world, blockBelowPlayer, 1.059463F);
                break;
            case PINK:
                playNote(world, blockBelowPlayer, 1.189207F);
                break;
            case MAGENTA:
                playNote(world, blockBelowPlayer, 1.334840F);
                break;
            case BLUE:
                playNote(world, blockBelowPlayer, 1.414214F);
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
