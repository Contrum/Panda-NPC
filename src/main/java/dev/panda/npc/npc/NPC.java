package dev.panda.npc.npc;

import com.google.common.collect.Maps;
import dev.panda.npc.utilities.item.ItemUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.panda.npc.PandaNPC;
import dev.panda.npc.utilities.LocationUtil;

import java.util.Map;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class NPC {

    @Getter
    private static Map<String, NPC> npcs = Maps.newHashMap();
    private final Map<UUID, Integer> entities = Maps.newHashMap();

    private final String name;
    private String displayName;
    private Location location;
    private ItemStack helmet, chest, legs, boots, hand;
    private float yaw, headYaw, pitch;
    private Map<String, Boolean> commands = Maps.newHashMap();
    private String signature;
    private String texture;

    public static NPC getByName(String name) {
        return npcs.get(name);
    }

    public static NPC getByID(Player player, int id) {
        for (Map.Entry<String, NPC> entry : npcs.entrySet()) {
            NPC npc = entry.getValue();

            if (npc.getEntities().containsKey(player.getUniqueId()) && npc.getEntities().get(player.getUniqueId()) == id) {
                return npc;
            }
        }
        return null;
    }

    public void destroyAll() {
        Bukkit.getServer().getOnlinePlayers()
                .forEach(online -> PandaNPC.get().getNPCPacket().destroy(this, online));
    }

    public void spawnAll() {
        Bukkit.getServer().getOnlinePlayers()
                .forEach(online -> PandaNPC.get().getNPCPacket().spawn(this, online));
    }

    public void save() {
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs." + name);

        if (section == null) section = PandaNPC.get().getNpcsConfig().getConfiguration().createSection("npcs." + name);

        section.set("name", this.name);
        section.set("displayName", this.displayName);
        section.set("location", LocationUtil.serialize(this.location));

        if (this.helmet != null) section.set("helmet", ItemUtil.serialize(this.helmet));
        if (this.chest != null) section.set("chest", ItemUtil.serialize(this.chest));
        if (this.legs != null) section.set("legs", ItemUtil.serialize(this.legs));
        if (this.boots != null) section.set("boots", ItemUtil.serialize(this.boots));
        if (this.hand != null) section.set("hand", ItemUtil.serialize(this.hand));

        section.set("yaw", this.location.getYaw());
        section.set("headYaw", this.location.getYaw());
        section.set("pitch", this.location.getPitch());

        if (this.signature != null) {
            section.set("signature", signature);
        }
        else {
            section.set("signature", null);
        }
        if (this.texture != null) {
            section.set("texture", texture);
        }
        else {
            section.set("texture", null);
        }

        section.set("commands", null);

        if (!this.commands.isEmpty()) {
            section.set("commands", this.commands);
            int position = 0;
            for (String key : this.commands.keySet()) {
                position++;
                section.set("commands." + position + ".console", this.commands.get(key));
                section.set("commands." + position + ".command", key);
            }
        }

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
    }

    public void delete() {
        ConfigurationSection section =  PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");
        section.set(name, null);

        PandaNPC.get().getNpcsConfig().save();
        PandaNPC.get().getNpcsConfig().reload();
        npcs.remove(this.name);
    }

    public void reset() {
        destroyAll();
        spawnAll();
        save();
    }

    public static void loadNPCs() {
        ConfigurationSection section = PandaNPC.get().getNpcsConfig().getConfiguration().getConfigurationSection("npcs");

        if (section == null) return;

        section.getKeys(false).forEach(npc -> {
            String name = section.getString(npc + ".name");
            String displayName = section.getString(npc + ".displayName");

            String value = section.getString(npc + ".signature");
            String texture = section.getString(npc + ".texture");
            Location location = LocationUtil.deserialize(section.getString(npc + ".location"));

            ItemStack hand = null;

            if (section.getString(npc + ".hand") != null) hand = ItemUtil.deserialize(section.getString(npc + ".hand"));

            ItemStack helmet = null;

            if (section.getString(npc + ".helmet") != null) helmet = ItemUtil.deserialize(section.getString(npc + ".helmet"));

            ItemStack chest = null;

            if (section.getString(npc + ".chest") != null) chest = ItemUtil.deserialize(section.getString(npc + ".chest"));

            ItemStack legs = null;

            if (section.getString(npc + ".legs") != null) legs = ItemUtil.deserialize(section.getString(npc + ".legs"));

            ItemStack boots = null;

            if (section.getString(npc + ".boots") != null) boots = ItemUtil.deserialize(section.getString(npc + ".boots"));

            float yaw = (float) section.getDouble(npc + ".yaw");
            float headYaw = (float) section.getDouble(npc + ".headYaw");
            float pitch = (float) section.getDouble(npc + ".pitch");

            Map<String, Boolean> commands = Maps.newHashMap();

            if (section.getConfigurationSection(npc + ".commands") != null) {
                for (String position : section.getConfigurationSection(npc + ".commands").getKeys(false)) {
                    commands.put(section.getString(npc + ".commands." + position + ".command"),
                            section.getBoolean(npc + ".commands." + position + ".console"));
                }
            }

            npcs.put(name, new NPC(name, displayName, location, helmet, chest, legs, boots, hand, yaw, headYaw, pitch, commands, value, texture));
        });
    }
}