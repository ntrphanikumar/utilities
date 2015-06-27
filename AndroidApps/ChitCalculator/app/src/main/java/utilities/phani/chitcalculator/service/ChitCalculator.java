package utilities.phani.chitcalculator.service;

import utilities.phani.chitcalculator.model.Chit;

public class ChitCalculator {
    private final FDCalculator fdCalculator;
    private final int commissionPercent;
    private final int taxOnCommissionPercent;

    public ChitCalculator(FDCalculator fdCalculator, int commissionPercent, int taxOnCommissionPercent) {
        this.fdCalculator = fdCalculator;
        this.commissionPercent = commissionPercent;
        this.taxOnCommissionPercent = taxOnCommissionPercent;
    }

    public long profitableBid(Chit chit, double rateOfInterestOnDeposit) {
        long minDepositAmount = fdCalculator.depositAmount(chit.minProfitableAmount(), chit.remainingMonths(), rateOfInterestOnDeposit);
        long finalAmountOnProfitableBid = nearestGreaterMultiple(minDepositAmount, chit.getMinBidIncrement());
        long profitableBid = chit.getChitValue() - (finalAmountOnProfitableBid + taxOnCommission(chit));
        if(profitableBid<minBid(chit)) {
            throw new ProfitableBidNotFoundException();
        }
        return profitableBid;
    }


    public long minBid(Chit chit) {
        return commission(chit);
    }

    public String bidSummary(Chit chit, double rateOfInterest) {
        return bidSummary(chit, rateOfInterest, profitableBid(chit, rateOfInterest));
    }

    public String bidSummary(Chit chit, double rateOfInterest, long bid) {
        long dividendOnProfitableBid = dividendOnBid(chit, bid);
        long finalAmount = finalAmount(chit, bid);
        long maturityAmount = fdCalculator.maturityAmount(finalAmount, chit.remainingMonths(), rateOfInterest);
        return String.format("Max suggested bid: Rs %s.\nDividend: Rs %s.\nAmount: Rs %s.\nFDMaturity amount: Rs %s.", bid, dividendOnProfitableBid, finalAmount, maturityAmount);
    }

    private long finalAmount(Chit chit, long bidAmount) {
        long commission = commission(chit);
        if(bidAmount<commission) {
            throw new IllegalArgumentException("Bid amount should be greater than or equal to: " + commission);
        }
        return chit.getChitValue()-bidAmount- taxOnCommission(chit);
    }

    private long nearestGreaterMultiple(long number, long divisor) {
        if((number % divisor) > 0) {
            return ((number/divisor)+1)*divisor;
        }
        return number;
    }

    private long dividendOnBid(Chit chit, long bidAmount) {
        return (bidAmount-commission(chit))/chit.getMonths();
    }

    private long minDeductions(Chit chit) {
        return commission(chit) + taxOnCommission(chit);
    }

    private long commission(Chit chit) {
        return chit.getChitValue()*commissionPercent/100;
    }

    private long taxOnCommission(Chit chit) {
        return commission(chit)*taxOnCommissionPercent/100;
    }

    public static void main(String... args) {
        ChitCalculator chitCalculator = new ChitCalculator(new FDCalculator(), 5, 14);
        Chit chit = new Chit(500000, 50, 35);
        System.out.println(chitCalculator.bidSummary(chit, 9));
    }
}
