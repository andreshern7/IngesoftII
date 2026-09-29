package ejercicio_s;

/** Responsabilidad única: comunicarse con el acudiente. */
public class NotificadorAcudiente {
  public void enviarBoletin(Estudiante estudiante, String boletin) {
    System.out.println("Enviando boletin por correo al acudiente de " + estudiante.getNombre());
  }
}
