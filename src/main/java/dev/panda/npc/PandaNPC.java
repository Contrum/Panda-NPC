package dev.panda.npc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.panda.npc.commands.NPCCommand;
import dev.panda.npc.commands.subcommands.*;
import dev.panda.npc.listeners.PlayerListener;
import dev.panda.npc.listeners.ProtocolListener;
import dev.panda.npc.npc.NPC;
import dev.panda.npc.npc.NPCManager;
import dev.panda.npc.npc.nms.NPCPacket;
import dev.panda.npc.npc.nms.impl.*;
import dev.panda.npc.utilities.command.CommandManager;
import dev.panda.npc.utilities.cooldown.Cooldown;
import dev.panda.npc.utilities.file.FileConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class PandaNPC extends JavaPlugin {

    private FileConfig npcsConfig;

    private ProtocolManager protocolManager;
    private CommandManager commandManager;

    private NPCManager npcManager;
    private NPCPacket NPCPacket;

    @Override
    public void onEnable() {
        this.npcsConfig = new FileConfig(this, "npcs.yml");

        loadManagers();
        loadListeners();
        loadCommands();
        loadVersion();

        NPC.loadNPCs();

        new Cooldown("NPCFix", 10L);
    }

    private void loadManagers() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.commandManager = new CommandManager(this);
        this.npcManager = new NPCManager();
    }

    private void loadListeners() {
        new PlayerListener(this);
        new ProtocolListener(protocolManager);
    }

    private void loadCommands() {
        new NPCCommand();
        new NPCCreateCommand(this);
        new NPCDeleteCommand(this);
        new NPCListCommand();
        new NPCSetDisplayNameCommand();
        new NPCSetSkinCommand();
        new NPCSetArmorCommand();
        new NPCTeleportCommand();
        new NPCTeleportHereCommand();
        new NPCExecuteCommand();
    }

    private void loadVersion() {
        if (Bukkit.getVersion().contains("1.7")) {
            NPCPacket = new NPCPacket1_7();
        }
        else if (Bukkit.getVersion().contains("1.8")) {
            NPCPacket = new NPCPacket1_8();
        }
        else if (Bukkit.getVersion().contains("1.9")) {
            NPCPacket = new NPCPacket1_9();
        }
        else if (Bukkit.getVersion().contains("1.10")) {
            NPCPacket = new NPCPacket1_10();
        }
        else if (Bukkit.getVersion().contains("1.11")) {
            NPCPacket = new NPCPacket1_11();
        }
        else if (Bukkit.getVersion().contains("1.12")) {
            NPCPacket = new NPCPacket1_12();
        }
        else if (Bukkit.getVersion().contains("1.13")) {
            NPCPacket = new NPCPacket1_13();
        }
        else if (Bukkit.getVersion().contains("1.14")) {
            NPCPacket = new NPCPacket1_14();
        }
        else if (Bukkit.getVersion().contains("1.15")) {
            NPCPacket = new NPCPacket1_15();
        }
        else if (Bukkit.getVersion().contains("1.16")) {
            NPCPacket = new NPCPacket1_16();
        }
        else {
            Bukkit.getLogger().log(Level.SEVERE, "Spigot version not supported.");
        }
    }

    public static PandaNPC get() {
        return getPlugin(PandaNPC.class);
    }
}