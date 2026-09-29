package ejercicio_s;

public class Main {
  public static void main(String[] args) {
    Estudiante ana = new Estudiante("Ana Gomez", new double[] { 4.5, 3.8, 4.2 });

    RepositorioEstudiantes repositorio = new RepositorioEstudiantes("estudiants.txt");
    GeneradorBoletin generador = new GeneradorBoletin();
    NotificadorAcudiente notificador = new NotificadorAcudiente();

    repositorio.guardar(ana);
    String boletin = generador.generar(ana);
    System.out.println(boletin);
    notificador.enviarBoletin(ana, boletin);
  }
}
