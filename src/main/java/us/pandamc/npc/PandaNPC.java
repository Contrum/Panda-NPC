package us.pandamc.npc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.file.type.BasicConfigurationFile;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.pandamc.npc.commands.PandaNPCCommand;
import us.pandamc.npc.listeners.PlayerListeners;
import us.pandamc.npc.listeners.ProtocolListeners;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.ItemUtils;
import us.pandamc.npc.utils.LocationUtil;
import us.pandamc.npc.utils.nms.NPCPackets;
import us.pandamc.npc.utils.nms.impl.NPCPackets1_7;
import us.pandamc.npc.utils.nms.impl.NPCPackets1_8;

@Getter
public class PandaNPC extends JavaPlugin {

    private NPCPackets packets;
    private BasicConfigurationFile npcsConfig;
    private ProtocolManager protocolManager;
    private NPC npc;

    @Override
    public void onEnable() {
        npcsConfig = new BasicConfigurationFile(this, "npcs");

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        System.out.println("Version: " + getVersion());

        if(getVersion().equalsIgnoreCase("v1_8_R3")){
            packets = new NPCPackets1_8();
        }else if(getVersion().equalsIgnoreCase("v1_7_R4")){
            packets = new NPCPackets1_7();
        }

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
        registerCommand(new PandaNPCCommand(), getName());
    }

    private String getVersion(){
        return getServer().getClass().getPackage().getName().substring(getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
    }

    public static PandaNPC get(){
        return getPlugin(PandaNPC.class);
    }

    public void saveNPCs(NPC npc) {
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        section.set("name", npc.getName());
        section.set("displayName." + npc.getName(), npc.getDisplayName());
        section.set("location." + npc.getName(), LocationUtil.serialize(npc.getLocation()));

        section.set("helmet." + npc.getName(), ItemUtils.serialize(npc.getHelmet()));
        section.set("chest." + npc.getName(), ItemUtils.serialize(npc.getChest()));
        section.set("legs." + npc.getName(), ItemUtils.serialize(npc.getLegs()));
        section.set("boots." + npc.getName(), ItemUtils.serialize(npc.getBoots()));
        section.set("hand." + npc.getName(), ItemUtils.serialize(npc.getHand()));

        section.set("yaw." + npc.getName(), npc.getYaw());
        section.set("headYaw." + npc.getName(), npc.getHeadYaw());
        section.set("pitch." + npc.getName(), npc.getPitch());

        section.set("command." + npc.getName(), npc.getCommand());

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
    }

    private void registerCommand(Command cmd, String fallbackPrefix) {
        MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
    }
}