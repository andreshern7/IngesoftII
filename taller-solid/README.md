# Laboratorio: Arregla el código — Principios SOLID

**Ingeniería de Software II · Universidad Nacional de Colombia · Sede Bogotá · 2026**

- **Lenguaje elegido:** Java
- **Integrantes:** Jorge Andrés Hernández García - jorhernandezga@unal.edu.co
- **Mecanismo de abstracción usado:** `interface` de Java.

## Estructura carpetas

```
src/
├── ejercicio_s/   Single Responsibility
├── ejercicio_o/   Open/Closed
├── ejercicio_l/   Liskov Substitution
├── ejercicio_i/   Interface Segregation
└── ejercicio_d/   Dependency Inversion
```

---

## Ejercicio S — Single Responsibility Principle

### Problema identificado

Al describir la clase en una frase salen cuatro "y": *Estudiante guarda sus notas **y** calcula el promedio **y** se guarda en un archivo **e** imprime su boletín **y** avisa al acudiente.* Eso son cuatro razones distintas para cambiar (reglas académicas, persistencia, formato del boletín y canal de notificación) metidas en una sola clase. Si el colegio cambia el formato del boletín hay que tocar `Estudiante`; si el archivo se cambia por una base de datos, también. Cualquiera de esos cambios pone en riesgo el cálculo del promedio, que no tenía nada que ver.

### Código corregido

**`Estudiante.java`**

```java
package ejercicio_s;

/** Responsabilidad única: los datos académicos del estudiante y las reglas sobre ellos. */
public class Estudiante {
    private final String nombre;
    private final double[] notas;

    public Estudiante(String nombre, double[] notas) {
        this.nombre = nombre;
        this.notas = notas.clone();
    }

    public String getNombre() {
        return nombre;
    }

    public double calcularPromedio() {
        double suma = 0;
        for (double n : notas) suma += n;
        return suma / notas.length;
    }
}
```

**`RepositorioEstudiantes.java`**

```java
package ejercicio_s;

/** Responsabilidad única: persistir estudiantes. Si se cambia el archivo por una BD, solo cambia esta clase. */
public class RepositorioEstudiantes {
    private final String rutaArchivo;

    public RepositorioEstudiantes(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void guardar(Estudiante estudiante) {
        System.out.println("Guardando " + estudiante.getNombre() + " en " + rutaArchivo + "...");
    }
}
```

**`GeneradorBoletin.java`**

```java
package ejercicio_s;

/** Responsabilidad única: el formato del boletín. Si el colegio cambia el formato, solo cambia esta clase. */
public class GeneradorBoletin {
    public String generar(Estudiante estudiante) {
        return "=== BOLETÍN ===\n"
             + "Nombre: " + estudiante.getNombre() + "\n"
             + "Promedio: " + estudiante.calcularPromedio();
    }
}
```

**`NotificadorAcudiente.java`**

```java
package ejercicio_s;

/** Responsabilidad única: comunicarse con el acudiente. */
public class NotificadorAcudiente {
    public void enviarBoletin(Estudiante estudiante, String boletin) {
        System.out.println("Enviando boletín por correo al acudiente de " + estudiante.getNombre());
    }
}
```

**`Main.java`**

```java
package ejercicio_s;

public class Main {
    public static void main(String[] args) {
        Estudiante ana = new Estudiante("Ana Gómez", new double[]{4.5, 3.8, 4.2});

        RepositorioEstudiantes repositorio = new RepositorioEstudiantes("estudiantes.txt");
        GeneradorBoletin generador = new GeneradorBoletin();
        NotificadorAcudiente notificador = new NotificadorAcudiente();

        repositorio.guardar(ana);
        String boletin = generador.generar(ana);
        System.out.println(boletin);
        notificador.enviarBoletin(ana, boletin);
    }
}
```

### Justificación

Cada clase tiene ahora un único motivo de cambio: el formato del boletín solo toca `GeneradorBoletin`, pasar de archivo a base de datos solo toca `RepositorioEstudiantes`, y cambiar correo por WhatsApp solo toca `NotificadorAcudiente`. `Estudiante` queda con sus datos y la regla del promedio. El generador devuelve el texto en lugar de imprimirlo, así el mismo boletín sirve para pantalla y para el correo.

