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
    private static Double lastResult = null;

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
            if (lastResult != null && line.matches("^[+\\-*/^].*")) {
                line = lastResult + " " + line;
            }

            if (lastResult != null) {
                line = line.replaceAll("(?i)\\bAns\\b", lastResult.toString());
            }
            if (line.isBlank()) continue;

            try {
                List<Token> tokens = new Lexer(line).lex();
                Expr ast = new Parser(tokens).parse();
                result = Evaluator.eval(ast);
                System.out.println(result);
                lastResult = result;
                writeFile("data.txt", line + " = "+ result);
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error inesperado: " + ex);
            }
        }
        System.out.println("Adiós!");
        sc.close();
    }

    public static void clearConsole() {
        try {

            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            
            for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
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
