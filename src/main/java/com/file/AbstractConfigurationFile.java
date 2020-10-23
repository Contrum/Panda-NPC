package com.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class AbstractConfigurationFile{
    public static final String FILE_EXTENSION = ".yml";
    private final JavaPlugin plugin;
    private final String name;
    
    public AbstractConfigurationFile(final JavaPlugin plugin, final String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract String getString(final String string);

    public abstract String getStringOrDefault(final String string, final String defaultString);

    public abstract int getInteger(final String p0);

    public abstract double getDouble(final String p0);

    public abstract Object get(final String p0);

    public abstract List<String> getStringList(final String p0);

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getName() {
        return this.name;
    }
}
