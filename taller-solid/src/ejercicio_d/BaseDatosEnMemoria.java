package ejercicio_d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseDatosEnMemoria implements BaseDatos {
  private final List<String> datos = new ArrayList<>();

  @Override
  public void guardar(String dato) {
    datos.add(dato);
  }

  public List<String> getDatos() {
    return Collections.unmodifiableList(datos);
  }
}
