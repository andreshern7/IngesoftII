package ejercicio_i;

public class ImpresoraMultifuncional implements Impresora, Escaner, Fax, Fotocopiadora {
    public void imprimir(String d)   { System.out.println("[Multifuncional] Imprimiendo " + d); }
    public void escanear(String d)   { System.out.println("[Multifuncional] Escaneando " + d); }
    public void enviarFax(String d)  { System.out.println("[Multifuncional] Enviando fax " + d); }
    public void fotocopiar(String d) { System.out.println("[Multifuncional] Fotocopiando " + d); }
}
