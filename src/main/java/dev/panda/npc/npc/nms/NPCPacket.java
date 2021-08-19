package dev.panda.npc.npc.nms;

import dev.panda.npc.npc.NPC;
import org.bukkit.entity.Player;

public interface NPCPacket {

    void spawn(NPC npc, Player player);

    void destroy(NPC npc, Player player);

}