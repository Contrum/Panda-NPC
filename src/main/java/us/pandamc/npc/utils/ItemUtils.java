package us.pandamc.npc.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemUtils {

    public static String serialize(ItemStack itemStack){
        StringBuilder serialized = new StringBuilder();

        serialized.append("material:").append(itemStack.getType()).append(";")
            .append("durability:").append(itemStack.getDurability()).append(";")
            .append("amount:").append(itemStack.getAmount()).append(";");

        if(itemStack.hasItemMeta()){
            ItemMeta meta = itemStack.getItemMeta();
            if(meta.hasDisplayName()){
                serialized.append("name:").append(meta.getDisplayName()).append(";");
            }
            if (meta.hasLore()){
                serialized.append("lore:").append(String.join(",", meta.getLore())).append(";");
            }
        }

        return serialized.toString();
    }

    public static ItemStack deSerialized(String string){
        ItemBuilder itemBuilder = null;

        String[] serializedSplit = string.split(";");
        for (String str : serializedSplit){
            if(str.contains("material")){
                String material = str.replace("material:", "");
                itemBuilder = new ItemBuilder(Material.valueOf(material));
            }else if(str.contains("durability")){
                String durabity = str.replace("durability:", "");
                itemBuilder.durability(Short.parseShort(durabity));
            }else if(str.contains("name")){
                String name = str.replace("name:", "");
                itemBuilder.name(name);
            }else if(str.contains("lore")){
                String lore = str.replace("lore:", "");
                itemBuilder.lore(Arrays.asList(lore.split(",")));
            }else if(str.contains("amount")){
                itemBuilder.amount(Integer.parseInt(str.replace("amount:", "")));
            }
        }

        return itemBuilder.build();
    }


}