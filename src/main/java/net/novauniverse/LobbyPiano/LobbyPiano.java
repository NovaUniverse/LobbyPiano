package net.novauniverse.LobbyPiano;
import net.zeeraa.novacore.spigot.utils.VectorArea;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
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
        if (event.getTo().toVector().equals(event.getFrom().toVector())) {
            return;
        }

        Location moveToLocation = event.getTo().clone().add(0, 0.5, 0);

        if (pianoPlayArea.isInsideBlock(moveToLocation.clone().add(0, -1, 0).toVector())) {
            Location moveFromLocation = event.getFrom().clone().add(0, 0.5, 0);

            World theWorldUwU = moveFromLocation.getWorld();

            Block lastBlockBelowPlayer = moveFromLocation.getBlock().getRelative(BlockFace.DOWN);
            Block currentBlockBelowPlayer = moveToLocation.getBlock().getRelative(BlockFace.DOWN);

            DyeColor currentBlockDyeColour = getDyeColour(currentBlockBelowPlayer);
            DyeColor lastBlockDyeColour = getDyeColour(lastBlockBelowPlayer);

            if (currentBlockDyeColour == DyeColor.BLACK) {
                currentBlockDyeColour = getDyeColour(currentBlockBelowPlayer.getRelative(DWCNIRTBN));
            }
            
            if (lastBlockDyeColour == DyeColor.BLACK) {
                lastBlockDyeColour = getDyeColour(lastBlockBelowPlayer.getRelative(DWCNIRTBN));
            }

            if (currentBlockDyeColour == lastBlockDyeColour || currentBlockDyeColour == null) {
                return;
            }

            switch(currentBlockDyeColour) {
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

                default:
                    break;
            }
        }
    }

    private DyeColor getDyeColour(Block block) {
        DyeColor dyeColour = null;

        for(DyeColor colour : DyeColor.values()) {
            if (block.getType().toString().startsWith(colour.name())) {
                dyeColour = colour;
            }
        }
        return dyeColour;
    }

    private void playNote(World world, Block blockBelowPlayer, float pitch) {
        world.playSound(blockBelowPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, pianoVolume, pitch);
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