### Evidencia

```
Guardando Ana Gomez en estudiante.txt...
=== BOLETIN ===
Nombre: Ana Gomez
Promedio: 4.1666666667
Enviando boletin por correo al acudiente de Ana Gomez
```

---

## Ejercicio O — Open/Closed Principle

### Problema identificado

Para agregar `MISMO_DIA` hay que abrir `CalculadoraEnvio` y meter otro `else if` en `calcular`. Cada tipo nuevo modifica código que ya funcionaba y obliga a volver a probar todas las tarifas. Con 15 tipos de envío el método se vuelve una cadena de 15 condiciones donde conviven todas las reglas de precios, difícil de leer y fácil de romper; además, un error de tipeo en el `String` solo se descubre en ejecución.

### Código corregido

**`TarifaEnvio.java`**

```java
package ejercicio_o;

/** Abstracción estable: cada tipo de envío sabe calcular su propio costo. */
public interface TarifaEnvio {
    double calcular(double peso);
}
```

**`EnvioNormal.java`**

```java
package ejercicio_o;

public class EnvioNormal implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 2000;
    }
}
```

**`EnvioExpress.java`**

```java
package ejercicio_o;

public class EnvioExpress implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 5000 + 10000;
    }
}
```

**`EnvioInternacional.java`**

```java
package ejercicio_o;

public class EnvioInternacional implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 15000 + 50000;
    }
}
```

**`EnvioMismoDia.java`**

```java
package ejercicio_o;

/** Reto extra: nuevo tipo de envío agregado SIN modificar ninguna clase existente. */
public class EnvioMismoDia implements TarifaEnvio {
    @Override
    public double calcular(double peso) {
        return peso * 8000 + 20000;
    }
}
```

**`CalculadoraEnvio.java`**

```java
package ejercicio_o;

import java.util.HashMap;
import java.util.Map;

/**
 * Cerrada para modificación: no conoce ningún tipo de envío concreto.
 * Abierta para extensión: se le registran tarifas nuevas desde afuera.
 */
public class CalculadoraEnvio {
    private final Map<String, TarifaEnvio> tarifas = new HashMap<>();

    public void registrar(String tipoEnvio, TarifaEnvio tarifa) {
        tarifas.put(tipoEnvio, tarifa);
    }

    public double calcular(String tipoEnvio, double peso) {
        TarifaEnvio tarifa = tarifas.get(tipoEnvio);
        if (tarifa == null) {
            throw new IllegalArgumentException("Tipo de envío no soportado");
        }
        return tarifa.calcular(peso);
    }
}
```

**`Main.java`**

```java
package ejercicio_o;

public class Main {
    public static void main(String[] args) {
        CalculadoraEnvio calculadora = new CalculadoraEnvio();
        calculadora.registrar("NORMAL", new EnvioNormal());
        calculadora.registrar("EXPRESS", new EnvioExpress());
        calculadora.registrar("INTERNACIONAL", new EnvioInternacional());
        calculadora.registrar("MISMO_DIA", new EnvioMismoDia()); // extensión del reto

        double peso = 3;
        for (String tipo : new String[]{"NORMAL", "EXPRESS", "INTERNACIONAL", "MISMO_DIA"}) {
            System.out.printf("%-14s %.1f kg -> $%,.0f%n", tipo, peso, calculadora.calcular(tipo, peso));
        }

        try {
            calculadora.calcular("DRON", peso);
        } catch (IllegalArgumentException e) {
            System.out.println("DRON           -> error esperado: " + e.getMessage());
        }
    }
}
```

### Justificación

La calculadora ya no conoce ninguna tarifa concreta: depende de la interfaz `TarifaEnvio`. El reto `MISMO_DIA` se resolvió creando `EnvioMismoDia` y registrándola en `Main` (el punto donde se configura la aplicación), sin editar `CalculadoraEnvio` ni ninguna tarifa existente. No se usó un `enum` con `switch` porque cada tipo nuevo volvería a exigir modificar el `switch`.

### Evidencia

```
NORMAL         3.0 kg -> $6,000
EXPRESS        3.0 kg -> $25,000
INTERNACIONAL  3.0 kg -> $95,000
MISMO_DIA      3.0 kg -> $44,000
DRON           -> error esperado: Tipo de envío no soportado
```

