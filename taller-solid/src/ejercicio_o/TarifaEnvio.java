package ejercicio_o;

/**
 * Esta es la abtracción base ya luego cada tipo de envío tendrá que especificar
 * como se calcula su propio costo.
 */
public interface TarifaEnvio {
  double calcular(double peso);
}
