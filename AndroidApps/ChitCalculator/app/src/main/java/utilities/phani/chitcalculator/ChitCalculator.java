package utilities.phani.chitcalculator;

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

    public String bidSummary(Chit chit, double rateOfInterest) {
        long profitableBid = profitableBid(chit, rateOfInterest);
        long dividendOnProfitableBid = dividendOnBid(chit, profitableBid);
        long finalAmount = finalAmount(chit, profitableBid);
        long maturityAmount = fdCalculator.maturityAmount(finalAmount, chit.remainingMonths(), rateOfInterest);

        return String.format("Profitable bid: Rs %s.\nDividend: Rs %s.\nAmount: Rs %s.\nFDMaturity amount: Rs %s.", profitableBid, dividendOnProfitableBid, finalAmount, maturityAmount);
    }

    private long commission(Chit chit) {
        return chit.getChitValue()*commissionPercent/100;
    }

    private long taxOnCommission(long commission) {
        return commission*taxOnCommissionPercent/100;
    }

    public static void main(String... args) {
        ChitCalculator chitCalculator = new ChitCalculator(new FDCalculator(), 5, 14);
        Chit chit = new Chit(500000, 50, 40);
        System.out.println(chitCalculator.bidSummary(chit, 9));
    }
}
