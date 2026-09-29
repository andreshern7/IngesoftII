package ejercicio_o;

public class EnvioMismoDia implements TarifaEnvio {
  @Override
  public double calcular(double peso) {
    return peso * 8000 + 20000;
  }
}
