set -e
git rm -q --ignore-unmatch download
mkdir -p datos && mv -f movimientos_ejemplo.csv datos/movimientos_ejemplo.csv
mkdir -p sql && mv -f 01_crear_tablas.sql sql/01_crear_tablas.sql
mkdir -p sql && mv -f 02_datos_prueba.sql sql/02_datos_prueba.sql
mkdir -p sql && mv -f 03_consultas.sql sql/03_consultas.sql
mkdir -p src/main/java/vetgestion && mv -f Main.java src/main/java/vetgestion/Main.java
mkdir -p src/main/java/vetgestion/control && mv -f GestorSalaEspera.java src/main/java/vetgestion/control/GestorSalaEspera.java
mkdir -p src/main/java/vetgestion/control && mv -f GestorStock.java src/main/java/vetgestion/control/GestorStock.java
mkdir -p src/main/java/vetgestion/control && mv -f GestorTurnos.java src/main/java/vetgestion/control/GestorTurnos.java
mkdir -p src/main/java/vetgestion/dao && mv -f ClienteDAO.java src/main/java/vetgestion/dao/ClienteDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f ConsultaDAO.java src/main/java/vetgestion/dao/ConsultaDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f InsumoDAO.java src/main/java/vetgestion/dao/InsumoDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f MascotaDAO.java src/main/java/vetgestion/dao/MascotaDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f TurnoDAO.java src/main/java/vetgestion/dao/TurnoDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f VacunacionDAO.java src/main/java/vetgestion/dao/VacunacionDAO.java
mkdir -p src/main/java/vetgestion/dao && mv -f VeterinarioDAO.java src/main/java/vetgestion/dao/VeterinarioDAO.java
mkdir -p src/main/java/vetgestion/db && mv -f ConexionBD.java src/main/java/vetgestion/db/ConexionBD.java
mkdir -p src/main/java/vetgestion/excepcion && mv -f PersistenciaException.java src/main/java/vetgestion/excepcion/PersistenciaException.java
mkdir -p src/main/java/vetgestion/excepcion && mv -f StockExcepcion.java src/main/java/vetgestion/excepcion/StockExcepcion.java
mkdir -p src/main/java/vetgestion/model && mv -f Cliente.java src/main/java/vetgestion/model/Cliente.java
mkdir -p src/main/java/vetgestion/model && mv -f ConsultaMedica.java src/main/java/vetgestion/model/ConsultaMedica.java
mkdir -p src/main/java/vetgestion/model && mv -f Describible.java src/main/java/vetgestion/model/Describible.java
mkdir -p src/main/java/vetgestion/model && mv -f Insumo.java src/main/java/vetgestion/model/Insumo.java
mkdir -p src/main/java/vetgestion/model && mv -f Mascota.java src/main/java/vetgestion/model/Mascota.java
mkdir -p src/main/java/vetgestion/model && mv -f Medicamento.java src/main/java/vetgestion/model/Medicamento.java
mkdir -p src/main/java/vetgestion/model && mv -f Turno.java src/main/java/vetgestion/model/Turno.java
mkdir -p src/main/java/vetgestion/model && mv -f Vacuna.java src/main/java/vetgestion/model/Vacuna.java
mkdir -p src/main/java/vetgestion/model && mv -f Vacunacion.java src/main/java/vetgestion/model/Vacunacion.java
mkdir -p src/main/java/vetgestion/model && mv -f Veterinario.java src/main/java/vetgestion/model/Veterinario.java
mkdir -p src/main/java/vetgestion/mvc/controlador && mv -f ControladorStock.java src/main/java/vetgestion/mvc/controlador/ControladorStock.java
mkdir -p src/main/java/vetgestion/mvc/modelo && mv -f ArchivoStock.java src/main/java/vetgestion/mvc/modelo/ArchivoStock.java
mkdir -p src/main/java/vetgestion/mvc/modelo && mv -f ItemStock.java src/main/java/vetgestion/mvc/modelo/ItemStock.java
mkdir -p src/main/java/vetgestion/mvc/modelo && mv -f ModeloStock.java src/main/java/vetgestion/mvc/modelo/ModeloStock.java
mkdir -p src/main/java/vetgestion/mvc/vista && mv -f VistaConsola.java src/main/java/vetgestion/mvc/vista/VistaConsola.java
mkdir -p src/main/java/vetgestion/mvc/vista && mv -f VistaStock.java src/main/java/vetgestion/mvc/vista/VistaStock.java
mkdir -p src/main/java/vetgestion/util && mv -f Algoritmos.java src/main/java/vetgestion/util/Algoritmos.java
mkdir -p src/main/resources && mv -f config.properties src/main/resources/config.properties
cat > .gitignore <<'EOF'
# Archivos generados al compilar
target/
out/
*.class

# Configuracion de IDEs
.idea/
.vscode/
*.iml
.classpath
.project
.settings/

# Archivos generados por el modulo de stock
datos/reposicion_insumos.csv
datos/importacion_errores.log
dependency-reduced-pom.xml
EOF
git add -A && git commit -qm "Etapa 4: ordenar estructura de carpetas" && git push && ls
