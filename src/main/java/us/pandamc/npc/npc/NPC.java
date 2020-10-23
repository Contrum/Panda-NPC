package us.pandamc.npc.npc;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.utils.ItemUtils;
import us.pandamc.npc.utils.LocationUtil;

import java.util.Map;
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
    private float yaw;
    private float headYaw;
    private float pitch;
    private String command;

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


    public void save(){
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        section.set("name", this.name);
        section.set("displayName." + this.name, this.displayName);
        section.set("location." + this.name, LocationUtil.serialize(this.location));

        section.set("helmet." + this.name, ItemUtils.serialize(this.helmet));
        section.set("chest." + this.name, ItemUtils.serialize(this.chest));
        section.set("legs." + this.name, ItemUtils.serialize(this.legs));
        section.set("boots." + this.name, ItemUtils.serialize(this.boots));
        section.set("hand." + this.name, ItemUtils.serialize(this.hand));

        section.set("yaw." + this.name, this.yaw);
        section.set("headYaw." + this.name, this.headYaw);
        section.set("pitch." + this.name, this.pitch);

        section.set("command." + this.name, this.command);

        PandaNPC.get().getNpcsConfig().save();
    }

    public static void loadNPCs(){
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");
        section.getKeys(false).forEach(key -> {
            String name = section.getString(key + ".name");
            String displayName = section.getString(key + ".displayName");
            Location location = LocationUtil.deserialize(section.getString(key + ".location"));
            ItemStack hand = ItemUtils.deSerialized(section.getString(key + ".hand"));

            ItemStack helmet = ItemUtils.deSerialized(section.getString(key + ".helmet"));
            ItemStack chest = ItemUtils.deSerialized(section.getString(key + ".chest"));
            ItemStack legs = ItemUtils.deSerialized(section.getString(key + ".legs"));
            ItemStack boots = ItemUtils.deSerialized(section.getString(key + ".boots"));

            float yaw = (float) section.get(key + ".yaw");
            float headYaw = (float) section.get(key + ".headYaw");
            float pitch = (float) section.get(key + ".pitch");

            String command = section.getString(key + ".command");

            npcs.put(name, new NPC(name, displayName, location, helmet, chest, legs, boots, hand, yaw, headYaw, pitch, command));
        });
    }

}