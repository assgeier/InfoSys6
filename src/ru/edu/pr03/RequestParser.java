package ru.edu.pr03;

public class RequestParser {
    public static final String HEADER = "amount;urgent;risk;personal";
    private static final int FIELD_COUNT = 4;

    public ApprovalRequest parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("empty line");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != FIELD_COUNT) {
            throw new IllegalArgumentException("expected " + FIELD_COUNT + " fields, got " + parts.length);
        }
        long amount = parseAmount(parts[0]);
        boolean urgent = parseBoolean(parts[1], "urgent");
        RiskLevel risk = parseRisk(parts[2]);
        boolean personal = parseBoolean(parts[3], "personal");
        return new ApprovalRequest(amount, urgent, risk, personal);
    }

    private long parseAmount(String raw) {
        String value = raw.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("amount is empty");
        }
        long amount;
        try {
            amount = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("amount is not an integer: " + value);
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount is negative: " + value);
        }
        return amount;
    }

    private boolean parseBoolean(String raw, String name) {
        String value = raw.trim();
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException(name + " must be true or false: '" + value + "'");
    }

    private RiskLevel parseRisk(String raw) {
        String value = raw.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("risk is empty");
        }
        try {
            return RiskLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("unknown risk level: " + value);
        }
    }
}
