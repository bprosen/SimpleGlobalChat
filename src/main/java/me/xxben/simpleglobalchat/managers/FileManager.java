package me.xxben.simpleglobalchat.managers;

import me.xxben.simpleglobalchat.SimpleGlobalChat;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.HashMap;

public class FileManager {

    private HashMap<String, Configuration> configMap = new HashMap<>();

    public FileManager() {
        init("config");
        init("lang");
    }

    public void init(String filename) {

        try {

            Plugin plugin = SimpleGlobalChat.getInstance();
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            filename += ".yml";

            File file = new File(plugin.getDataFolder().getPath(), filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                copy(plugin.getResourceAsStream(filename), file);
            }

            ConfigurationProvider cfgProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            Configuration config = cfgProvider.load(file);
            cfgProvider.save(config, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(Configuration config, String fileName) {

        try {
            File file = new File(SimpleGlobalChat.getInstance().getDataFolder(), fileName + ".yml");
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

            // put into map
            configMap.put(fileName, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copy(InputStream in, File file) {

        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configuration get(String fileName) {
        return configMap.get(fileName);
    }
}
