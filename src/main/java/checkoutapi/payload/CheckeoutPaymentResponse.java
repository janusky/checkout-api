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
public class CheckeoutPaymentResponse {
	/**
	 * Identificador de inicio de Pago sesión de checkout.
	 */
	@NotEmpty(message = "Not null")
	private String transaction;

	private StatusResponse status;
}
