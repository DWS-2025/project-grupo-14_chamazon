package es.urjc.chamazon.services;

import es.urjc.chamazon.dto.*;
import es.urjc.chamazon.models.Category;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.Product;

import es.urjc.chamazon.models.ShoppingCar;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

import es.urjc.chamazon.repositories.ProductRepository;

import javax.security.sasl.SaslServer;
import javax.sql.rowset.serial.SerialBlob;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");
    @Autowired
    private SanitizationService sanitizationService;


    public Collection<ProductDTOExtended> getProducts() { // findAllProducts
        return toDTOsExtended(productRepository.findAll());
    } //findAllProducts

    public Product getEntityId(long id) { // for img creation
        return productRepository.findById(id).orElseThrow();
    }

    public ProductDTOExtended getProduct(long id) {   //findById with DTO
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            return productMapper.toDTOExtended(product);
        } else {
            return null;
        }
    }
    public Collection<ProductDTOExtended> findCategoryFromProduct(long id) {
        return toDTOsExtended(productRepository.findByCategoryId(id));
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
        Product sanitizedProduct = sanitizationService.sanitizePoduct(product);
        productRepository.save(sanitizedProduct);
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
    public ProductDTO update(Product existingProduct, Product newProduct, MultipartFile imageFile) throws IOException, SQLException {
        //Sanitize new product
        Product sanitizedNewProduct = sanitizationService.sanitizePoduct(newProduct);

        // Update existing  product with new values
        existingProduct.setName(sanitizedNewProduct.getName());
        existingProduct.setPrice(sanitizedNewProduct.getPrice());
        existingProduct.setDescription(sanitizedNewProduct.getDescription());
        existingProduct.setRating(sanitizedNewProduct.getRating());


        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getInputStream().readAllBytes();
            Blob blob = new SerialBlob(imageBytes);
            existingProduct.setImageFile(blob);
            existingProduct.setImage("/products/" + existingProduct.getId() + "/image");
        }

        this.save(existingProduct);
        return toDTO(existingProduct);
    }

    public ProductDTOExtended replaceProduct(long id, ProductDTOExtended newProductDTO) {
        //sanitize productDTO
        ProductDTOExtended sanitizedProductReplacedDTO = sanitizationService.sanitizeProductDTO(newProductDTO);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow();


        existingProduct.setName(sanitizedProductReplacedDTO.name());
        existingProduct.setPrice(sanitizedProductReplacedDTO.price());
        existingProduct.setDescription(sanitizedProductReplacedDTO.description());
        existingProduct.setRating(sanitizedProductReplacedDTO.rating());

        if (newProductDTO.categoryDTOList() != null) {
            List<CategoryDTO> categories = sanitizedProductReplacedDTO.categoryDTOList();
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
        //sanitize productDTO
        ProductDTOExtended sanitizedProductDTO = sanitizationService.sanitizeProductDTO(productDTO);

        Product newProduct = toProductFromExtended(sanitizedProductDTO);

        this.save(newProduct);

        if (!imageFile.isEmpty()) {
            try {
                newProduct.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
                newProduct.setImage("/products/" + newProduct.getId() + "/image");
            } catch (IOException e) {
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
        return productRepository.findByFilters(categoryId, minPrice, maxPrice, rating);
    }

    public void attachFileToProduct(Long productId, MultipartFile file) throws IOException {
        System.out.println("Intentando guardar archivo para producto ID: " + productId);
        System.out.println("Nombre original del archivo: " + file.getOriginalFilename());
        System.out.println("Tamaño del archivo: " + file.getSize() + " bytes");


        // Validar extensión
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, DOC, DOCX o TXT");
        }

        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre seguro manteniendo extensión original
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String safeFilename = generateSafeFilename(originalFilename);

        // Guardar archivo en disco
        Path targetLocation = uploadPath.resolve(safeFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Actualizar producto
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setOriginalFileName(originalFilename);
        product.setStoredFileName(safeFilename);
        product.setFilePath("/uploads/" + safeFilename);
    }

    private String generateSafeFilename(String originalFilename) {
        String baseName = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return extension != null ? baseName + "." + extension : baseName;
    }

    public Resource loadFileAsResource(Long productId) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStoredFileName() == null) {
            throw new FileNotFoundException("Este producto no tiene archivo adjunto");
        }

        Path filePath = Paths.get(uploadDir).resolve(product.getStoredFileName()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Archivo no encontrado: " + product.getOriginalFileName());
        }

        return resource;
    }

}