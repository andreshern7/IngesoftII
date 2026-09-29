package ejercicio_d;

public class Main {
  public static void main(String[] args) {
    // Una persona externa al programa elige qué base de datos se usa.
    new ServicioUsuarios(new MySQLDatabase()).registrar("jperez");
    new ServicioUsuarios(new MongoDBDatabase()).registrar("jperez");
  }
}
