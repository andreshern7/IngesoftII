package ejercicio_s;

/**
 * Responsabilidad única: el formato del boletín. Si el colegio cambia el
 * formato, solo cambia esta clase.
 */
public class GeneradorBoletin {
  public String generar(Estudiante estudiante) {
    return "=== BOLETIN ===\n"
        + "Nombre: " + estudiante.getNombre() + "\n"
        + "Promedio: " + estudiante.calcularPromedio();
  }
}
