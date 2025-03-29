package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Product;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.List;

import es.urjc.chamazon.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

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

    public void save(Product product, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
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

    public List<Product> findByFilters(Long categoryId, Float minPrice, Float maxPrice, Float rating) {
    // Apply filters
    if (categoryId != null) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);
        } else if (rating != null) {
            return productRepository.findByCategoryIdAndRatingGreaterThanEqual(categoryId, rating);
        } else {
            return productRepository.findByCategoryId(categoryId);
        }
    } else if (minPrice != null && maxPrice != null) {
        if (rating != null) {
            // If we need to filter by price range AND rating
            return productRepository.findByPriceBetween(minPrice, maxPrice)
                    .stream()
                    .filter(p -> p.getRating() >= rating)
                    .collect(Collectors.toList());
        } else {
            // Just price range
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
    } else if (rating != null) {
        // Just rating
        return productRepository.findByRatingGreaterThanEqual(rating);
    } else {
        // No filters - return all
        return productRepository.findAll();
    }
}
}