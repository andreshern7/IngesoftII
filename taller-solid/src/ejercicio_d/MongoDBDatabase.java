package ejercicio_d;

public class MongoDBDatabase implements BaseDatos {
  @Override
  public void guardar(String dato) {
    System.out.println("[MongoDB] Guardando: " + dato);
  }
}
