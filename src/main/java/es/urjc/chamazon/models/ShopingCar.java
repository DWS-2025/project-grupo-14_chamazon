package es.urjc.chamazon.models;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ShopingCar {

    private int id;
    private int idUser;
    private List<Object> products;
    private DateTimeFormatter dateSold;

    /*Generate a sequence id for the new registers*/
    /*Genera un id secuencial para los nuevos registros*/
    private static int idCounter = 1;

    public ShopingCar(int idUser, List<Object> products) {
        this.id = idCounter;
        this.idUser = idUser;
        this.products = products;
        this.dateSold = null;

        idCounter++;
    }

    public ShopingCar() {
        this.id = idCounter;
        this.idUser = 0;
        this.products = null;
        this.dateSold = null;

        idCounter++;
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

    public List<Object> getProducts() {
        return products;
    }

    public void setProducts(List<Object> products) {
        this.products = products;
    }

    public DateTimeFormatter getDateSold() {
        return dateSold;
    }

    public void setDateSold(DateTimeFormatter dateSold) {
        this.dateSold = dateSold;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShopingCar that = (ShopingCar) o;
        return id == that.id && idUser == that.idUser && Objects.equals(products, that.products) && Objects.equals(dateSold, that.dateSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUser, products, dateSold);
    }

    @Override
    public String toString() {
        return "ShopingCar{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", products=" + products +
                ", dateSold=" + dateSold +
                '}';
    }
}
