package us.pandamc.npc.utils.nms;

import org.bukkit.entity.Player;
import us.pandamc.npc.npc.NPC;

public interface NPCPackets {

    void spawn(NPC npc, Player player);

    void destroy(NPC npc,Player player);

}