---

## Ejercicio L — Liskov Substitution Principle

### Problema identificado

`ArchivoSoloLectura` hereda un `escribir()` que no puede cumplir y lo "resuelve" lanzando una excepción. `Editor` confía en el contrato de `Archivo` (todo archivo se puede escribir) y se cae apenas encuentra uno de solo lectura. Al ejecutar el código original con la lista `[Archivo, ArchivoSoloLectura, Archivo]`, el primero se firma, el segundo lanza la excepción y el tercero nunca se procesa: el lote queda a medias.

```
Exception in thread "main" java.lang.UnsupportedOperationException: Este archivo es de solo lectura
	at ArchivoSoloLectura.escribir(...)
	at Editor.agregarFirma(...)
```

Agregar `if (!(a instanceof ArchivoSoloLectura))` es un parche: el `Editor` tendría que conocer cada subclase "problemática", cada caso nuevo obligaría a modificarlo, y cualquier otro cliente que llame `escribir()` seguiría fallando. La raíz es la jerarquía: un archivo de solo lectura **no es** un archivo escribible; solo comparte con él la capacidad de ser leído.

### Código corregido

**`Archivo.java`**

```java
package ejercicio_l;

/** Contrato mínimo que cumplen TODOS los archivos: se pueden leer. */
public interface Archivo {
    String leer();
}
```

**`ArchivoEscribible.java`**

```java
package ejercicio_l;

/** Solo los archivos que de verdad se pueden escribir prometen escribir(). */
public interface ArchivoEscribible extends Archivo {
    void escribir(String texto);
}
```

**`ArchivoTexto.java`**

```java
package ejercicio_l;

public class ArchivoTexto implements ArchivoEscribible {
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
```

**`ArchivoSoloLectura.java`**

```java
package ejercicio_l;

/** Ya no hereda un escribir() que no puede cumplir: solo promete lo que sí hace. */
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
```

**`Editor.java`**

```java
package ejercicio_l;

import java.util.List;

public class Editor {
    /** Pide exactamente lo que necesita: archivos escribibles. */
    public void agregarFirma(List<? extends ArchivoEscribible> archivos) {
        for (ArchivoEscribible a : archivos) {
            a.escribir("\n-- Firmado por el sistema");
        }
    }
}
```

**`Main.java`**

```java
package ejercicio_l;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArchivoTexto informe = new ArchivoTexto("Informe trimestral");
        ArchivoTexto acta = new ArchivoTexto("Acta de reunión");
        ArchivoSoloLectura reglamento = new ArchivoSoloLectura("Reglamento interno");

        Editor editor = new Editor();
        editor.agregarFirma(List.of(informe, acta));
        // editor.agregarFirma(List.of(informe, reglamento));  // NO COMPILA: el error se detecta antes de ejecutar

        // Sustitución válida: cualquier Archivo (escribible o no) sirve donde se espera leer.
        List<Archivo> todos = List.of(informe, acta, reglamento);
        for (Archivo a : todos) {
            System.out.println(a.leer());
            System.out.println("----");
        }
    }
}
```

### Justificación

Separamos el contrato en `Archivo` (leer) y `ArchivoEscribible` (leer y escribir), y cada clase promete solo lo que cumple, así que sustituir un subtipo nunca rompe al cliente. `Editor` pide exactamente lo que necesita, y el intento de firmar un archivo de solo lectura lo rechaza el compilador en lugar de explotar en ejecución, sin `instanceof` ni `try/catch`. Usamos interfaces en vez de clase base porque las dos implementaciones no comparten lógica, solo la capacidad de lectura.

### Evidencia

```
Informe trimestral
-- Firmado por el sistema
----
Acta de reunión
-- Firmado por el sistema
----
Reglamento interno
----
```

Si se intenta pasarle al editor un archivo de solo lectura, el programa ni siquiera compila:

```
Neg.java:3: error: incompatible types: inference variable E has incompatible bounds
    upper bounds: ArchivoEscribible,Object
    lower bounds: ArchivoSoloLectura,ArchivoTexto
```

---

## Ejercicio I — Interface Segregation Principle

### Problema identificado

