package ejercicio_o;

import java.util.HashMap;
import java.util.Map;

/**
 * Cerrada para modificación: no conoce ningún tipo de envío concreto.
 * Abierta para extensión: se le registran tarifas nuevas desde afuera.
 */
public class CalculadoraEnvio {
    private final Map<String, TarifaEnvio> tarifas = new HashMap<>();

    public void registrar(String tipoEnvio, TarifaEnvio tarifa) {
        tarifas.put(tipoEnvio, tarifa);
    }

    public double calcular(String tipoEnvio, double peso) {
        TarifaEnvio tarifa = tarifas.get(tipoEnvio);
        if (tarifa == null) {
            throw new IllegalArgumentException("Tipo de envío no soportado");
        }
        return tarifa.calcular(peso);
    }
}
