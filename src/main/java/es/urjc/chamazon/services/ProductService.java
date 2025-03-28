package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Collection;



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

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Optional<Product> findByRating(Float rating) {
        return productRepository.findByRating(rating);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void save(Product product, MultipartFile imageFile) throws IOException{
		if(!imageFile.isEmpty()) {
			product.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		}
		this.save(product);
	}

    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

}