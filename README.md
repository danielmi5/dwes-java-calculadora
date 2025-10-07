# Calc21 — Calculadora educativa (Java 21)

- Expresiones: `+ - * / ^`, paréntesis, funciones `sin(x)`, `cos(x)`.
- Arquitectura por capas: Lexer → Parser → AST → Evaluator → REPL.
- Java moderno: records + sealed, switch con pattern matching, text blocks.
- Ejecutar:
  ```bash
  ./gradlew run
  ```
- Tests:
  ```bash
  ./gradlew test
  ```

> Si falta el wrapper JAR: `gradle wrapper --gradle-version 8.9` (una vez).
## Retos del profesor
Retos del profesor respondidos en [RETOS_PROFESOR.md](https://github.com/danielmi5/dwes-java-calculadora/blob/main/RETOS_PROFESOR.md)

## Retos de los grupos

Retos que se han realizado: 

- Función para limpiar la terminal.
- Ans añadido para usar el último resultado.
- Cálculo del módulo (%).
- Cálculo de la tangente (tan()).
- Historial para la calculadora en un txt en una carpeta data/
- Cálculo de raíces (sqrt()).
