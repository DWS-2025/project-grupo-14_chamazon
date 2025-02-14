package es.urjc.chamazon.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class ChamazonBDD {

        private List<ShopingCar> ListShopingCar;

        public ChamazonBDD() {
            this.ListShopingCar = new ArrayList<>();
        }

        public void addShopingCar(ShopingCar car) {
            this.ListShopingCar.add(car);
        }

        public List<ShopingCar> getListShopingCar() {
            return ListShopingCar;
        }
}
