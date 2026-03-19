import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int id;
    private final OrderItem[] items;
    private final Customer customer;
    private final LocalDateTime createAt;
    private Discount discount;

    private Order(int id, OrderItem[] items, Customer customer, Discount discount){
        this.id = id;
        OrderItem[] copy = new OrderItem[items.length];

        for(int i = 0; i < items.length; i++){
            copy[i] = items[i];
        }
        this.items = copy;
        this.customer = customer;
        this.discount = discount;
        this.createAt = LocalDateTime.now();
    }

    public int getId(){
        return id;
    }
    public Customer getCustomer(){
        return customer;
    }
    public Discount getDiscount(){
        return discount;
    }

    public void setDiscount(Discount discount){
        this.discount = discount;
    }

    public OrderItem[] getItems(){
        OrderItem[] copy =  new OrderItem[items.length];
        for (int i = 0; i < items.length; i++)
            copy[i] = items[i];
        return copy;
    }

    public int getLineCount(){
        return items.length;
    }
    public int getItemCount(){
        int count = 0;
        for( int i = 0; i < items.length; i++){
            count+= items[i].quantity();
        }
        return count;
    }

    public double calculateSubtotal(){
        double sum =0;
        for(int i = 0; i < items.length; i++){
            sum+= items[i].totalPrice();
        }
        return sum;
    }

    public double calculateTotal(){
        double subtotal = calculateSubtotal();
        if(discount != null){
            return discount.apply(subtotal);
        }
        return subtotal;
    }
//TODO
    public String toString(){
        return String.format("%d, %s, %d, %f",id,customer.name(),getItemCount(),calculateTotal());
    }
    public class Receipt{
        private final String cashierName ;
        private final String CAFE_NAME = "CAFE \"UNDER JAVA\"";
        private final int WIDTH = 42;

        public Receipt(String cashierName){
            if(cashierName ==null || cashierName.isBlank())
                throw new IllegalArgumentException("Cashier name cannot be empty");
            this.cashierName = cashierName;
        }

        private String center(String text){
            int diff = (WIDTH - text.length())/2;
            int abs = Math.max(diff,0);
            return (" ".repeat(abs) + text + " ".repeat(abs));
        }

        private String formatLine(String label, double amount){
            String left = " ".repeat(4) + label;
            String right = String.format("%.2fPLN",amount);
            int spaces = WIDTH - (left.length()+ right.length());
            int spac = Math.max(spaces,1);
            return left + " ".repeat(spac) + right;
        }

        private String formatNegLine(String label, double amount){
            String left = " ".repeat(4) + label;
            String right = String.format("-%.2fPLN",amount);
            int spaces = WIDTH - (left.length()+ right.length());
            int spac = Math.max(spaces,1);
            return left + " ".repeat(spac) + right;
        }

        private String generate(){
            StringBuilder receipt = new StringBuilder() ;
            String sep1 = "=".repeat(WIDTH);
            String  sep2 = "-".repeat(WIDTH);

            receipt.append(sep1);
            receipt.append(center(CAFE_NAME)).append('\n');
            receipt.append(sep2);
            DateTimeFormatter date =DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            receipt.append("Date: ").append(date.format(createAt)).append('\n');
            receipt.append("Cashier: ").append(cashierName).append('\n');
            receipt.append(String.format("Order: [#%d]",id)).append("\n");
            receipt.append(String.format("Customer: %s [%s]\n",customer.name(),customer.loyaltyLevel()));
            receipt.append("\n");

            for(int i = 0; i < items.length; i++) {
                receipt.append(items[i].formatted()).append("\n");
            }
            receipt.append("\n");
            receipt.append(sep2);

            double subtotal = calculateSubtotal();
            receipt.append(formatLine("Subtotal: %d ",subtotal)).append("\n");

            if(discount!=null){
                double savings = discount.savings(subtotal);
                receipt.append(formatNegLine("Discount: " + discount.getDescription(),savings)).append("\n");
            }

            receipt.append(sep2);
            receipt.append(formatLine("TOTAL VALUE: ",calculateTotal())).append("\n");
            receipt.append(sep1);
            receipt.append(center("Thank you")).append('\n');
            receipt.append(sep1);
            return receipt.toString();
        }

    }

    private class Builder{

        private final int INITIAL_CAPACITY = 4;
        private final int id;
        private int  size;
        private final Customer  customer;
        private  OrderItem[] items;
        private  Discount discount;

        public Builder(int id, Customer customer){
            if(id<0)
                throw new IllegalArgumentException("The order ID must be greater than zero");
            if(customer == null)
                throw new IllegalArgumentException("The customer cannot be equal null");
            this.id = id;
            this.customer = customer;
            items = new OrderItem[INITIAL_CAPACITY];
            size = 0;
        }

        private void grow(){
            OrderItem[] arr = new OrderItem[items.length*2];
            for(int i = 0; i < items.length; i++){
                arr[i] = items[i];
            }
            items = arr;
        }

        private Builder addItem(Product product, int quantity){
            if(size == items.length) grow();
            OrderItem item = new OrderItem(product,quantity);
            items[size] = item;
            size++;
            return this;
        }

        Builder addItem(Product product){
           return addItem(product,1);
        }

        Builder withDiscount(Discount discount){
            this.discount = discount;
            return this;
        }

        Order build(){
            if(size<0) throw new IllegalStateException("The order must contain at least one item");
            OrderItem[] items = new OrderItem[size];
            for(int i = 0; i <items.length; i++)
                items[i] = this.items[i];
            return new Order(id, items, customer,discount);
        }
    }
}
