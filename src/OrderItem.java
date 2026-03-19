public record OrderItem(Product product, int quantity) {

    public OrderItem{
        if(product== null) throw new IllegalArgumentException("product cannot be empty");
        if(quantity <1) throw new IllegalArgumentException("Quantity must be at least 1. Received: "+ quantity);
    }

    public double totalPrice(){
        return product.price() * quantity;
    }

    public String formatted(){
        String left = String.format("%dx %s",quantity,product.name());
        String right = String.format("%.2f PLN",product.price());
        int width = 40;
        int cal = width - (left.length()+ right.length());
        return left + " ".repeat(cal) + right;
    }

}

