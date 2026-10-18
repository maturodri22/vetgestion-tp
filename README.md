# VetGestión — Prototipo

Prototipo funcional en Java con persistencia en MySQL del sistema de gestión de turnos,
historia clínica y stock para la Clínica Veterinaria PetLife, desarrollado como parte del
Trabajo Práctico de Análisis y Diseño de Software (Etapas 1 a 3: análisis, diseño y programación orientada a objetos).

## Requisitos

- JDK 17 o superior
- MySQL 8.0 o superior
- Maven (o un IDE que lo gestione: IntelliJ, Eclipse, NetBeans)

## 1. Crear la base de datos

Ejecutar en MySQL, en este orden:

```bash
mysql -u root -p < sql/01_crear_tablas.sql
mysql -u root -p < sql/02_datos_prueba.sql
```

(`sql/03_consultas.sql` contiene las consultas documentadas en el informe, para probarlas manualmente si se desea.)

## 2. Configurar la conexión

Editar `src/main/resources/config.properties` con el usuario y la contraseña de tu MySQL local:

```properties
db.url=jdbc:mysql://localhost:3306/vetgestion?useSSL=false&serverTimezone=UTC
db.user=root
db.password=TU_PASSWORD_AQUI
```

## 3. Compilar y ejecutar

Con Maven:

```bash
mvn clean package
java -jar target/vetgestion.jar
```

O bien, importar el proyecto como Maven Project en tu IDE y ejecutar la clase `vetgestion.Main`.

## Estructura del proyecto

```
vetgestion-java/
├── pom.xml
├── sql/
│   ├── 01_crear_tablas.sql
│   ├── 02_datos_prueba.sql
│   └── 03_consultas.sql
└── src/main/
    ├── java/vetgestion/
    │   ├── Main.java                          → menú de consola (prototipo operacional)
    │   ├── model/                             → clases de entidad
    │   │   ├── Cliente.java, Mascota.java, Veterinario.java
    │   │   ├── Turno.java, ConsultaMedica.java, Vacunacion.java
    │   │   ├── Insumo.java (abstracta), Vacuna.java, Medicamento.java
    │   │   └── Describible.java (interfaz)
    │   ├── excepcion/StockExcepcion.java      → excepción propia (stock insuficiente)
    │   ├── util/Algoritmos.java               → ordenación por inserción y búsqueda binaria
    │   ├── db/ConexionBD.java                 → conexión JDBC
    │   ├── dao/                                → acceso a datos (uno por tabla)
    │   └── control/
    │       ├── GestorTurnos.java              → caso de uso "Agendar turno"
    │       ├── GestorStock.java               → stock, pila de deshacer, ordenación y búsqueda
    │       └── GestorSalaEspera.java          → cola de la sala de espera
    └── resources/config.properties
```

## Correspondencia con el informe (Etapa 2)

| Documento | Código |
|---|---|
| Diagrama de secuencia (CU "Agendar turno") | `control/GestorTurnos.java` |
| Diagrama de clases de diseño (generalización Insumo/Vacuna/Medicamento) | `model/Insumo.java`, `Vacuna.java`, `Medicamento.java` |
| Creación de tablas MySQL | `sql/01_crear_tablas.sql` |
| Inserción / consulta / borrado | `sql/02_datos_prueba.sql`, métodos de los DAO |
| Presentación de consultas SQL | `sql/03_consultas.sql`, métodos `turnosPorFecha`, `historialPorMascota`, `listarEnStockMinimo` |

## Etapa 3: conceptos de POO aplicados

| Concepto | Dónde se aplica |
|---|---|
| Encapsulamiento | `Insumo`: atributos privados; el stock sólo cambia con `retirar()`, `reponer()` y `setCantidadActual()`, que validan la operación. `ConsultaMedica.validarPeso()` |
| Herencia | `Vacuna` y `Medicamento` extienden a `Insumo` (`super(...)`); `StockExcepcion` extiende a `Exception` |
| Abstracción | Clase abstracta `Insumo` (métodos abstractos `getTipo()` y `registrarAlerta()`); interfaz `Describible` |
| Polimorfismo | `insumo.registrarAlerta()` y `getDescripcion()` responden según el tipo real; en la opción 19 una única `List<Describible>` mezcla insumos y mascotas |
| Excepciones | `StockExcepcion` (opción 12); `try/catch` de `NumberFormatException` y `DateTimeParseException` en la lectura de datos; `IllegalArgumentException` en validaciones; `SQLException` en el menú |
| Cola (`Queue` / `LinkedList`) | `GestorSalaEspera`: la sala de espera se atiende por orden de llegada (opciones 14 a 16) |
| Pila (`Deque` / `ArrayDeque`) | `GestorStock`: deshacer el último movimiento de stock (opción 13) |
| Ordenación y búsqueda | `Algoritmos`: inserción y búsqueda binaria, usadas en las opciones 17 y 18 |
