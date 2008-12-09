package plugin;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.*;
import java.net.MalformedURLException;

/**
 * Charge des classes de plugins dont les fichiers .class sont places
 * dans des URL donnees, et cree une instance de chaque plugin charge.
 * Cette classe delegue a des ClassLoader le chargement des classes avant de
 * creer les instances.
 * <P>
 * On peut parcourir les URL pour charger "a chaud" de nouveaux plugins
 * qui y auraient ete nouvellement installes (methode loadPlugins).
 * En ce cas, les anciens plugins ne sont pas recharges.
 * On peut mme recuprer de nouvelles versions des plugins avec
 * les methodes reloadPlugins.
 * <P>
 * Normalement cette classe est utilise par un PluginManager mais pas
 * directement par les clients qui veulent charger des plugins.
 * @author Richard Grin (modif par Philippe Renevier)
 * @version 1.0
 */

public class PluginLoader {
	/**
	 * Le chargeur de classes qui va charger les plugins.
	 */
	private ClassLoader loader;
	/**
	 * Le repertoire ou les plugins seront recherches.
	 */
	private String pluginDirectory;
	/**
	 * Liste des instances des plugins qui ont ete charges par loadPlugins.
	 */
	private List<Plugin> loadedPluginInstances = new ArrayList<Plugin>();

	private static Logger logger =
		Logger.getLogger("fr.unice.plugin.PluginLoader");

	/**
	 * Cree une instance qui va chercher les plugins dans le repertoire dont
	 * on passe le nom en paramtre.
	 * @param urls tableau completement rempli par les URLs (pas d'elements null).
	 */
	public PluginLoader(String directory) throws MalformedURLException {
		// On verifie que l'URL correspond bien  un repertoire.
		File dirFile = new File(directory);
		if (dirFile == null || ! dirFile.isDirectory()) {
			logger.warning(directory + " n'est pas un rpertoire");
			throw new IllegalArgumentException(directory + " n'est pas un rpertoire");
		}

		// Si c'est un repertoire mais que l'URL ne se termine pas par un "/",
		// on ajoute un "/"  la fin (car URL ClassLoader oblige a donner
		// un URL qui se termine par un "/" pour les repertoires).
		if (!directory.endsWith("/")) {
			directory += "/";
		}
		this.pluginDirectory = directory;
		// Cree le chargeur de classes.
		createNewClassLoader();
	}

	/**
	 * Charge les instances des plugins d'un certain type places dans le
	 * repertoire indique a la creation du PluginLoader. Ces instances
	 * sont chargees en plus de celles qui ont deja ete chargees.
	 * Si on ne veut que les instances des plugins qui vont etre chargees
	 * dans cette methode, et avec les nouvelles versions s'il y en a, il faut
	 * utiliser {@link #reloadPlugins(Class)}
	 * On peut recuprer ces plugins par la methode {@link #getPluginInstances}.
	 * Si un plugin a deja ete charg, il n'est pas
	 * recharge, mme si une nouvelle version est rencontre.
	 * @param type type des plugins recherches. Si null, charge les plugins
	 * de tous les types.
	 */
	public void loadPlugins(Class type) {
		// En prevision d'un chargement ailleurs que d'un repertoire, on fait
		// cette indirection. On pourrait ainsi charger d'un jar.
		loadFromDirectory(type);
	}

	/**
	 * Charge les instances de tous les plugins.
	 * On peut recuprer ces plugins par la methode {@link #getPluginInstances}.
	 * Si un plugin a deja ete charg, il n'est pas
	 * recharge, mme si une nouvelle version est rencontree.
	 */
	public void loadPlugins() {
		loadPlugins(null);
	}

	/**
	 * Recharge tous les plugins.
	 * Charge les nouvelles versions des plugins s'il les rencontre.
	 */
	public void reloadPlugins() {
		reloadPlugins(null);
	}

	/**
	 * Recharge tous les plugins d'un type donne.
	 * Charge  les nouvelles versions des plugins s'il les rencontre.
	 * @param type type des plugins a charger.
	 */
	public void reloadPlugins(Class type) {
		// Cree un nouveau chargeur pour charger les nouvelles versions.
		try {
			createNewClassLoader();
		}
		catch(MalformedURLException ex) {
			// Ne devrait jamais arriver car si l'URL etait mal forme,
			// on n'aurait pu creer "this".
			ex.printStackTrace();
		}
		// Et efface tous les plugins du type deja chargs.
		erasePluginInstances(type);
		// Recharge les plugins du type
		loadPlugins(type);
	}

	/**
	 * Renvoie les instances de plugins qui ont ete recupres cette fois-ci et
	 * les fois d'avant (si on n'a pas efface les plugins charges avant lors de la
	 * derniere recherche de plugins {@link #loadPlugins(boolean)}.
	 * @return les instances recuprees. Le tableau est plein.
	 */
	public Plugin[] getPluginInstances() {
		return getPluginInstances(null);
	}

