package ejercicio_o;

public class Main {
  public static void main(String[] args) {
    // intenté hacerlo con el patron factory pero me enrredé, entonces lo hice
    // como manteniendo la idea
    CalculadoraEnvio calculadora = new CalculadoraEnvio();
    calculadora.registrar("NORMAL", new EnvioNormal());
    calculadora.registrar("EXPRESS", new EnvioExpress());
    calculadora.registrar("INTERNACIONAL", new EnvioInternacional());
    calculadora.registrar("MISMO_DIA", new EnvioMismoDia()); // extensión del reto

    double peso = 3;
    for (String tipo : new String[] { "NORMAL", "EXPRESS", "INTERNACIONAL", "MISMO_DIA" }) {
      System.out.printf("%-14s %.1f kg -> $%,.0f%n", tipo, peso, calculadora.calcular(tipo, peso));
    }

    String tipoEnvioCliente = "DRON";
    try {
      double tarifaEnvioCliente = calculadora.calcular(tipoEnvioCliente, peso);
      System.out.println("La tarifa de tu objeto de peso " + peso + " es de " + tarifaEnvioCliente);
    } catch (IllegalArgumentException e) {
      System.out.println("Error esperado: " + "'" + tipoEnvioCliente + "'" + " " + e.getMessage());
    }
  }
}
