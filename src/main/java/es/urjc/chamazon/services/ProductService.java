package es.urjc.chamazon.services;

import es.urjc.chamazon.models.Product;
import es.urjc.chamazon.dto.ProductDTO;
import es.urjc.chamazon.dto.ProductMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import es.urjc.chamazon.repositories.ProductRepository;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;


    public Collection<ProductDTO> getProducts() { // findAllProducts
        return toDTOs(productRepository.findAll());
    } //findAllProducts

    public Product getEntityId(long id) { // for img creation
        return productRepository.findById(id).orElseThrow();
    }

    /*public Page<ProductDTO> getProducts(Pageable pageable) { // findAllProducts with pagination
        return productRepository.findAll();
    } //findAllProducts with pagination
    */

    private Collection<ProductDTO> toDTOs(Collection<Product> products) {
        return productMapper.toDTOs(products);
    }

    public Optional<Product> findById(long id) {   //Return as entity not DTO like before (made for blob img)
        return productRepository.findById(id);
    }

    public ProductDTO getProduct(long id) {   //findById with DTO
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return toDTO(product.get());
        } else {
            return null;
        }
    }

    private ProductDTO toDTO(Product product) {
        return productMapper.toDTO(product);
    }

    private Collection<Product> toProducts(Collection<ProductDTO> productDTOs) {
        return productMapper.toProduct(productDTOs);
    }

    private Product toProduct(ProductDTO productDTO) {
        return productMapper.toProduct(productDTO);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Optional<Product> findByRating(Float rating) {
        return productRepository.findByRating(rating);
    }


    //for bbdd
    void save(Product product) {
        productRepository.save(product);
    }


    // for updating product which has been saved before in CONTROLLER
    public ProductDTO update(Product existingProduct, Product newProduct, MultipartFile imageFile) throws IOException {
        // Update existing  product with new values
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setRating(newProduct.getRating());

        if (!imageFile.isEmpty()) {
            existingProduct.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }

        this.save(existingProduct);
        return toDTO(existingProduct);
    }

    // for updating in REST CONTROLLER
    public ProductDTO replaceProduct(long id, ProductDTO updatedProductDTO) throws SQLException {
        Product oldProduct = getEntityId(id);
        Product updatedProduct = toProduct(updatedProductDTO);
        updatedProduct.setId(id);

        if (oldProduct.getImage() != null) {
            // Set the image in the updated product
            updatedProduct.setImageFile(BlobProxy.generateProxy(oldProduct.getImageFile().getBinaryStream(),
            oldProduct.getImageFile().length()));
            updatedProduct.setImage(oldProduct.getImage());
        }

        this.save(updatedProduct);
        return toDTO(updatedProduct);
    }


 //for adding new products
    public ProductDTO save(ProductDTO productDTO, MultipartFile imageFile){
        Product newProduct = toProduct(productDTO);

        if (!imageFile.isEmpty()) {
            try {
                newProduct.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            } catch (IOException e) {
                this.save(newProduct);
                throw new RuntimeException(e);
            }
        }
        this.save(newProduct);
        return toDTO(newProduct);
    }


    public void createProductImage (long id, URI location, InputStream imageFile, long size){
        Product product = getEntityId(id);

        product.setImage(location.toString());
        product.setImageFile(BlobProxy.generateProxy(imageFile, size));
        save(product);
    }

    public Resource getProductImage(long id) throws SQLException{
        Product product = getEntityId(id);
        if(product.getImage()!= null){
            return new InputStreamResource(product.getImageFile().getBinaryStream());
		} else {
			throw new NoSuchElementException();
        }
    }

    public void replaceProductImage(long id, InputStream inputStream, long size) {

		Product product = getEntityId(id);

		if(product.getImage() == null){
			throw new NoSuchElementException();
		}

		product.setImageFile(BlobProxy.generateProxy(inputStream, size));

		save(product);
	}

	public void deletePostImage(long id) {

		Product product = getEntityId(id);

		if(product.getImage() == null){
			throw new NoSuchElementException();
		}

		product.setImageFile(null);
		product.setImage(null);

		save(product);
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
                return productRepository.findByPriceBetween(minPrice, maxPrice).stream().filter(p -> p.getRating() >= rating).collect(Collectors.toList());
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