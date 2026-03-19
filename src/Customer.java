public record Customer(String name, String email,int loyaltyPoints) {

    public Customer{
        if(name== null || name.isBlank()) throw new IllegalArgumentException("Customer name cannot be empty");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("The email address is invalid " + email);
        if(loyaltyPoints < 0)  throw new IllegalArgumentException("The number of points cannot be negative");
    }

    public String loyaltyLevel(){
        if(loyaltyPoints >=200 ) return "GOLD";
        else if(loyaltyPoints >=100 ) return "SILVER";
        else if (loyaltyPoints >=50 ) return "BRONZE";
        else return "STANDARD";
    }


    public String formatted(){
        return String.format("%s. [%s] (%d points)",name,loyaltyLevel(),loyaltyPoints);
    }
}
