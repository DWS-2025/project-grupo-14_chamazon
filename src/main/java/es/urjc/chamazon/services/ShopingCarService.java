package es.urjc.chamazon.services;

import es.urjc.chamazon.models.ShopingCar;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ShopingCarService {

    private ConcurrentHashMap<Integer, ShopingCar> shopingCars = new ConcurrentHashMap<>();

    public ShopingCar getShopingCarByIdUser(int idUser) {


        return shopingCars.get(idUser);
    }

    public void addShopingCar(ShopingCar shopingCar) {
    }
}
