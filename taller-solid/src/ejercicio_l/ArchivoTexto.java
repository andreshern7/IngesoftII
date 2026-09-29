package ejercicio_l;

public class ArchivoTexto implements ArchivoEditable {
  private String contenido;

  public ArchivoTexto(String contenidoInicial) {
    this.contenido = contenidoInicial;
  }

  @Override
  public String leer() {
    return contenido;
  }

  @Override
  public void escribir(String texto) {
    contenido += texto;
  }
}
