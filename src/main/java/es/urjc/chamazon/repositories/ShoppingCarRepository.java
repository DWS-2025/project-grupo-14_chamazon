package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, Long> {

    //CRUD
    Optional<ShoppingCar> findById(Long id);
    ShoppingCar deleteById(long id);


    //JPQL//

    //GetActualShoppingCarByIdUser
    ShoppingCar findByUser_IdAndDateSoldNull(Long user_id);

    //GetShoppingCarListByIdUser
    Optional<List<ShoppingCar>> findByUser_Id(Long idUser);
    Boolean existsByUser_Id(Long idUser);

    //getActualShoppingCarByIdUser
    Optional<ShoppingCar> findByUser_IdAndDateSoldNotNull(Long user_id);
    Boolean existsByUser_IdAndDateSoldNull(long idUser);

    //To check
    //Long findByUser_IdAndDateSoldNotNull(long idUser);





}
