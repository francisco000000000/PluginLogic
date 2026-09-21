# PluginLogic

A plugin runtime for Mindustry.

PluginLogic allows external plugins to be loaded and executed at runtime using the `.PluginLogic` package format.

## Features

* External `.PluginLogic` plugin packages
* `plugin.json` manifest
* Lua plugin support through LuaJ
* Plugin version requirements
* Basic plugin permissions
* Plugins are loaded directly from ZIP packages

## Plugin Structure

A `.PluginLogic` file is a ZIP archive.

```text
MyPlugin.PluginLogic
├── plugin.json
└── src/
    └── main.lua
```

The `plugin.json` file must be located at the root of the package.

Example:

```hjson
{
    id: "example"
    displayName: "My Plugin"
    name: "my-plugin"
    author: "Author"
    version: 1.0
    minPluginLogic: 0.1
    type: "lua"
    main: "src/main.lua"
}
```

## Lua Plugins

Lua is currently the first supported plugin runtime.

Example:

```lua
function main()
    print("Hello from PluginLogic!")
end
```

The `main()` function is used as the plugin entry point.

## Installation

Copy the `PluginLogic` mod into the Mindustry `mods` directory.

Then place `.PluginLogic` files in:

```text
Mindustry/plugins/
```

On Linux, this is normally:

```text
~/.local/share/Mindustry/plugins/
```

## Status

PluginLogic is currently in early development.

The API, plugin format, permissions system, and runtime support may change.

## License

See the repository license for details.
