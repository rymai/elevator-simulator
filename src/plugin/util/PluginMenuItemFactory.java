package plugin.util;

import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import plugin.*;

public class PluginMenuItemFactory {
  /**
   * Le menu gere par cette instance.
   */
  private JMenu menu;

  /**
   * Le chargeur de classes charge les plugins.
   */
  private PluginLoader loader;

  /**
   * L'actionListener qui va ecouter les entres du menu des plugins.
   */
  private ActionListener listener;

  private static Logger logger =
    Logger.getLogger("fr.unice.plugin.PluginMenu");

  /**
   * Construit une instance qui concerne un certain menu. Ce menu aura
   * des choix qui permettront de selectionner un plugin ou un autre.
   * @param menu le menu gere par cette instance.
   * @param loader le chargeur de classes des plugins.
   * @param listener l'actionListener qui va couter les entres du menu.
   */
  public PluginMenuItemFactory(JMenu menu, PluginLoader loader, ActionListener listener) {
	logger.setLevel(Level.OFF);
	  
	this.menu = menu;
    this.loader = loader;
    this.listener = listener;
  }

  /**
   * Construit le menu des plugins.
   * @param type type des plugins utiliss pour construire le menu.
   * Si null, tous les types de plugin seront utilises pour construire le menu.
   * @return le menu construit avec les plugins.
   */
  public void buildMenu(Class type) {
    if (loader == null) {
      return;
    }
    logger.info("Construction du menu des PLUGINS");

    // Enleve les entrees precedentes s'il y en avait
    menu.removeAll();

    // Recupere les instances deja chargees
    Plugin [] instancesPlugins ; 
    instancesPlugins = loader.getPluginInstances();
    logger.info("Nombre de plugins trouves :" + instancesPlugins.length);

    // On ajoute une entree par instance de plugin
    for (int i = 0; i < instancesPlugins.length; i++) {
      Plugin plugin = instancesPlugins[i];
      PluginMenuItem item = new PluginMenuItem(plugin.getName(), plugin);
      item.addActionListener(listener);
      menu.add(item);
    }
  }
  
}