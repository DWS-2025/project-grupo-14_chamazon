package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import es.urjc.chamazon.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired ProductRepository productRepository;

    public Collection<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

}