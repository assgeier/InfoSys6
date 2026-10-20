package ru.edu.pr03;

public record ApprovalRequest(long amount, boolean urgent, RiskLevel risk, boolean containsPersonalData) {
}
