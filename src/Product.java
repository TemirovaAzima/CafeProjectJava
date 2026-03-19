public record Product(String name,int price, String category) {

    public Product {
        if(name.isBlank()) throw new IllegalArgumentException("Product name cannot be empty");
        if(price<0) throw new IllegalArgumentException("Price must be greater than 0: Received:" + price);
        if(category.isBlank()) throw new IllegalArgumentException("Product category cannot be empty");
    }

    public String formatted(){
        return String.format("%s (%s)  - 2%d PLN",name,category,price);
    }
}
