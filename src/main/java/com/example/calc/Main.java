package com.example.calc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String HELP = """
        === Calc21 ===
        Operaciones: +  -  *  /  ^  %   Funciones: sin(x), cos(x), tan(x) Valores: y (resultado anterior)
        Precedencia: ^ (derecha), luego * /, luego + -
        Ejemplos:
          1 + 2*3
          (1 + 2) * 3 ^ 2
          -2 ^ 3
          sin(3.14159/2) + cos(0)
          y será igual al último resultado
        Escribe 'exit' para salir.
        Escribe 'clear' para limpiar.
        """;

    public static Double result = null;

    public static void main(String[] args) {
        System.out.println(HELP);
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line == null) break;
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) break;
            if (line.equalsIgnoreCase("clear")) {
                clearConsole();
                continue;
            }
            if (line.isBlank()) continue;

            try {
                List<Token> tokens = new Lexer(line).lex();
                Expr ast = new Parser(tokens).parse();
                result = Evaluator.eval(ast);
                System.out.println(result);
                writeFile("data.txt", line + " = "+ result);
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error inesperado: " + ex);
            }
        }
        System.out.println("Adiós!");
    }

    public static void clearConsole() {
        try {
            if (System.console() == null) {
                for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
            } else {
                if (System.getProperty("os.name").contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo limpiar la consola");
        }
    }

    public static void writeFile(String fileName, String line) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter("data/"+fileName, true);
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) writer.close();
        }
    }
}
