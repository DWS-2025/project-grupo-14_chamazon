package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;

import es.urjc.chamazon.models.ShoppingCar;
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
import java.util.*;
import java.util.stream.Collectors;

import es.urjc.chamazon.repositories.ProductRepository;

import javax.security.sasl.SaslServer;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    /*
        public Page<ProductDTOExtended> getProducts(Pageable pageable) { // findAllProducts with pagination
            return productRepository.findAll(pageable).map(productMapper::toDTOExtended);
        } //findAllProducts with pagination
     */

        public Collection<ProductDTOExtended> getProducts() { // findAllProducts
            return toDTOsExtended(productRepository.findAll());
        } //findAllProducts

    public Product getEntityId(long id) { // for img creation
        return productRepository.findById(id).orElseThrow();
    }

    public ProductDTOExtended getProduct(long id) {   //findById with DTO
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return toDtoExtended(product.get());
        } else {
            return null;
        }
    }

    /*public Page<ProductDTO> getProducts(Pageable pageable) { // findAllProducts with pagination
        return productRepository.findAll();
    } //findAllProducts with pagination
    */

    private Collection<ProductDTO> toDTOs(Collection<Product> products) {
        return productMapper.toDTOs(products);
    }
    private Collection<ProductDTOExtended> toDTOsExtended(Collection<Product> products){
        return productMapper.toDTOsExtended(products);
    }
    private ProductDTOExtended toDtoExtended(Product product) {
        return productMapper.toDTOExtended(product);
    }



    public Optional<Product> findById(long id) {   //Return as entity not DTO like before (made for blob img)
        return productRepository.findById(id);
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
    private Product toProductFromExtended(ProductDTOExtended productDTO) {
        return productMapper.toProductFromExtended(productDTO);
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


    public ProductDTOExtended createProduct (ProductDTOExtended productDTO ) {
        Product newProduct = toProductFromExtended(productDTO);
        this.save(newProduct);
        if (productDTO.categoryDTOList() != null) {
            for (CategoryDTO categoryDTO : productDTO.categoryDTOList()) {
                categoryService.addProductToCategory(categoryDTO.id(), newProduct.getId());
            }
        }
        return toDtoExtended(newProduct);
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

    public ProductDTOExtended replaceProduct(long id, ProductDTOExtended newProductDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow();


        existingProduct.setName(newProductDTO.name());
        existingProduct.setPrice(newProductDTO.price());
        existingProduct.setDescription(newProductDTO.description());
        existingProduct.setRating(newProductDTO.rating());

        if (newProductDTO.categoryDTOList() != null) {
            List<CategoryDTO> categories = newProductDTO.categoryDTOList();
            existingProduct.setCategoryList(categoryMapper.toCategories(categories));
        }

        Product savedProduct = productRepository.save(existingProduct);
        return productMapper.toDTOExtended(savedProduct);
    }


    public ProductDTO deleteProduct(long id) {
        Product product = getEntityId(id);
        this.deleteById(id);
        return toDTO(product);
    }

 //for adding new products
    public ProductDTO save(ProductDTOExtended productDTO, MultipartFile imageFile){
        Product newProduct = toProductFromExtended(productDTO);

        if (!imageFile.isEmpty()) {
            try {
                newProduct.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            } catch (IOException e) {
                this.save(newProduct);
                throw new RuntimeException(e);
            }
        }
        this.save(newProduct);
        if (productDTO.categoryDTOList() != null) {
            for (CategoryDTO categoryDTO : productDTO.categoryDTOList()) {
                categoryService.addProductToCategory(categoryDTO.id(), newProduct.getId());
            }
        }
        return toDTO(newProduct);
    }



    public void createProductImage (long id, URI location, InputStream imageFile, long size){
        Product product = productRepository.findById(id).orElseThrow();

        product.setImage(location.toString());

        product.setImageFile(BlobProxy.generateProxy(imageFile, size));
        productRepository.save(product);
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

		Product product = productRepository.findById(id).orElseThrow();

		product.setImageFile(BlobProxy.generateProxy(inputStream, size));

		productRepository.save(product);
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
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            for (Category category : product.getCategoryList()) {
                category.getProductList().remove(product);
            }

            for (ShoppingCar shoppingCar : product.getShoppingCarList()) {
                shoppingCar.getProductList().remove(product);
            }


            //commentService.deleteById(product.getId());

            product.getCategoryList().clear();
            product.getShoppingCarList().clear();


            productRepository.delete(product);
        }
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public List<Product> findByFilters(Long categoryId, Float minPrice, Float maxPrice, Float rating) {
        // Filter by category + price range + rating
        if (categoryId != null && minPrice != null && maxPrice != null && rating != null) {
            return productRepository.findByCategoryIdAndPriceBetweenAndRatingGreaterThanEqual(
                categoryId, minPrice, maxPrice, rating);
        }
        
        // Filter by category + price range
        if (categoryId != null && minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);
        }
        
        // Filter by category + rating
        if (categoryId != null && rating != null) {
            return productRepository.findByCategoryIdAndRatingGreaterThanEqual(categoryId, rating);
        }
        
        // Filter only by category
        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        }
        
        // Filter by price range + rating
        if (minPrice != null && maxPrice != null && rating != null) {
            return productRepository.findByPriceBetweenAndRatingGreaterThanEqual(minPrice, maxPrice, rating);
        }
        
        // Filter only by price range
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }
        
        // Filter by minimum price + rating
        if (minPrice != null && rating != null) {
            return productRepository.findByPriceGreaterThanEqualAndRatingGreaterThanEqual(minPrice, rating);
        }
        
        // Filter by maximum price + rating
        if (maxPrice != null && rating != null) {
            return productRepository.findByPriceLessThanEqualAndRatingGreaterThanEqual(maxPrice, rating);
        }
        
        // Filter only by minimum price
        if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqual(minPrice);
        }
        
        // Filter only by maximum price
        if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice);
        }
        
        // Filter only by rating
        if (rating != null) {
            return productRepository.findByRatingGreaterThanEqual(rating);
        }
        
        // No filters applied - return all products
        return productRepository.findAll();
    }

}