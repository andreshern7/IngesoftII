package ejercicio_d;

import java.util.Objects;

public class ServicioUsuarios {
  private final BaseDatos db;

  /** Aqui se ve la inyección por constructor */
  public ServicioUsuarios(BaseDatos db) {
    this.db = Objects.requireNonNull(db, "Se requiere una base de datos");
  }

  public void registrar(String nombreUsuario) {
    if (nombreUsuario == null || nombreUsuario.isBlank()) {
      throw new IllegalArgumentException("Nombre inválido");
    }
    db.guardar(nombreUsuario);
  }
}