	public Plugin[] getPluginInstances(Class type) {
		List<Plugin> loadedPluginInstancesOfThatType = new ArrayList<Plugin>();
		for (Plugin plugin : loadedPluginInstances) {
			if (type == null || plugin.getType().equals(type)) {
				loadedPluginInstancesOfThatType.add(plugin);
			}
		}
		return loadedPluginInstancesOfThatType.toArray(new Plugin[0]);
	}

	/**
	 * Efface le chargeur de classes.
	 */
	private void eraseClassLoader() {
		loader = null;
	}

	/**
	 * Cree un nouveau chargeur. Permettra ensuite de charger de nouvelles
	 * versions des plugins.
	 */
	private void createNewClassLoader() throws MalformedURLException {
		logger.info("******Creation d'un nouveau chargeur de classes");
		loader = URLClassLoader.newInstance(new URL[] { getURL(pluginDirectory) });
	}

	/**
	 * Efface tous le plugins d'un certain type deja charges.
	 * @param type type des plugins  effacer. Efface tous les plugins si null.
	 */
	private void erasePluginInstances(Class type) {
		if (type == null) {
			loadedPluginInstances.clear();
		}
		else {
			for (Plugin plugin : loadedPluginInstances) {
				if (plugin.getType().equals(type)) {
					loadedPluginInstances.remove(plugin);
				}
			}
		}
	}

	/**
	 * Charge les plugins d'un certain type places dans un repertoire
	 * qui n'est pas dans un jar.
	 * @param type type des plugins. Charge tous les plugins si <code>null</code>.
	 */
	private void loadFromDirectory(Class type) {
		// Pour trouver le nom complet des plugins trouvs : c'est la partie
		// du chemin qui est en plus du rpertoire de base donn au loader.
		// Par exemple, si le chemin de base est rep1/rep2, le plugin
		// de nom machin.truc.P1 sera dans rep1/rep2/machin/truc/P1.class
		logger.info("=+=+=+=+=+ Entre dans loadPluginsFromDirectory=+=+=+=+");
		loadFromSubdirectory(new File(pluginDirectory), type, pluginDirectory);
		logger.info("=+=+=+=+=+ Sortie de loadPluginsFromDirectory=+=+=+=+");
	}