`Dispositivo` es una interfaz "gorda" que obliga a `ImpresoraBasica` a implementar tres métodos que no puede cumplir, y los deja vacíos. Si alguien llama `impresoraBasica.escanear("contrato")` no pasa nada y nadie se entera de que falló: el usuario cree que escaneó. Además, si se agrega `enviarPorCorreo` a la interfaz, hay que modificar las dos clases (y todas las futuras), aunque solo una lo soporte. Un cliente que solo quiere imprimir termina dependiendo de fax, escáner y fotocopiadora.

### Código corregido

**`Impresora.java`**

```java
package ejercicio_i;

public interface Impresora {
    void imprimir(String documento);
}
```

**`Escaner.java`**

```java
package ejercicio_i;

public interface Escaner {
    void escanear(String documento);
}
```

**`Fax.java`**

```java
package ejercicio_i;

public interface Fax {
    void enviarFax(String documento);
}
```

**`Fotocopiadora.java`**

```java
package ejercicio_i;

public interface Fotocopiadora {
    void fotocopiar(String documento);
}
```

**`ImpresoraMultifuncional.java`**

```java
package ejercicio_i;

public class ImpresoraMultifuncional implements Impresora, Escaner, Fax, Fotocopiadora {
    public void imprimir(String d)   { System.out.println("[Multifuncional] Imprimiendo " + d); }
    public void escanear(String d)   { System.out.println("[Multifuncional] Escaneando " + d); }
    public void enviarFax(String d)  { System.out.println("[Multifuncional] Enviando fax " + d); }
    public void fotocopiar(String d) { System.out.println("[Multifuncional] Fotocopiando " + d); }
}
```

**`ImpresoraBasica.java`**

```java
package ejercicio_i;

/** Solo implementa lo que sabe hacer: no hay métodos vacíos que fallen en silencio. */
public class ImpresoraBasica implements Impresora {
    public void imprimir(String d) { System.out.println("[Básica] Imprimiendo " + d); }
}
```

**`EscanerBasico.java`**

```java
package ejercicio_i;

/** Reto extra: un dispositivo que solo escanea. */
public class EscanerBasico implements Escaner {
    public void escanear(String d) { System.out.println("[Escáner] Escaneando " + d); }
}
```

**`Main.java`**

```java
package ejercicio_i;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ImpresoraMultifuncional multi = new ImpresoraMultifuncional();
        ImpresoraBasica basica = new ImpresoraBasica();
        EscanerBasico escaner = new EscanerBasico();

        // Cada cliente depende solo de la capacidad que usa.
        List<Impresora> impresoras = List.of(multi, basica);
        for (Impresora i : impresoras) i.imprimir("contrato.pdf");

        List<Escaner> escaneres = List.of(multi, escaner);
        for (Escaner e : escaneres) e.escanear("cédula.jpg");

        multi.enviarFax("factura.pdf");
        multi.fotocopiar("acta.pdf");

        // basica.escanear("contrato");  // NO COMPILA: ImpresoraBasica no promete escanear
    }
}
```

### Justificación

Hay una interfaz por capacidad, cada clase implementa solo lo que sabe hacer y cada cliente depende solo de lo que usa (`List<Impresora>`, `List<Escaner>`). Pedirle a una `ImpresoraBasica` que escanee pasó de ser un método vacío que falla en silencio a un error de compilación. Agregar `enviarPorCorreo` sería una interfaz nueva implementada solo por quien lo soporte; el reto `EscanerBasico` se agregó sin tocar ninguna clase existente.

### Evidencia

```
[Multifuncional] Imprimiendo contrato.pdf
[Básica] Imprimiendo contrato.pdf
[Multifuncional] Escaneando cédula.jpg
[Escáner] Escaneando cédula.jpg
[Multifuncional] Enviando fax factura.pdf
[Multifuncional] Fotocopiando acta.pdf
```

Intentar escanear con la impresora básica ya no compila:

```
Neg.java:2: error: cannot find symbol
  symbol:   method escanear(String)
  location: class ImpresoraBasica
```

---

## Ejercicio D — Dependency Inversion Principle

### Problema identificado

