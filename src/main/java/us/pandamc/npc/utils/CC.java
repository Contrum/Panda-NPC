package us.pandamc.npc.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CC {

    public static String MENU_BAR = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------";

    public String translate(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public List<String> translate(List<String> list){
        return list.stream().map(CC::translate).collect(Collectors.toList());
    }

}