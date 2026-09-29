package ejercicio_l;

public class ArchivoSoloLectura implements Archivo {
  private final String contenido;

  public ArchivoSoloLectura(String contenido) {
    this.contenido = contenido;
  }

  @Override
  public String leer() {
    return contenido;
  }
}
