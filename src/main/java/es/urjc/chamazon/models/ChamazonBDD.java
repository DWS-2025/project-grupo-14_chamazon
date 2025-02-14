package es.urjc.chamazon.models;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChamazonBDD {



    private List<ShopingCar> ListShopingCar;

        public ChamazonBDD() {
            List<ShopingCar> listShopingCar = new ArrayList<>();

                Object product = new Object();
                List<Object> products = new ArrayList<>();
                ShopingCar shopingCar = new ShopingCar(1,products);
                listShopingCar.add(shopingCar);

                this.ListShopingCar = listShopingCar;

        }

        public void addShopingCar(ShopingCar car) {
            this.ListShopingCar.add(car);
        }

        public List<ShopingCar> getListShopingCar() {
            return ListShopingCar;
        }
}
