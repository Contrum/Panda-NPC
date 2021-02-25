package us.pandamc.npc.npc.select;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.ItemBuilder;

import java.util.Map;
import java.util.UUID;

public class NPCSelect {

    private static Map<UUID, NPC> npcSelected = Maps.newHashMap();

    public static void select(Player player, NPC npc){
        npcSelected.put(player.getUniqueId(), npc);
    }

    public static NPC getSelectedNPC(Player player){
        return npcSelected.get(player.getUniqueId());
    }

    public static void unSelect(Player player){
        npcSelected.remove(player.getUniqueId());
    }

    public static ItemStack selectItem(){
        return new ItemBuilder(Material.STICK).name("&bNpc Selector").build();
    }


}