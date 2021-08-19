package dev.panda.npc.commands.subcommands;

import dev.panda.npc.npc.NPC;
import dev.panda.npc.utilities.ChatUtil;
import dev.panda.npc.utilities.NPCUtil;
import dev.panda.npc.utilities.command.BaseCommand;
import dev.panda.npc.utilities.command.Command;
import dev.panda.npc.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class NPCSetSkinCommand extends BaseCommand {

    @Command(name = "npc.setskin", permission = "pandanpc.npc.setskin", aliases = {"pandanpc.setskin", "pnpc.setskin"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getReplaceLabel("setskin");

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " setskin <npc> (skin|player)"));
            return;
        }

        String npcName = args[0];
        NPC npc = NPC.getByName(npcName);

        if (npc == null) {
            sender.sendMessage(ChatUtil.translate("&cNPC '&7" + npcName + "&c' not found."));
            return;
        }

        String skinName = args[1];

        if (skinName.equalsIgnoreCase("player")) {
            npc.setTexture(null);
            npc.setSignature(null);
            npc.reset();

            sender.sendMessage(ChatUtil.translate("&bNPC skin has been set &7(&fSkin Per Player&7)"));
            return;
        }

        String[] skin = NPCUtil.getFromName(skinName);

        if (skin == null) {
            sender.sendMessage(ChatUtil.translate("&cSkin can only be configured with premium players."));
            return;
        }

        npc.setTexture(skin[0]);
        npc.setSignature(skin[1]);
        npc.reset();

        sender.sendMessage(ChatUtil.translate("&bNPC skin has been set &7(&r" + skinName + "&7)"));
    }
}
