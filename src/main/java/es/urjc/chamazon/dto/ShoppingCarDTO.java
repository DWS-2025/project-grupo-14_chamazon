package es.urjc.chamazon.dto;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingCarDTO {

    private Long id;
    private LocalDateTime dateSold;
    private User user;
    private List<Product> productList = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return user.getId();
    }

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCarDTO that = (ShoppingCarDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(dateSold, that.dateSold) && Objects.equals(user, that.user) && Objects.equals(productList, that.productList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateSold, user, productList);
    }



}
