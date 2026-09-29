package ejercicio_d;

/** Abstracción de la que dependen tanto el servicio (alto nivel) como las BD concretas (bajo nivel). */
public interface BaseDatos {
    void guardar(String dato);
}
