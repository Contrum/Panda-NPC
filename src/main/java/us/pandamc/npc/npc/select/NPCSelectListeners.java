package us.pandamc.npc.npc.select;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.pandamc.npc.npc.events.NPCInteractEvent;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.utils.CC;

public class NPCSelectListeners implements Listener {

    @EventHandler
    public void onInteractNPC(NPCInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null) return;
        if(item.getType() == Material.AIR) return;
        if(!item.equals(NPCSelect.selectItem())) return;

        NPC npc = event.getNpc();

        NPCSelect.select(player, npc);

        player.sendMessage(CC.translate("&aYou have selected &f" + npc.getName() + "&a NPC."));
    }

}