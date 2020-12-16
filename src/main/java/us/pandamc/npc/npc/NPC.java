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

    private final String name, displayName;
    private final Location location;
    private ItemStack helmet, chest, legs, boots, hand;
    private float yaw, headYaw, pitch;
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

    public void save() {
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        section.set("name", this.name);
        section.set("displayName." + this.name, this.displayName);
        section.set("location." + this.name, LocationUtil.serialize(this.location));

        if (this.helmet != null) section.set("helmet." + this.name, ItemUtils.serialize(this.helmet));
        if (this.chest != null) section.set("chest." + this.name, ItemUtils.serialize(this.chest));
        if (this.legs != null) section.set("legs." + this.name, ItemUtils.serialize(this.legs));
        if (this.boots != null) section.set("boots." + this.name, ItemUtils.serialize(this.boots));
        if (this.hand != null) section.set("hand." + this.name, ItemUtils.serialize(this.hand));

        section.set("yaw." + this.name, this.location.getYaw());
        section.set("headYaw." + this.name, this.location.getYaw());
        section.set("pitch." + this.name, this.location.getPitch());

        if (this.command != null) section.set("command." + this.name, this.command);

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
    }

    public static void loadNPCs(){
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        if(section == null) return;

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