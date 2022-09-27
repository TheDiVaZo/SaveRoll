package saveroll.saveroll;

import org.bukkit.plugin.java.JavaPlugin;
import saveroll.saveroll.logging.Logger;
import saveroll.saveroll.logging.handlers.JULHandler;

public final class SaveRoll extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger.init(new JULHandler(getLogger()));
        Logger.info("Starting...");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
