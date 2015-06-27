package utilities.phani.chitcalculator.model;

public class Chit {
    private final long chitValue, dividendAlreadyRecieved;
    private final int months, completedMonths, minBidIncrement;

    public Chit(long chitValue, int months, int completedMonths) {
        this(chitValue, months, completedMonths, 0);
    }

    public Chit(long chitValue, int months, int completedMonths, long dividendAlreadyRecieved) {
        this(chitValue, months, completedMonths, dividendAlreadyRecieved, 100);
    }

    public Chit(long chitValue, int months, int completedMonths, long dividendAlreadyRecieved, int minBidIncrement) {
        this.chitValue=chitValue;
        this.months=months;
        this.completedMonths=completedMonths;
        this.minBidIncrement = minBidIncrement;
        this.dividendAlreadyRecieved = dividendAlreadyRecieved;
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

    public long getDividendAlreadyRecieved() {
        return dividendAlreadyRecieved;
    }

    public long minProfitableAmount() {
        return chitValue - dividendAlreadyRecieved;
    }
}