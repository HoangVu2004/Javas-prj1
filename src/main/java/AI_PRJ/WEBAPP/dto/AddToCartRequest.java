package AI_PRJ.WEBAPP.dto;

public class AddToCartRequest {

    private Long productId;
    private int quantity;

    // Getters
    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}