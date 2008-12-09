package plugin;

/**
 * Interface generique que doivent implementer tous les plugins.
 * Les classes qui implementent cette interface devront aussi fournir
 * un constructeur sans parametre (d'une façon implicite ou non).
 *
 * @author  Richard Grin (modifie de la version de Michel Buffa)
 * @version 2.0 7/12/02
 */
public interface Plugin {
  /**
   * @return le nom du plugin
   */
  public String getName();

  /**
   * @return le type du plugin
   */
  public Class getType();
}
