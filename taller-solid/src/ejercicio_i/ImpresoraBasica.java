package ejercicio_i;

//Es la abtracción más elemental del problema
public class ImpresoraBasica implements Impresora {
  public void imprimir(String d) {
    System.out.println("[Básica] Imprimiendo " + d);
  }
}
