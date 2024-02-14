package checkoutapi.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import checkoutapi.tools.ConstantsApp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SignUpRequest {
	@Size(max = 40)
    private String firstname;

    @Size(max = 60)
    private String lastname;

//--FIELDS BASE USER
	// @NotBlank
	@Size(min = 4, max = 110)
	private String fullname;

	// @NotBlank
	@Size(min = ConstantsApp.MIN_SIZE, max = ConstantsApp.USER_USERNAME_SIZE)
	private String username;
	
	// @NotBlank
	@Size(min = ConstantsApp.MIN_SIZE, max = ConstantsApp.USER_USERNAME_SIZE)
	private String name;

	@NotBlank
	@Size(max = ConstantsApp.USER_EMAIL_SIZE)
	@Email
	private String email;

	@NotBlank
	@Size(min = ConstantsApp.MIN_SIZE, max = ConstantsApp.USER_PASSWORD_SIZE)
	private String password;
	
	//FIX: Request name value
	public void setName(String name) {
		this.name = name;
		this.username = name;
	}
}
