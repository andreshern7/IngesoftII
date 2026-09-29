package ejercicio_o;

public class EnvioInternacional implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 15000 + 50000;
    }
}
