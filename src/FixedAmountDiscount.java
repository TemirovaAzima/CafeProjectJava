public class FixedAmountDiscount extends Discount{
    private final double amount;

    public FixedAmountDiscount(double amount){
        super(String.format("%.2f PLN", amount));
        if(amount<=0) throw new IllegalArgumentException("The discount amount value is invalid" + amount);
        this.amount = amount;

    }

    @Override
    double apply(double originalPrice) {
        return Math.max(originalPrice-amount,0);
    }

    public double getAmount() {
        return amount;
    }
}
