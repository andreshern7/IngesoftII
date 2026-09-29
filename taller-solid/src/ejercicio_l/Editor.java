package ejercicio_l;

import java.util.List;

public class Editor {
  // Si un archivo va a pasar por la clase editor entonces lo lógico seria que
  // fuera un archivo editable, modificable o escribible
  public void agregarFirma(List<? extends ArchivoEditable> archivos) {
    for (ArchivoEditable a : archivos) {
      a.escribir("\n-- Firmado por el sistema");
    }
  }
}
