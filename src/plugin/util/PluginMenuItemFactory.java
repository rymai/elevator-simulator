package plugin.util;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.logging.Logger;
import java.awt.Component;
import plugin.*;

/**
 * Classe qui met dans un menu des items lies aux plugins deja charges.
 * Une instance de cette classe s'occupe toujours du meme menu qui peut etre
 * reconstruit plusieurs fois durant une session de travail. Pour etre clair,
 * le JMenu est toujours le meme mais ses entres sont reconstruites  chaque
 * appel de la methode buildMenu. Les entrees tiennent compte des plugins deja
 * charges au moment de l'appel de builMenu.
 * S'il y a des nouveaux plugins, de nouvelles entrees sont ajoutees au menu.
 * Si des plugins ont disparu, les entrees correspondantes ne seront plus
 * dans le menu.
 * Si les plugins ont change, un client peut reconstruire le menu. En ce cas,
 * les anciennes entrees liees aux plugins sont enlevees et les nouvelles
 * sont ajoutees au meme endroit dans le menu.
 * <P>
 * Exemple d'utilisation :
 * <pre>
 * pluginManager = PluginManager.getPluginManager(
 *     "file:repplugin/plugins.jar");
 * pluginManager.loadPlugins();
 * JMenuBar mb = new JMenuBar();
 * menuPlugins = new JMenu("Plugins");
 * menuPlugins.add(...);
 * menuPlugins.addSeparator();
 * PluginMenuFactory pluginMenuFactory = new PluginMenuFactory(menuPlugins);
 * // Construit un menu avec tous les plugins de type Dessinateur.
 * menuPlugins = pluginMenuFactory.buildMenu(Dessinateur.class);
 * menuPlugins.add(...);
 * mb.add(menuPlugins);
 * // Met un dessinateur par defaut (le 1er dans la liste des entres du menu)
 * dessinateur = (Dessinateur)pluginMenuFactory.getPlugin(0);
 * pluginMenuFactory.addObserver(this);
 * . . .
 * public void update(Observable o, Object arg) {
 *   fenetreDessin.setDessinateur((Dessinateur)arg);
 * }
 * </pre>
 * @author Richard Grin
 * @version 1.0
 */
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
  public PluginMenuItemFactory(JMenu menu, PluginLoader loader,
                               ActionListener listener) {
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

    // Enlve les entrees precedentes s'il y en avait
    menu.removeAll();

    // Rcupere les instances deja chargees

    Plugin [] instancesPlugins ; 
    //  ***** DU CODE A ECRIRE ICI !!!!!
    instancesPlugins = loader.getPluginInstances();
    logger.info("Nombre de plugins trouves :" + instancesPlugins.length);

    // On ajoute une entree par instance de plugin
    for (int i = 0; i < instancesPlugins.length; i++) {
      Plugin plugin = instancesPlugins[i];
      
      // ***** DU CODE A ECRIRE ICI !!!!!
      PluginMenuItem item = new PluginMenuItem(plugin.getName(), plugin);
      item.addActionListener(listener);
      menu.add(item);
    }
   
  }
}