	/**
	 * Charge les plugins places directement sous un sous-repertoire
	 * d'un rpertoire de base. Les 2 repertoires ne sont pas dans un jar.
	 * appel recursif de cette methode par elle-meme temps qu'elle trouve
	 * des repertoires a explorer
	 * @param dir sous-repertoire. Le nom du paquetage du plugin devra
	 * correspondre a la position relative du sous-repertoire par rapport
	 * au repertoire de base.
	 * @param type type de plugins a charger.
	 * @param baseName nom du repertoire ou sont les plugins
	 * Charge tous les plugins si <code>null</code>.
	 */
	private void loadFromSubdirectory(File dir, Class type,
			String baseName) {
		logger.info("Chargement dans le sous-rpertoire " + dir
				+ " avec nom de base " + baseName);
		int baseNameLength = baseName.length();
		// On parcourt toute l'arborescence  la recherche de classes
		// qui pourraient etre des plugins.
		// Quand on l'a trouve, on en deduit son paquetage avec sa position
		// relativement a baseName.
		File[] files = dir.listFiles();
		logger.info("Le listing : " + files);
		// On trie pour que les plugins apparaissent dans le menu
		// par ordre alphabtique
		//    Arrays.sort(list);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				loadFromSubdirectory(file, type, baseName);
				continue;
			}
			// Ca n'est pas un repertoire, on test si c'est un plugin
			logger.info("Examen du fichier " + file.getPath() + ";" + file.getName());
			String path = file.getPath();
			String qualifiedClassName = getQualifiedName(baseNameLength, path);
			// On obtient une instance de cette classe
			if (qualifiedClassName != null) {
				Plugin plugin = getInstance(qualifiedClassName, type);
				if(plugin != null) {
					logger.info("Classe " + qualifiedClassName + " est bien un plugin !");
					// S'il n'y a pas deja un plugin de la meme classe, on ajoute
					// l'instance de plugin que l'on vient de creer.
					boolean alreadyLoaded = false;
					for (Plugin loadedPlugin : loadedPluginInstances) {
						if (loadedPlugin.getClass().equals(plugin.getClass())) {
							alreadyLoaded = true;
							break;
						}
					}
					if (! alreadyLoaded) {
						loadedPluginInstances.add(plugin);
					}
					logger.info("Les plugins : " + loadedPluginInstances);
				}
			}
		} // for
	}

	/**
	 * Dans le cas ou un chemin correspond a un fichier .class,
	 * calcule le nom complet d'une classe a partir du nom d'un repertoire
	 * de base et du chemin de la classe, les 2 chemins etant ancres au meme
	 * repertoire racine.
	 * Le repertoire de base se termine par "/" (voir classe URLClassLoader).
	 * Par exemple, a/b/c/ (c'est--dire 6 pour baseNameLength)
	 * et a/b/c/d/e/F.class donneront d.e.F
	 * @param baseNameLength nombre de caracteres du nom du repertoire de base.
	 * @param classPath chemin de la classe.
	 * @return le nom complet de la classe, ou null si le nom ne correspond
	 * pas a une classe externe.
	 */
	private String getQualifiedName(int baseNameLength, String classPath) {
		logger.info("Calcul du nom qualifi de " + classPath + " en enlevant "
				+ baseNameLength + " caractres au dbut");
		// Un plugin ne peut etre une classe interne
		if ((! classPath.endsWith(".class")) || (classPath.indexOf('$') != -1)) {
			return null;
		}
		// C'est bien une classe externe
		classPath = classPath.substring(baseNameLength)
		.replace(File.separatorChar, '.');
		// On enelve le .class final pour avoir le nom de la classe
		logger.info("Nom complet de la classe : " + classPath);
		return classPath.substring(0, classPath.lastIndexOf('.'));
	}

	/**
	 * Transforme le nom du repertoire en URL si le client n'a pas donne
	 * un bon format pour l'URL (pour pouvoir creer un URLClassLoader).
	 * @param dir nom du repertoire.
	 * @return l'URL avec le bon format.
	 * @throws MalformedURLException lancee si on ne peut deviner de quel URL il
	 * s'agit.
	 */
	private static URL getURL(String dir) throws MalformedURLException {
		logger.info("URL non transforme : " + dir);

		/* On commence par transformer les noms absolus de Windows en URL ;
		 * par exemple, transformer C:\rep\machin en file:/C:/rep/machin
		 */
		if (dir.indexOf("\\") != -1) {
			// on peut souponner un nom Windows !
			// 4 \ pour obtenir \\ pour l'expression regulire !
			dir = dir.replaceAll("\\\\", "/");
		} // Nom Windows

		/* C'est un repertoire ; plusieurs cas :
		 *   1. S'il y a le protocole "file:", on ne fait rien ; par exemple,
		 *      l'utilisateur indique que les plugins sont dans un repertoire
		 *      avec un nom absolu et, dans ce cas, il doit mettre le protocole
		 *      "file:" au debut ;
		 *   2. S'il n'y a pas de protocole "file:", on le rajoute.
		 */
		if (! dir.startsWith("file:")) {
			/* On considere que c'est le nom d'une ressource ; si le nom est
			 * absolu, c'est un nom par rapport au classpath.
			 */
			dir = "file:" + dir;
		}

		logger.info("URL transforme : " + dir);
		return new URL(dir);
	}

	/**
	 * Retourne une instance de plugin d'un type donne.
	 * @param nomClasse Nom de la classe du plugin
	 * @param type type de plugin
	 * @param cl chargeur de classes qui va faire le chargement de la classe
	 * de plugin
	 * @return une instance de plugin. Retourne null si probleme.
	 * Par exemple, si le plugin n'a pas le bon type.
	 */
	private Plugin getInstance(String nomClasse, Class type) {
		logger.info("Entre dans getInstance");
		try {
			// C'est ici que se passe le chargement de la classe par le
			// chargeur de classes.
			logger.info("Demande de chargement de la classe " + nomClasse + " par " + this);

			Class newClass = null;
			Plugin result = null;
			// ***** DU CODE A ECRIRE ICI !!!!!
			newClass = loader.loadClass(nomClasse);

			try {
				// On cree une instance de la classe
				logger.info("Creation d'une instance de " + newClass);

				// ***** DU CODE A ECRIRE ICI !!!!!
				if(!(newClass.isInterface() || Modifier.isAbstract(newClass.getModifiers()))) {
					result = (Plugin) newClass.newInstance();
				}
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				// Le fichier  n'est pas un plugin 'Plugin'
				logger.warning("La classe " + nomClasse + " n'est pas un Plugin");
				return null;
			}
			catch (InstantiationException e ) {
				e.printStackTrace();
				logger.warning("La classe " + nomClasse + " ne peut pas etre instanci");
				return null;
			}
			catch (IllegalAccessException e ) {
				e.printStackTrace();
				logger.warning("La classe " + nomClasse + " est interdite d'accs");
				return null;
			}
			catch (NoClassDefFoundError e ) {
				// Survient si la classe n'a pas le bon nom
				e.printStackTrace();
				logger.warning("La classe " + nomClasse + " ne peut tre trouve");
				return null;
			} // Fin des catchs pour le try pour creation instance

			// Teste si plug est du bon type
			// ***** DU CODE A ECRIRE ICI !!!!!
			if(type != null && !type.isInstance(result)) {
				return null;
			}
			return result;
		}
		// Les catchs pour le 1er try pour chargement de la classe
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.warning("Le plugin " + nomClasse + " est introuvable");
			return null;
		}
		catch (NoClassDefFoundError e ) {
			// Survient si la classe n'a pas le bon nom
			e.printStackTrace();
			logger.warning("La classe " + nomClasse + " ne peut tre trouve");
			return null;
		}
	}
}