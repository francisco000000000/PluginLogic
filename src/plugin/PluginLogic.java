package plugin;

import mindustry.mod.Mod;

public class PluginLogic extends Mod {

    public static PluginManager manager;

    @Override
    public void init() {
        manager = new PluginManager();
        manager.load();
    }
}
