package ejercicio_l;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    ArchivoTexto informe = new ArchivoTexto("Informe trimestral");
    ArchivoTexto acta = new ArchivoTexto("Acta de reunión");
    ArchivoSoloLectura reglamento = new ArchivoSoloLectura("Reglamento interno");

    Editor editor = new Editor();
    editor.agregarFirma(List.of(informe, acta));

    // Cualquier Archivo (escribible o no) sirve donde se espera leer.
    List<Archivo> todos = List.of(informe, acta, reglamento);
    for (Archivo a : todos) {
      System.out.println(a.leer());
      System.out.println("----");
    }
  }
}
