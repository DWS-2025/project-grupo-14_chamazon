package es.urjc.chamazon.services;


import es.urjc.chamazon.models.ChamazonBDD;
import es.urjc.chamazon.models.ShopingCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopingCarService {

    /*Acces to unique chamazon object*/
    /*Accedemos al unico objeto chamazon*/
    private final ChamazonBDD chamazonBDD;

    @Autowired
    public ShopingCarService(ChamazonBDD chamazonBDD) {
        this.chamazonBDD = chamazonBDD;
    }

    public void addShopingCar(ShopingCar shopingCar) {
        chamazonBDD.addShopingCar(shopingCar);
    }

    public List<ShopingCar> getShopingCars() {
        return chamazonBDD.getListShopingCar();
    }
}
