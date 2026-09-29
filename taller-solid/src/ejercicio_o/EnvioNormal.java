package ejercicio_o;

public class EnvioNormal implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 2000;
    }
}
