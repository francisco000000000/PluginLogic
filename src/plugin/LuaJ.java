package plugin;

import arc.util.Log;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* Aqui é o Runtime dos mods feito em Lua. */
public class LuaJ {

    public static void run(
        InputStream source,
        String pluginName,
        String main
    ) {
        try {
            String code = new String(
                source.readAllBytes(),
                StandardCharsets.UTF_8
            );

            Globals globals = JsePlatform.standardGlobals();

            globals.set("print", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    StringBuilder text = new StringBuilder();

                    for (int i = 1; i <= args.narg(); i++) {
                        if (i > 1) text.append('\t');
                        text.append(args.arg(i).tojstring());
                    }

                    Log.info(
                        "[PluginLogic] [@] @",
                        pluginName,
                        text
                    );

                    return NONE;
                }
            });

            LuaValue chunk = globals.load(code, main);
            chunk.call();

            LuaValue entry = globals.get("main");

            if (entry.isnil() || !entry.isfunction()) {
                Log.err(
                    "[PluginLogic] Plugin @ has no main() function",
                    pluginName
                );
                return;
            }

            entry.call();

        } catch (Exception e) {
            Log.err(
                "[PluginLogic] Error executing @ in @",
                main,
                pluginName
            );
            Log.err(e);
        }
    }
}
