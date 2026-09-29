package ejercicio_s;

/** Responsabilidad única: los datos académicos del estudiante y las reglas sobre ellos. */
public class Estudiante {
    private final String nombre;
    private final double[] notas;

    public Estudiante(String nombre, double[] notas) {
        this.nombre = nombre;
        this.notas = notas.clone();
    }

    public String getNombre() {
        return nombre;
    }

    public double calcularPromedio() {
        double suma = 0;
        for (double n : notas) suma += n;
        return suma / notas.length;
    }
}
