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
Devuelve el resultado 14: primero multiplica 3 * 4 = 12, y luego suma 2 + 12 = 14.

1. **Lexer**: Convierte la cadena "2 + 3 * 4" en una lista de tokens (NUMBER(2), PLUS(+), NUMBER(3), STAR(*), NUMBER(4)).

2. **Parser**: A partir de los tokens, construye un árbol AST siguiendo las reglas de precedencia de operadores. Siendo este:
```
    +
   / \
  2   *
     / \
    3   4
```

3. **Evaluator**: Recibe el AST y lo evalúa recursivamente. Evalúa primero el nodo de mayor precedencia (multiplicación=3*4) y luego realiza la suma (2+12), devolviendo 14. 

- ¿Qué ocurre si escribimos una expresión no válida como `2 + *`? ¿Cómo reacciona el código?  
Lanza y muestra un Error: Token inesperado: STAR en pos 4.

1. **Lexer**: Convierte la cadena "2 * *" en una lista de tokens (NUMBER(2), PLUS(+), STAR(*)).

2. **Parser**: El parser espera que después de un operador "+" venga un valor válido (un número, una función o paréntesis). Como no coincide con ninguno al ser un "*" (STAR), lanza un error IllegalArgumentException personalizado:
```java
private Expr primary() {
        if (match(NUMBER)) return new NumberLit(Double.parseDouble(prev().lexeme()));
        if (match(IDENT)) {
            String name = prev().lexeme();
            expect(LPAREN, "Se esperaba '(' tras función");
            Expr arg = expr();
            expect(RPAREN, "Se esperaba ')' tras argumento");
            return new Call(name, arg);
        }
        if (match(LPAREN)) {
            Expr inside = expr();
            expect(RPAREN, "Se esperaba ')'");
            return inside;
        }
        throw error("Token inesperado: " + peek().type() + " en pos " + peek().position());
    }
```
Y después este error se muestra por pantalla.

- ¿Qué devuelve si calculamos `(2 + 3) ^ 2`?

Devuelve el resultado 25: primero suma 2 + 3 = 5, y luego eleva 5 ^ 2 = 25.

1. **Lexer**: Convierte la cadena "(2 + 3) ^ 2" en una lista de tokens (LPAREN("("), NUMBER(2), PLUS(+), NUMBER(3), RPAREN(")"), CARET(^), NUMBER(2)).

2. **Parser**: A partir de los tokens, construye un árbol AST siguiendo las reglas de precedencia de operadores y agrupación por paréntesis. Siendo este:

```
    ^
   / \
  +   2
 / \
2   3
```

3. **Evaluator**: Recibe el árbol y lo evalúa. Evalúa primero el nodo de suma (2+3=5) ya que tiene prioridad por los paréntesis, y luego eleva ese resultado al cuadrado (5^2=25), devolviendo 25.


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
