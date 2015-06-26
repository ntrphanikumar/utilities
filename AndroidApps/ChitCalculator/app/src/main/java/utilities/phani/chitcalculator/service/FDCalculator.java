package utilities.phani.chitcalculator.service;

public class FDCalculator {
    public long maturityAmount(long deposit, int termInMonths, double rateOfInterest) {
        double interest = (deposit * termInMonths * rateOfInterest)/ (100 * 12);
        return deposit + Math.round(interest);
    }

    public long depositAmount(long maturityAmount, int termInMonths, double rateOfInterest) {
        double principal = maturityAmount / (1 + (termInMonths * rateOfInterest) / (100 * 12));
        return Math.round(principal);
    }
}
