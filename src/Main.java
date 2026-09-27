import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("1. Running Unit Tests...");
        System.out.println("==========================================");

        try {
            // Запуск всех модульных тестов
            Tests.main(args);
        } catch (Exception e) {
            System.err.println("Tests failed with exception: " + e.getMessage());
            e.printStackTrace();
            return; // Прекращаем выполнение, если тесты не прошли
        }

        System.out.println("\n==========================================");
        System.out.println("2. Running Workload Benchmarks...");
        System.out.println("==========================================");

        try {
            // Запуск бенчмарков и генерация results.csv
            Benchmark.main(args);
        } catch (IOException e) {
            System.err.println("Benchmark failed to save results: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n==========================================");
        System.out.println("All steps finished successfully!");
        System.out.println("==========================================");
    }
}