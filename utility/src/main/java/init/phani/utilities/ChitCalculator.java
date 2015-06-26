package init.phani.utilities;

import init.phani.utilities.init.phani.utilities.model.Chit;

public class ChitCalculator {
    private final FDCalculator fdCalculator;
    private final int commissionPercent;
    private final int taxOnCommissionPercent;

    public ChitCalculator(FDCalculator fdCalculator, int commissionPercent, int taxOnCommissionPercent) {
        this.fdCalculator = fdCalculator;
        this.commissionPercent = commissionPercent;
        this.taxOnCommissionPercent = taxOnCommissionPercent;
    }

    public long finalAmount(Chit chit, long bidAmount) {
        long commission = commission(chit);
        if(bidAmount<commission) {
            throw new IllegalArgumentException("Bid amount should be greater than or equal to: " + commission);
        }
        return chit.getChitValue()-bidAmount- taxOnCommission(commission);
    }

    public long profitableBid(Chit chit, double rateOfInterestOnDeposit) {
        long minDepositAmount = fdCalculator.depositAmount(chit.getChitValue(), chit.remainingMonths(), 9);
        long commission = commission(chit);

        long minimumDeductions = chit.getChitValue() - (commission + taxOnCommission(commission));
        if(minDepositAmount > minimumDeductions) {
            throw new RuntimeException("Cannot determine profitable bid.");
        }

        long finalAmountOnProfitableBid = nearestGreaterMultiple(minDepositAmount, chit.getMinBidIncrement());
        return chit.getChitValue()-finalAmountOnProfitableBid-taxOnCommission(commission);
    }

    private long nearestGreaterMultiple(long number, long divisor) {
        if((number % divisor) > 0) {
            return ((number/divisor)+1)*divisor;
        }
        return number;
    }

    public long dividendOnProfitableBid(Chit chit, double rateOfInterest) {
        return dividendOnBid(chit, profitableBid(chit, rateOfInterest));
    }

    public long dividendOnBid(Chit chit, long bidAmount) {
        return (bidAmount-commission(chit))/chit.getMonths();
    }

    private long commission(Chit chit) {
        return chit.getChitValue()*commissionPercent/100;
    }

    private long taxOnCommission(long commission) {
        return commission*taxOnCommissionPercent/100;
    }

    public static void main(String... args) {
        FDCalculator fdCalculator = new FDCalculator();
        ChitCalculator chitCalculator = new ChitCalculator(fdCalculator, 5, 14);
        Chit chit = new Chit(500000, 50, 40);

        long profitableBid = chitCalculator.profitableBid(chit, 9);
        long dividendOnProfitableBid = chitCalculator.dividendOnBid(chit, profitableBid);
        long finalAmount = chitCalculator.finalAmount(chit, profitableBid);
        long maturityAmount = fdCalculator.maturityAmount(finalAmount, chit.remainingMonths(), 9);

        String message = String.format("Profitable bid amount is: Rs %s and dividend is: Rs %s.\nAmount received will be: Rs %s and maturity amount will be: Rs %s", profitableBid, dividendOnProfitableBid, finalAmount, maturityAmount);
        System.out.println(message);
    }
}
