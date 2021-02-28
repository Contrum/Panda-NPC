package us.pandamc.npc.npc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.utils.ItemUtils;
import us.pandamc.npc.utils.LocationUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class NPC {

    @Getter private static Map<String, NPC> npcs = Maps.newHashMap();
    private final Map<UUID, Integer> entitys = Maps.newHashMap();

    private final String name;
    private String displayName;
    private Location location;
    private ItemStack helmet, chest, legs, boots, hand;
    private float yaw, headYaw, pitch;
    private Map<Boolean, String> commands = Maps.newHashMap();
    private String signature;
    private String texture;

    public static NPC getByName(String name){
        return npcs.get(name);
    }

    public static NPC getByID(Player player, int id){
        for (Map.Entry<String, NPC> entry : npcs.entrySet()) {
            NPC npc = entry.getValue();
            if(npc.getEntitys().containsKey(player.getUniqueId()) && npc.getEntitys().get(player.getUniqueId()) == id){
                return npc;
            }
        }
        return null;
    }

    public void destroyAll(){
        Bukkit.getOnlinePlayers().forEach(player -> PandaNPC.get().getPackets().destroy(this, player));
    }

    public void spawnAll(){
        Bukkit.getOnlinePlayers().forEach(player -> PandaNPC.get().getPackets().spawn(this, player));
    }

    public void save() {
        ConfigurationSection section =  PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs." + name);

        if(section == null) section = PandaNPC.get().getNpcsConfig().getConfiguration().createSection("npcs." + name);

        section.set("name", this.name);
        section.set("displayName", this.displayName);
        section.set("location", LocationUtil.serialize(this.location));

        if (this.helmet != null) section.set("helmet", ItemUtils.serialize(this.helmet));
        if (this.chest != null) section.set("chest", ItemUtils.serialize(this.chest));
        if (this.legs != null) section.set("legs", ItemUtils.serialize(this.legs));
        if (this.boots != null) section.set("boots", ItemUtils.serialize(this.boots));
        if (this.hand != null) section.set("hand", ItemUtils.serialize(this.hand));

        section.set("yaw", this.location.getYaw());
        section.set("headYaw", this.location.getYaw());
        section.set("pitch", this.location.getPitch());

        if (this.signature != null) section.set("signature", signature);
        if (this.texture != null) section.set("texture", texture);

        if (!this.commands.isEmpty()) {
            section.set("commands", this.commands);
            int b = 0;
            for (Boolean key : this.commands.keySet()) {
                b++;
                section.set("commands." + b + ".boolean", key);
                section.set("commands." + b + ".command", this.commands.get(key));
            }
        }

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
    }

    public static void loadNPCs(){
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        if(section == null) return;

        section.getKeys(false).forEach(key -> {
            String name = section.getString(key + ".name");
            String displayName = section.getString(key + ".displayName");

            String value = section.getString(key + ".signature");
            String texture = section.getString(key + ".texture");
            Location location = LocationUtil.deserialize(section.getString(key + ".location"));

            ItemStack hand = null;

            if(section.getString(key + ".hand") != null) hand = ItemUtils.deSerialized(section.getString(key + ".hand"));

            ItemStack helmet = null;

            if(section.getString(key + ".helmet") != null) helmet = ItemUtils.deSerialized(section.getString(key + ".helmet"));

            ItemStack chest = null;

            if(section.getString(key + ".chest") != null) chest = ItemUtils.deSerialized(section.getString(key + ".chest"));

            ItemStack legs = null;

            if(section.getString(key + ".legs") != null) legs = ItemUtils.deSerialized(section.getString(key + ".legs"));

            ItemStack boots = null;

            if(section.getString(key + ".boots") != null) boots = ItemUtils.deSerialized(section.getString(key + ".boots"));

            float yaw = (float) section.getDouble(key + ".yaw");
            float headYaw = (float) section.getDouble(key + ".headYaw");
            float pitch = (float) section.getDouble(key + ".pitch");

            Map<Boolean, String> command = Maps.newHashMap();
            if (section.getConfigurationSection(key + ".commands") != null) {
                for (String s : section.getConfigurationSection(key + ".commands").getKeys(false)) {
                    command.put(section.getBoolean(key + ".commands." + s + ".boolean"),
                            section.getString(key + ".commands." + s + ".command"));
                }
            }

            npcs.put(name, new NPC(name, displayName, location, helmet, chest, legs, boots, hand, yaw, headYaw, pitch, command, value, texture));
        });
    }

    public void delete() {
        this.destroyAll();
        ConfigurationSection section =  PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");
        section.set(name, null);

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
        npcs.remove(this.name);
    }
}