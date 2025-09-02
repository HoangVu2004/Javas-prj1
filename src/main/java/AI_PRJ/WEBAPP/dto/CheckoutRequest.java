package AI_PRJ.WEBAPP.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String name;
    private String address;
    private String phone;
    private String paymentMethod;
}