package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {
    private FileUtil() {
    }

    public static List<String> readLines(String filePath) {
        Path path = Paths.get(filePath);
        ensureFile(path);

        try {
            List<String> lines = Files.readAllLines(path);
            List<String> cleaned = new ArrayList<>();

            for (String line : lines) {
                if (line != null && !line.trim().isEmpty()) {
                    cleaned.add(line.trim());
                }
            }
            return cleaned;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + filePath, e);
        }
    }

    public static void writeLines(String filePath, List<String> lines) {
        Path path = Paths.get(filePath);
        ensureFile(path);

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + filePath, e);
        }
    }

    private static void ensureFile(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to prepare file: " + path, e);
        }
    }
}
