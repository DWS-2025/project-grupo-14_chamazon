package es.urjc.chamazon.dto;

public class CommentDTO {
    private Long id;
    private String comment;
    private int rating;
    private UserDTO user;
    private ProductDTO product;
    private boolean canEditOrDelete; // NUEVO


    public CommentDTO() {}

    public CommentDTO(Long id, String comment, int rating, UserDTO user, ProductDTO product, boolean canEditOrDelete) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.user = user;
        this.product = product;
        this.canEditOrDelete = canEditOrDelete;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
    public boolean isCanEditOrDelete() {
        return canEditOrDelete;
    }
}

