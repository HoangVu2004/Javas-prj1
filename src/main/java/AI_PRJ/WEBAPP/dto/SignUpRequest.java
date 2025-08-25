package AI_PRJ.WEBAPP.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
}