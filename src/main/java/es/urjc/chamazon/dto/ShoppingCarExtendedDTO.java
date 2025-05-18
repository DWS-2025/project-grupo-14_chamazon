package es.urjc.chamazon.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShoppingCarExtendedDTO {
    private Long id;
    private LocalDateTime dateSold;
    private SimpletUserDTO user;
    private List<ProductDTO> productList;


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

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        this.productList = productList;
    }

    //GETS FOR MUSTACHE
    public String getDateSoldString() {
        if (dateSold == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
        return dateSold.format(formatter);
    }

    public int getProductSize(){
        return this.productList.size();
    }


}