`ServicioUsuarios` contiene la regla de negocio (validar y registrar), pero crea por dentro un `MySQLDatabase` concreto con `new`: el módulo de alto nivel depende de un detalle de bajo nivel. Migrar a MongoDB obliga a editar el servicio (el tipo del campo y el `new`). Tampoco se puede probar `registrar` sin una base de datos real, porque la dependencia está escondida adentro. Hoy es `ServicioUsuarios` quien decide qué base de datos se usa, cuando esa decisión debería tomarse afuera.

### Código corregido

**`BaseDatos.java`**

```java
package ejercicio_d;

/** Abstracción de la que dependen tanto el servicio (alto nivel) como las BD concretas (bajo nivel). */
public interface BaseDatos {
    void guardar(String dato);
}
```

**`MySQLDatabase.java`**

```java
package ejercicio_d;

public class MySQLDatabase implements BaseDatos {
    @Override
    public void guardar(String dato) {
        System.out.println("[MySQL] Guardando: " + dato);
    }
}
```

**`MongoDBDatabase.java`**

```java
package ejercicio_d;

/** Migración a MongoDB: clase nueva, ServicioUsuarios no se toca. */
public class MongoDBDatabase implements BaseDatos {
    @Override
    public void guardar(String dato) {
        System.out.println("[MongoDB] Guardando: " + dato);
    }
}
```

**`BaseDatosEnMemoria.java`**

```java
package ejercicio_d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Reto extra: BD falsa para pruebas, guarda en una lista. */
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
```

**`ServicioUsuarios.java`**

```java
package ejercicio_d;

import java.util.Objects;

public class ServicioUsuarios {
    private final BaseDatos db;

    /** La BD la decide quien crea el servicio (inyección por constructor). */
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
```

**`Main.java`**

```java
package ejercicio_d;

public class Main {
    public static void main(String[] args) {
        // "Alguien de afuera" decide qué base de datos se usa.
        new ServicioUsuarios(new MySQLDatabase()).registrar("jperez");
        new ServicioUsuarios(new MongoDBDatabase()).registrar("jperez");
    }
}
```

**`PruebaServicioUsuarios.java`**

```java
package ejercicio_d;

import java.util.List;

/** Prueba de registrar() sin tocar MySQL, usando BaseDatosEnMemoria. */
public class PruebaServicioUsuarios {
    public static void main(String[] args) {
        // 1. Un nombre válido queda guardado
        BaseDatosEnMemoria db = new BaseDatosEnMemoria();
        new ServicioUsuarios(db).registrar("jperez");
        verificar(db.getDatos().equals(List.of("jperez")), "registrar guarda un nombre válido");

        // 2. Un nombre en blanco se rechaza y no se guarda nada
        BaseDatosEnMemoria db2 = new BaseDatosEnMemoria();
        verificar(lanzaExcepcion(() -> new ServicioUsuarios(db2).registrar("   ")), "rechaza nombre en blanco");
        verificar(db2.getDatos().isEmpty(), "no guarda nada si el nombre es inválido");

        // 3. Un nombre nulo se rechaza
        verificar(lanzaExcepcion(() -> new ServicioUsuarios(db2).registrar(null)), "rechaza nombre nulo");

        System.out.println("Todas las pruebas pasaron.");
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void verificar(boolean condicion, String descripcion) {
        if (!condicion) throw new AssertionError("FALLÓ: " + descripcion);
        System.out.println("OK  " + descripcion);
    }
}
```

### Justificación

`ServicioUsuarios` depende de la interfaz `BaseDatos` y la recibe por constructor, así que la base concreta la elige quien crea el servicio. Migrar a MongoDB fue agregar `MongoDBDatabase` sin tocar el servicio, y la prueba usa `BaseDatosEnMemoria` sin conectarse a nada. Se usó inyección manual por constructor, sin frameworks, porque el problema no necesita más.

### Evidencia

Ejecución normal, cambiando de base de datos desde afuera:

```
[MySQL] Guardando: jperez
[MongoDB] Guardando: jperez
```

Prueba de `registrar` con la base de datos en memoria (reto extra):

```
OK  registrar guarda un nombre válido
OK  rechaza nombre en blanco
OK  no guarda nada si el nombre es inválido
OK  rechaza nombre nulo
Todas las pruebas pasaron.
```
