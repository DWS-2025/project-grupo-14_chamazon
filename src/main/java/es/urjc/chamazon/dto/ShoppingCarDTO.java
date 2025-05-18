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
    private SimpletUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }


    public SimpletUserDTO getUser() {
        return user;
    }

    public void setUser(SimpletUserDTO user) {
        this.user = user;
    }

}
