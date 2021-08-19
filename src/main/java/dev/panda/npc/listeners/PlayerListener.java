package dev.panda.npc.listeners;

import dev.panda.npc.PandaNPC;
import dev.panda.npc.npc.NPC;
import dev.panda.npc.npc.event.NPCInteractEvent;
import dev.panda.npc.utilities.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final PandaNPC plugin;

    public PlayerListener(PandaNPC plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> plugin.getNPCPacket().spawn(npc, player));
    }

    @EventHandler
    public void onInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();

        if (event.getAction() == NPCInteractEvent.Action.RIGHT_CLICK) {
            if (!npc.getCommands().isEmpty()){
                npc.getCommands().forEach((key, value) -> {
                    if (value) {
                        TaskUtil.run(() -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
                                key.replace("%player%", player.getName())));
                    }
                    else {
                        player.performCommand(key.replace("%player%", player.getName()));
                    }
                });
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        NPC.getNpcs().values().forEach(npc -> npc.getEntities().remove(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            NPC.getNpcs().values().forEach(npc -> {
                Location location = npc.getLocation();
                if (location == null) return;
                if (location.getWorld() != from.getWorld()) return;
                if (location.getWorld() != to.getWorld()) return;
                if (from.distance(location) > 60 && to.distance(location) < 60) {
                    plugin.getNPCPacket().destroy(npc, player);
                    plugin.getNPCPacket().spawn(npc, player);
                }
            });
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> {
            Location location = npc.getLocation();
            if (location == null) return;
            if (location.getWorld() != player.getLocation().getWorld()) return;
            if (location.distance(player.getLocation()) > 30) {
                plugin.getNPCPacket().destroy(npc, player);
                plugin.getNPCPacket().spawn(npc, player);
            }
        });
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        NPC.getNpcs().values().forEach(npc -> {
            plugin.getNPCPacket().destroy(npc, player);
            TaskUtil.runLater(() -> plugin.getNPCPacket().spawn(npc, player), 20L);
        });
    }
}