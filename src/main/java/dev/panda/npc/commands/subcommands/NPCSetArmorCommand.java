package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class NPCSetArmorCommand extends BaseCommand {

    @Command(name = "npc.setarmor", permission = "pandanpc.npc.setarmor", aliases = {"pandanpc.setarmor", "pnpc.setarmor"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("setarmor");

        if (args.length < 1) {
            player.sendMessage(ChatUtil.translate("&cUsage: /" + label + " setarmor <npc>"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            player.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        npc.setHelmet(player.getInventory().getHelmet());
        npc.setChest(player.getInventory().getChestplate());
        npc.setLegs(player.getInventory().getLeggings());
        npc.setBoots(player.getInventory().getBoots());
        npc.setHand(player.getInventory().getItemInHand());
        npc.reset();

        player.sendMessage(ChatUtil.translate("&bNPC armor has been set."));
    }
}
