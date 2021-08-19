package dev.panda.npc.npc;

import org.bukkit.Location;

public class NPCManager {

    public void create(String npcName, Location location) {
        NPC npc = new NPC(npcName);

        npc.setDisplayName(npcName);
        npc.setLocation(location);
        npc.setYaw(location.getYaw());
        npc.setPitch(location.getPitch());
        npc.setHeadYaw(location.getYaw());
        npc.save();
        npc.spawnAll();

        NPC.getNpcs().put(npcName, npc);
    }

    public void delete(NPC npc) {
        npc.delete();
        npc.destroyAll();
    }
}
