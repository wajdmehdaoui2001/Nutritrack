package  com.machinestalk.loadTest.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "User login request model")
public class LoginRequest {
    @NotBlank
    @Email
    @Schema(description = "User email", example = "user@example.com")
    private String email;
    @NotBlank
    @Schema(description = "User password", example = "password123")
    private String password;

    public String getEmail(){return email;}
    public  void setEmail(String email){
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}