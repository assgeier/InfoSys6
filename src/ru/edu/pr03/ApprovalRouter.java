package ru.edu.pr03;

public class ApprovalRouter {
    public static final long FINANCE_LIMIT = 500_000L;

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel risk, boolean containsPersonalData) {
        validate(amount, risk);
        if (containsPersonalData) {
            return ApprovalRoute.SECURITY_REVIEW;
        }
        if (isHighRisk(risk)) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        }
        if (requiresFinance(amount)) {
            return ApprovalRoute.FINANCE_REVIEW;
        }
        if (urgent) {
            return ApprovalRoute.FAST_TRACK;
        }
        return ApprovalRoute.STANDARD;
    }

    private void validate(long amount, RiskLevel risk) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
        if (risk == null) {
            throw new IllegalArgumentException("risk must not be null");
        }
    }

    private boolean isHighRisk(RiskLevel risk) {
        return risk == RiskLevel.HIGH;
    }

    private boolean requiresFinance(long amount) {
        return amount >= FINANCE_LIMIT;
    }
}
