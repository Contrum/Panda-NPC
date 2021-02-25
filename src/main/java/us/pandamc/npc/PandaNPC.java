package us.pandamc.npc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import us.pandamc.npc.npc.select.NPCSelectListeners;
import us.pandamc.npc.utils.file.type.BasicConfigurationFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.pandamc.npc.commands.PandaNPCCommand;
import us.pandamc.npc.listeners.PlayerListeners;
import us.pandamc.npc.listeners.ProtocolListeners;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.cooldown.Cooldown;
import us.pandamc.npc.utils.nms.NPCPackets;
import us.pandamc.npc.utils.nms.impl.*;

import java.util.logging.Level;

@Getter
public class PandaNPC extends JavaPlugin {

    private NPCPackets packets;
    private BasicConfigurationFile npcsConfig;
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        npcsConfig = new BasicConfigurationFile(this, "npcs");

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        if(getVersion().equalsIgnoreCase("v1_8_R3")){
            packets = new NPCPackets1_8();
        }else if(getVersion().equalsIgnoreCase("v1_7_R4")){
            packets = new NPCPackets1_7();
        }else if(getVersion().equalsIgnoreCase("v1_12_R1")){
            packets = new NPCPackets1_12();
        }else if(getVersion().equalsIgnoreCase("v1_14_R1")){
            packets = new NPCPackets1_14();
        }else if(getVersion().equalsIgnoreCase("v1_15_R1")){
            packets = new NPCPackets1_15();
        }else if(getVersion().equalsIgnoreCase("v1_16_R3")){
            packets = new NPCPackets1_16();
        }else {
            Bukkit.getLogger().log(Level.SEVERE, "Spigot version not supported, please contact the developer. EnzoL_#8468");
        }

        registerListeners();
        registerCommands();

        NPC.loadNPCs();
        new Cooldown("npcfix", 10L);
    }

    private void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new NPCSelectListeners(), this);
        this.protocolManager.addPacketListener(new ProtocolListeners());
    }

    private void registerCommands() {
        getCommand("pandanpc").setExecutor(new PandaNPCCommand());
    }

    public String getVersion(){
        return getServer().getClass().getPackage().getName().substring(getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
    }

    public static PandaNPC get(){
        return getPlugin(PandaNPC.class);
    }
}