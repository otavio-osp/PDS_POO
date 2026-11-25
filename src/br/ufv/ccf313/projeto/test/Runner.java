package br.ufv.ccf313.projeto.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Runner {

    public static void main(String[] args) {
        List<Class<?>> testClasses = List.of(
            GerenciadorRankingTest.class,
            GerenciadorCombateTest.class,
            MapaTest.class,
            JogadorTest.class
        );

        int totalPassed = 0;
        int totalFailed = 0;

        System.out.println("=========================================");
        System.out.println("          EXECUTANDO TESTES JUNIT 5      ");
        System.out.println("=========================================");

        for (Class<?> testClass : testClasses) {
            System.out.println("\nClasse de Teste: " + testClass.getSimpleName());
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();

            for (Method m : testClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(BeforeEach.class)) {
                    beforeMethods.add(m);
                }
                if (m.isAnnotationPresent(Test.class)) {
                    testMethods.add(m);
                }
            }

            for (Method testMethod : testMethods) {
                try {
                    Object testInstance = testClass.getDeclaredConstructor().newInstance();
                    for (Method b : beforeMethods) {
                        b.invoke(testInstance);
                    }
                    testMethod.invoke(testInstance);
                    System.out.println("  [PASS] " + testMethod.getName());
                    totalPassed++;
                } catch (Throwable t) {
                    System.err.println("  [FAIL] " + testMethod.getName() + " -> " + (t.getCause() != null ? t.getCause() : t));
                    totalFailed++;
                }
            }
        }

        System.out.println("\n=========================================");
        System.out.println("RESULTADO: " + totalPassed + " passaram, " + totalFailed + " falharam.");
        System.out.println("=========================================");

        if (totalFailed > 0) {
            System.exit(1);
        }
    }
}
