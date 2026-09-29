package ejercicio_i;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    ImpresoraMultifuncional multi = new ImpresoraMultifuncional();
    ImpresoraBasica basica = new ImpresoraBasica();
    EscanerBasico escaner = new EscanerBasico();

    // Cada cliente depende solo de la capacidad que usa.
    List<Impresora> impresoras = List.of(multi, basica);
    for (Impresora i : impresoras)
      i.imprimir("contrato.pdf");

    List<Escaner> escaneres = List.of(multi, escaner);
    for (Escaner e : escaneres)
      e.escanear("cédula.jpg");

    multi.enviarFax("factura.pdf");
    multi.fotocopiar("acta.pdf");
  }
}
