package vMachine_v3.dto;

public class DrinkDto {
    private int id;
    private String Name;
    private int price;
    private int stock;

    public DrinkDto(){}

    public DrinkDto(int id, String name, int price, int stock) {
        this.id = id;
        Name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
