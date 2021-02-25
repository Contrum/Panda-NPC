package us.pandamc.npc.utils;

import us.pandamc.npc.PandaNPC;

import java.io.InputStreamReader;
import java.net.URL;

public class Utils {

    public static String[] getFromName(String str) {
        if(PandaNPC.get().getVersion().equalsIgnoreCase("v1_7_R4")){
            try {
                URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + str);
                InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
                String uuid = new net.minecraft.util.com.google.gson.JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                net.minecraft.util.com.google.gson.JsonObject textureProperty = new net.minecraft.util.com.google.gson.JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();

                return new String[]{texture, signature};
            } catch (Exception ignored) {
                System.err.println("Could not get skin data from session servers!");
                return null;
            }
        }else{
            try {
                URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + str);
                InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
                String uuid = new com.google.gson.JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                com.google.gson.JsonObject textureProperty = new com.google.gson.JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();

                return new String[]{texture, signature};
            } catch (Exception ignored) {
                System.err.println("Could not get skin data from session servers!");
                return null;
            }
        }
    }


}
