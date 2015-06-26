package utilities.phani.chitcalculator.model;

public class Chit {
    private final long chitValue;
    private final int months, completedMonths, minBidIncrement;

    public Chit(long chitValue, int months, int completedMonths) {
        this(chitValue, months, completedMonths, 100);
    }

    public Chit(long chitValue, int months, int completedMonths, int minBidIncrement) {
        this.chitValue=chitValue;
        this.months=months;
        this.completedMonths=completedMonths;
        this.minBidIncrement = minBidIncrement;
        if(chitValue%months>0) {
            throw new IllegalArgumentException("Invalid chit details: "+ this.toString());
        }
        if(completedMonths>=months) {
            throw new IllegalArgumentException("Chit already completed.");
        }
    }

    @Override
    public String toString() {
        return String.format("{chitValue: %s, months: %s, completedMonths: %s}", chitValue, months, completedMonths);
    }

    public long monthlyInstallment() {
        return chitValue/months;
    }

    public long getChitValue() {
        return chitValue;
    }

    public int getCompletedMonths() {
        return completedMonths;
    }

    public int getMonths() {
        return months;
    }

    public int getMinBidIncrement() {
        return minBidIncrement;
    }

    public int remainingMonths() {
        return months - completedMonths -1;
    }
}