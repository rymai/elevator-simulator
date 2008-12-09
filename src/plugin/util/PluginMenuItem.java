package plugin.util;

import javax.swing.JMenuItem;
import javax.swing.Icon;
import plugin.*;

public class PluginMenuItem extends JMenuItem {

	private static final long serialVersionUID = -7623957110537936584L;
	
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