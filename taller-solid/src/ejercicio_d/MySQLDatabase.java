package ejercicio_d;

public class MySQLDatabase implements BaseDatos {
    @Override
    public void guardar(String dato) {
        System.out.println("[MySQL] Guardando: " + dato);
    }
}
