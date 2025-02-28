package es.urjc.chamazon.models;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingCar {

    private int id;
    private int idUser;
    private List<Integer> products;
    private LocalDateTime dateSold;

    /*Generate a sequence id for the new registers*/
    /*Genera un id secuencial para los nuevos registros*/
    private static int idCounter = 1;

    public ShoppingCar(int idUser, List<Integer> products) {
        this.id = generateNextId();
        this.idUser = idUser;
        this.products = products;
        this.dateSold = null;
    }

    public ShoppingCar() {
        this.id = generateNextId();
        this.idUser = 0;
        this.products = new ArrayList<>();
        this.dateSold = null;
    }

    /*Generate a sequence id for the new registers*/
    /*Genera un id secuencial para los nuevos registros*/
    private static synchronized int generateNextId() {
        return idCounter++;
    }

    /*GETTERS - SETTERS*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }

    //GETS FOR MUSTACHE
    public String getDateSoldString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        return dateSold.format(formatter);
    }

    public int getProductSize(){
        return this.products.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCar that = (ShoppingCar) o;
        return id == that.id && idUser == that.idUser && Objects.equals(products, that.products) && Objects.equals(dateSold, that.dateSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUser, products, dateSold);
    }

    @Override
    public String toString() {
        return "ShoppingCar{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", products=" + products +
                ", dateSold=" + dateSold +
                '}';
    }
}
