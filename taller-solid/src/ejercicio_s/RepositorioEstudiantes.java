package ejercicio_s;

/** Responsabilidad única: persistir estudiantes. Si se cambia el archivo por una BD, solo cambia esta clase. */
public class RepositorioEstudiantes {
    private final String rutaArchivo;

    public RepositorioEstudiantes(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void guardar(Estudiante estudiante) {
        System.out.println("Guardando " + estudiante.getNombre() + " en " + rutaArchivo + "...");
    }
}
