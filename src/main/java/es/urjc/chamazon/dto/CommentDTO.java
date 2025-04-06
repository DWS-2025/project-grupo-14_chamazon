package es.urjc.chamazon.dto;

public class CommentDTO {
    private Long id;
    private String comment;
    private int rating;
    private Long userId;
    private Long productId;

    public CommentDTO() {}

    public CommentDTO(Long id, String comment, int rating, Long userId, Long productId) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.productId = productId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
