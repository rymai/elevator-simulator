package plugin.util;

import javax.swing.JMenuItem;
import javax.swing.Icon;
import plugin.*;

/**
 * MenuItem qui garde une reference à une instance de plugin.
 * Doit-elle etre public ??
 * @author Richard Grin
 * @version 1.0
 */
public class PluginMenuItem extends JMenuItem {
  private Plugin plugin;

  public PluginMenuItem() {
    super();
  }

  public PluginMenuItem(String text) {
    super(text);
  }

  public PluginMenuItem(String text, Plugin plugin) {
    super(text);
    this.plugin = plugin;
  }

  public PluginMenuItem(String text, Icon icon) {
    super(text, icon);
  }

  public void setPlugin(Plugin plugin) {
    this.plugin = plugin;
  }

  public Plugin getPlugin() {
    return plugin;
  }
}