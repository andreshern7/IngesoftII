package ejercicio_d;

import java.util.List;

/** Prueba de registrar() sin tocar MySQL, usando BaseDatosEnMemoria. */
public class PruebaServicioUsuarios {
    public static void main(String[] args) {
        // 1. Un nombre válido queda guardado
        BaseDatosEnMemoria db = new BaseDatosEnMemoria();
        new ServicioUsuarios(db).registrar("jperez");
        verificar(db.getDatos().equals(List.of("jperez")), "registrar guarda un nombre válido");

        // 2. Un nombre en blanco se rechaza y no se guarda nada
        BaseDatosEnMemoria db2 = new BaseDatosEnMemoria();
        verificar(lanzaExcepcion(() -> new ServicioUsuarios(db2).registrar("   ")), "rechaza nombre en blanco");
        verificar(db2.getDatos().isEmpty(), "no guarda nada si el nombre es inválido");

        // 3. Un nombre nulo se rechaza
        verificar(lanzaExcepcion(() -> new ServicioUsuarios(db2).registrar(null)), "rechaza nombre nulo");

        System.out.println("Todas las pruebas pasaron.");
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void verificar(boolean condicion, String descripcion) {
        if (!condicion) throw new AssertionError("FALLÓ: " + descripcion);
        System.out.println("OK  " + descripcion);
    }
}
