package us.pandamc.npc.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.npc.events.NPCInteractEvent;
import us.pandamc.npc.npc.NPC;
import us.pandamc.npc.npc.select.NPCSelect;
import us.pandamc.npc.utils.TaskUtil;

public class PlayerListeners implements Listener {

    private final PandaNPC plugin = PandaNPC.get();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> plugin.getPackets().spawn(npc, player));
    }

    @EventHandler
    public void onInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();

        ItemStack item = player.getItemInHand();
        if(item.equals(NPCSelect.selectItem())) return;

        if(event.getAction() == NPCInteractEvent.Action.RIGHT_CLICK){
            if(npc.getCommands() != null){
                npc.getCommands().forEach(player::performCommand);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        NPCSelect.unSelect(event.getPlayer());
        NPC.getNpcs().values().forEach(npc -> npc.getEntitys().remove(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            NPC.getNpcs().values().forEach(npc -> {
                Location location = npc.getLocation();
                if(location == null) return;
                if(location.getWorld() != from.getWorld()) return;
                if(location.getWorld() != to.getWorld()) return;
                if (from.distance(location) > 60 && to.distance(location) < 60) {
                    plugin.getPackets().destroy(npc, player);
                    plugin.getPackets().spawn(npc, player);
                }
            });
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> {
            Location location = npc.getLocation();
            if(location == null) return;
            if(location.getWorld() != player.getLocation().getWorld()) return;
            if(location.distance(player.getLocation()) > 30){
                plugin.getPackets().destroy(npc, player);
                plugin.getPackets().spawn(npc, player);
            }
        });
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> {
            plugin.getPackets().destroy(npc, player);
            TaskUtil.runLater(() -> plugin.getPackets().spawn(npc, player), 20L);
        });
    }

}