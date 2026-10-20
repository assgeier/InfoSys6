package ru.edu.pr03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvRunner {
    public static void main(String[] args) {
        String file = args.length > 0 ? args[0] : "data/sample.csv";
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("ERROR cannot read file: " + file);
            System.exit(1);
            return;
        }
        run(lines);
    }

    static void run(List<String> lines) {
        RequestParser parser = new RequestParser();
        ApprovalRouter router = new ApprovalRouter();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == 0 && line.trim().equalsIgnoreCase(RequestParser.HEADER)) {
                continue;
            }
            int number = i + 1;
            try {
                ApprovalRequest r = parser.parse(line);
                ApprovalRoute route = router.route(r.amount(), r.urgent(), r.risk(), r.containsPersonalData());
                System.out.println("line " + number + ": " + line.trim() + " -> " + route);
            } catch (IllegalArgumentException e) {
                System.out.println("line " + number + ": ERROR " + e.getMessage());
            }
        }
    }
}
