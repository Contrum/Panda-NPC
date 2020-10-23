package us.pandamc.npc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.file.type.BasicConfigurationFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.pandamc.npc.commands.PandaNPCCommand;
import us.pandamc.npc.listeners.PlayerListeners;
import us.pandamc.npc.listeners.ProtocolListeners;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.nms.NPCPackets;
import us.pandamc.npc.utils.nms.impl.NPCPackets1_8;

@Getter
public class PandaNPC extends JavaPlugin {

    private NPCPackets packets;
    private BasicConfigurationFile npcsConfig;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        npcsConfig = new BasicConfigurationFile(this, "npcs");

        packets = new NPCPackets1_8();

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        registerListeners();
        registerCommands();

        NPC.loadNPCs();
    }

    private void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(), this);
        this.protocolManager.addPacketListener(new ProtocolListeners());
    }

    private void registerCommands() {
        getCommand("pandanpc").setExecutor(new PandaNPCCommand());
    }

    public static PandaNPC get(){
        return getPlugin(PandaNPC.class);
    }
}