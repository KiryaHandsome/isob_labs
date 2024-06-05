package by.bsuir;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaObfuscator {

    private static final Map<String, String> variables = new HashMap<>();
    private static final Random random = new Random();
    private static final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    public static void main(String[] args) throws IOException {
        JavaParser javaParser = new JavaParser();
        File input = new File("/media/kirya/linux/projects/bsuir/isob_labs/lab6_obfuscator/Input.java");

        String code = renameVariables(input, javaParser);
        code = addDeadCode(code);
//        code = reformatCode(code);

        Files.write(Paths.get("/media/kirya/linux/projects/bsuir/isob_labs/lab6_obfuscator/Output.java"),
                code.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String renameVariables(File input, JavaParser javaParser) throws FileNotFoundException {
        ParseResult<CompilationUnit> result = javaParser.parse(input);
        CompilationUnit compilationUnit = result.getResult()
                .orElseThrow();
        compilationUnit.accept(new VariableDeclarationVisitor(), null);
        compilationUnit.accept(new VariableUsageVisitor(), null);
        return compilationUnit.toString();
    }

    private static class VariableDeclarationVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(VariableDeclarator vd, Void arg) {
            super.visit(vd, arg);
            System.out.println("Visit variableDecl: " + vd.getNameAsString());
            String oldName = vd.getNameAsString();
            String newName = generateRandomVariableName();
            vd.setName(newName);
            variables.put(oldName, newName);
        }
    }

    private static class VariableUsageVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(final NameExpr nameExpr, final Void arg) {
            super.visit(nameExpr, arg);
            System.out.println("Visit nameExpr: " + nameExpr.getNameAsString());

            Optional.ofNullable(variables.get(nameExpr.getNameAsString()))
                    .ifPresent(nameExpr::setName);
        }
    }

    private static String reformatCode(String code) {
        StringBuilder obfuscatedCode = new StringBuilder();

        String[] lines = code.split(System.lineSeparator());

        Random random = new Random();
        for (String line : lines) {
            String[] tokens = line.split("\\s+"); // Разбиваем строку на токены по пробелу

            // Добавляем случайное количество пробелов перед строкой
            int spacesBefore = random.nextInt(5);
            for (int i = 0; i < spacesBefore; i++) {
                obfuscatedCode.append(" ");
            }

            for (String token : tokens) {
                obfuscatedCode.append(String.valueOf(" \n\t".charAt(random.nextInt(3))).repeat(10));
                obfuscatedCode.append(token);
            }

            int emptyLinesAfter = random.nextInt(3);
            for (int i = 0; i < emptyLinesAfter; i++) {
                obfuscatedCode.append("\n");
            }
        }

        return obfuscatedCode.toString();
    }

    private static String addDeadCode(String inputCode) {
        StringBuilder stringBuilder = new StringBuilder();
        Pattern ifPattern = Pattern.compile("^\\s*if\\s*\\(.*\\)\\s*\\{\\s*$");

        String[] lines = inputCode.split("\\r?\\n");
        for (String line : lines) {
            stringBuilder.append(line).append("\n");

            Matcher matcher = ifPattern.matcher(line.trim());
            if (matcher.matches()) {
                int randomValue = random.nextInt(10);
                if (randomValue < 3) {
                    stringBuilder.append("String ").append(generateRandomVariableName())
                            .append(" = \"Critical string that can change all program!\";\n");
                } else if (randomValue < 6) {
                    stringBuilder.append("int ").append(generateRandomVariableName())
                            .append(" = ").append(random.nextInt(4123)).append(" - 423 + 532 * 53;\n");
                } else {
                    stringBuilder.append("double ").append(generateRandomVariableName())
                            .append(" = 645 / 1123 * 643 / 43532;\n");
                }
            }
        }

        return stringBuilder.toString();
    }

    private static String generateRandomVariableName() {
        int length = 20;
        StringBuilder sb = new StringBuilder();
        sb.append(alphabet.charAt(random.nextInt(alphabet.length())));

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}