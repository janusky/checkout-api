package checkoutapi.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckeoutSubmitRequest {
	/**
	 * Identificador de la configuración Checkout utilizada.
	 */
	@NotEmpty(message = "Not null")
	private String idCheckoutCompany;
}
