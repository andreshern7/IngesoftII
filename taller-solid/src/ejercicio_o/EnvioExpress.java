package ejercicio_o;

public class EnvioExpress implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 5000 + 10000;
    }
}
