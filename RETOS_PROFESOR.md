# RETOS_PROFESOR

## Retos de comprensión
- Explica qué es un **Token** y da un ejemplo de cómo aparece en la expresión `3 + 5 * 2`.  
Un token es una unidad mínima con significado.  
La expresión `3 + 5 * 2` se convierte en: [NUMBER("3"), PLUS("+"), NUMBER("5"), STAR("*"), NUMBER("2")]
 
- ¿Qué diferencia hay entre el **Lexer** y el **Parser**?  
El Lexer convierte la cadena introducida en tokens y el Parser organiza los tokens en una estructura jerárquica llamada AST (Abstract Syntax Tree).

- ¿Qué significa que el parser sea **recursivo**? Pon un ejemplo de función que lo demuestra.
Que se puede llamar a sí mismo. Ejemplo:
(https://github.com/danielmi5/dwes-java-calculadora/blob/a4b3650af4b8c928578108b388b7ef4fabad9b47/src/main/java/com/example/calc/Parser.java#L51-L58
El método unary() implementa recursión porque, cuando detecta un operador unario (+ o -), se llama a sí misma para comprobar la siguiente expresión. 

## Retos de depuración
- ¿Qué devuelve la calculadora con la entrada: `2 + 3 * 4` y por qué?  
Devuelve 14.0, porque 
- ¿Qué ocurre si escribimos una expresión no válida como `2 + *`? ¿Cómo reacciona el código?  
Lanza y muestra un Error: Token inesperado: STAR en pos 4.
- ¿Qué devuelve si calculamos `(2 + 3) ^ 2`?  
Devuelve 25.0

## Retos de modificación
- Añade la función `tan(x)` usando el mismo esquema que `sin` y `cos`. Además, modifica el parser para que acepte también la función `sqrt(x)`.
Simplemente añadí a la clase Evaluator en el caso "Call", la tangente y sqrt:
``` java
case Call c -> {
    double x = eval(c.arg());
    yield switch (c.name()) {
        case "sin" -> Math.sin(x);
        case "cos" -> Math.cos(x);
        case "tan" -> Math.tan(x);
        case "sqrt" -> Math.sqrt(x);
        default -> throw new IllegalArgumentException("Función no soportada: " + c.name());
    };
}
```

- Haz que la calculadora acepte números negativos explícitos como `-5 + 3`.  
La calculadora ya admite números negativos.

## Retos de predicción
- ¿Qué resultado debería devolver esta expresión y por qué?: `cos(0) + sin(90)`  
  *(recuerda: las funciones trigonométricas usan radianes).*
- ¿Cuál es el resultado de `2 ^ 3 ^ 2`? Explica por qué según la precedencia implementada.
- ¿Qué devuelve la calculadora con `(2 + 3) * (4 + 5)`?  
Devuelve 45.0

## Retos de diseño
- El código actual usa un parser recursivo. ¿Qué ventaja tiene frente a procesar los tokens con un bucle y pila manual? 
Un parser recursivo hace el código más simple y fácil de leer porque la pila de llamadas ya maneja por sí sola la anidación y la precedencia de operadores, sin necesidad de implementar estructuras extra. Esto reduce la lógica que hay que escribir y facilita mantener o extender el parser en el futuro.

- ¿Por qué es útil separar la calculadora en las fases **lexer → parser → evaluator** en vez de hacerlo todo en un solo método?  
Es útil porque separa las responsabilidades en tres clases independientes: el Lexer convierte caracteres en tokens, el Parser transforma tokens en AST, y el Evaluator ejecuta el AST para producir resultados. Esto permite modificar, testear y reutilizar cada clase por separado sin afectar a las demás clases, y además, facilita el mantenimiento y proporciona un mejor manejo de los errores,

- Si quisieras añadir soporte para variables (`x = 5`, `y = 2 * x`), ¿en qué parte del código lo implementarías y por qué?
