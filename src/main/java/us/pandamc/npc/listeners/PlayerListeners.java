package us.pandamc.npc.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import us.pandamc.npc.PandaNPC;
import us.pandamc.npc.events.NPCInteractEvent;
import us.pandamc.npc.npc.NPC;
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

        player.performCommand(npc.getCommand());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
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
                if (location != null && from.distance(location) > 60 && to.distance(location) < 60) {
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
            if(location != null && location.distance(player.getLocation()) > 30){
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