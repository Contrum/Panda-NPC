package dev.panda.npc.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import dev.panda.npc.npc.event.NPCInteractEvent;
import dev.panda.npc.utilities.TaskUtil;
import dev.panda.npc.utilities.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev.panda.npc.PandaNPC;
import dev.panda.npc.npc.NPC;

import java.lang.reflect.Field;

public class ProtocolListener implements PacketListener {

    public ProtocolListener(ProtocolManager protocolManager) {
        protocolManager.addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event) {}

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        if (packet.getType() == PacketType.Play.Client.USE_ENTITY) {
            int id = packet.getIntegers().read(0);

            if (id < 0) return;

            final NPC npc = NPC.getByID(player, id);

            if (npc == null) return;

            if (player.isDead()) return;
            if (player.getLocation().distance(npc.getLocation()) > 8) {
                return;
            }

            NPCInteractEvent.Action action;
            try {
                Field field = packet.getEntityUseActions().getField(0);
                field.setAccessible(true);
                Object obj = field.get(packet.getEntityUseActions().getTarget());
                String actionName = (obj == null) ? "" : obj.toString();

                switch (actionName) {
                    case "INTERACT":
                        action = NPCInteractEvent.Action.RIGHT_CLICK;
                        break;
                    case "ATTACK":
                        action = NPCInteractEvent.Action.LEFT_CLICK;
                        break;
                    default:
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            event.setCancelled(true);

            if (Cooldown.getCooldownMap().get("NPCFix").isOnCooldown(player)) return;
            Cooldown.getCooldownMap().get("NPCFix").setCooldown(player);
            TaskUtil.run(() -> Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, npc, action)));
        }
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return ListeningWhitelist.newBuilder()
            .priority(ListenerPriority.NORMAL)
            .types(PacketType.Play.Client.USE_ENTITY)
            .gamePhase(GamePhase.PLAYING)
            .options(new ListenerOptions[0])
            .build();
    }

    @Override
    public Plugin getPlugin() {
        return PandaNPC.get();
    }
}
