package ru.edu.pr03;

import java.util.List;

public class SelfCheck {
    private static int failed = 0;
    private static int total = 0;

    public static void main(String[] args) {
        try {
            ApprovalRouter router = new ApprovalRouter();
            checkDecisionTable(router);
            checkBoundaries(router);
            checkInvalidArguments(router);
            checkParser();
        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
            failed++;
        }
        System.out.println("checks: " + total + ", failed: " + failed);
        System.out.println(failed == 0 ? "PASS" : "FAIL  complete TODO methods");
        if (failed != 0) {
            System.exit(1);
        }
    }

    private static void checkDecisionTable(ApprovalRouter r) {
        expect("T1", ApprovalRoute.FAST_TRACK, r.route(120_000, true, RiskLevel.LOW, false));
        expect("T2", ApprovalRoute.FINANCE_REVIEW, r.route(500_000, false, RiskLevel.LOW, false));
        expect("T3", ApprovalRoute.DIRECTOR_REVIEW, r.route(10_000, true, RiskLevel.HIGH, false));
        expect("T4", ApprovalRoute.SECURITY_REVIEW, r.route(10_000, true, RiskLevel.LOW, true));
        expect("T5", ApprovalRoute.STANDARD, r.route(100, false, RiskLevel.MEDIUM, false));
        expect("T6", ApprovalRoute.DIRECTOR_REVIEW, r.route(500_000, false, RiskLevel.HIGH, false));
        expect("T7", ApprovalRoute.SECURITY_REVIEW, r.route(900_000, true, RiskLevel.HIGH, true));
        expect("T8", ApprovalRoute.SECURITY_REVIEW, r.route(600_000, false, RiskLevel.LOW, true));
        expect("T9", ApprovalRoute.FAST_TRACK, r.route(50_000, true, RiskLevel.MEDIUM, false));
        expect("T10", ApprovalRoute.STANDARD, r.route(0, false, RiskLevel.LOW, false));
    }

    private static void checkBoundaries(ApprovalRouter r) {
        expect("B1 499999 not urgent", ApprovalRoute.STANDARD, r.route(499_999, false, RiskLevel.LOW, false));
        expect("B2 499999 urgent", ApprovalRoute.FAST_TRACK, r.route(499_999, true, RiskLevel.LOW, false));
        expect("B3 500000 urgent", ApprovalRoute.FINANCE_REVIEW, r.route(500_000, true, RiskLevel.LOW, false));
        expect("B4 500001 urgent", ApprovalRoute.FINANCE_REVIEW, r.route(500_001, true, RiskLevel.LOW, false));
        expect("B5 limit constant", ApprovalRoute.FINANCE_REVIEW,
                r.route(ApprovalRouter.FINANCE_LIMIT, false, RiskLevel.MEDIUM, false));
        expect("B6 amount 0", ApprovalRoute.STANDARD, r.route(0, false, RiskLevel.LOW, false));
    }

    private static void checkInvalidArguments(ApprovalRouter r) {
        expectError("E1 negative amount", () -> r.route(-1, false, RiskLevel.LOW, false));
        expectError("E2 null risk", () -> r.route(1, false, null, false));
    }

    private static void checkParser() {
        RequestParser p = new RequestParser();
        ApprovalRequest ok = p.parse("120000;true;LOW;false");
        expect("P1 amount", 120_000L, ok.amount());
        expect("P2 risk", RiskLevel.LOW, ok.risk());
        expect("P3 lower case risk", RiskLevel.HIGH, p.parse("1;false;high;false").risk());
        List<String> bad = List.of("", ";true;LOW;false", "abc;true;LOW;false", "-5;true;LOW;false",
                "100;yes;LOW;false", "100;true;LOW", "100;true;LOW;false;1", "100;true;EXTREME;false",
                "100;true;;false", "1.5;true;LOW;false");
        for (int i = 0; i < bad.size(); i++) {
            String line = bad.get(i);
            expectError("P" + (4 + i) + " invalid '" + line + "'", () -> p.parse(line));
        }
        expect("P14 repeat is stable", p.parse("500000;false;LOW;false"), p.parse("500000;false;LOW;false"));
    }

    private static void expect(String name, Object expected, Object actual) {
        total++;
        if (!expected.equals(actual)) {
            failed++;
            System.out.println("FAIL " + name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expectError(String name, Runnable action) {
        total++;
        try {
            action.run();
            failed++;
            System.out.println("FAIL " + name + ": no exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
