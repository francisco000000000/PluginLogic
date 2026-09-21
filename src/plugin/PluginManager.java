package plugin;

import arc.files.Fi;
import arc.util.Log;
import arc.util.serialization.Jval;
import mindustry.Vars;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager {

    public static final String VERSION = "0.1";

    private final Fi pluginsDir;

    public PluginManager() {
        pluginsDir = Vars.dataDirectory.child("plugins");
        pluginsDir.mkdirs();
    }

    public void load() {
        for (Fi file : pluginsDir.list()) {
            if (!file.extEquals("PluginLogic")) continue;

            loadPlugin(file);
        }
    }

    private void loadPlugin(Fi file) {
        Log.info("[PluginLogic] Loading plugin: @", file.name()); //Informar o que plugin ele tá carregando

        try (ZipFile zip = new ZipFile(file.file())) { //ZipFile imbutido do java

            ZipEntry manifestEntry = zip.getEntry("plugin.json"); //Ele tenta achar o plugin.json, se não tiver, ele não executa

            if (manifestEntry == null) {
                Log.err("[PluginLogic] Plugin @ has no plugin.json", file.name());
                return;
            }

            String manifest;

            try (InputStream stream = zip.getInputStream(manifestEntry)) {
                manifest = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }

            Jval json = Jval.read(manifest);

            String name = json.getString("name", file.name());
            String displayName = json.getString("displayName", name);
            String type = json.getString("type", "");
            String main = json.getString("main", "");
            double minPluginLogic = json.get("minPluginLogic").asDouble();

            if (minPluginLogic > Double.parseDouble(VERSION)) {
                Log.err(
                    "[PluginLogic] Plugin @ requires PluginLogic @, current version is @",
                    displayName,
                    minPluginLogic,
                    VERSION
                );
                return;
            }

            if (main.isEmpty()) {
                Log.err("[PluginLogic] Plugin @ has no main file", displayName);
                return;
            }

            ZipEntry mainEntry = zip.getEntry(main);

            if (mainEntry == null || mainEntry.isDirectory()) {
                Log.err(
                    "[PluginLogic] Main file @ not found in plugin @",
                    main,
                    displayName
                );
                return;
            }

            Log.info(
                "[PluginLogic] Loading @ (@) using @",
                displayName,
                name,
                type
            );

            switch (type.toLowerCase()) {
                case "lua" -> {
                    try (InputStream stream = zip.getInputStream(mainEntry)) {
                        LuaJ.run(stream, displayName, main);
                    }
                }

                default -> Log.err(
                    "[PluginLogic] Unknown plugin type @ in @",
                    type,
                    displayName
                );
            }

        } catch (Exception e) {
            Log.err("[PluginLogic] Failed to load @", file.name());
            Log.err(e);
        }
    }
}
