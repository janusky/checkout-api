package checkoutapi.payload;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckeoutPaymentRequest {
	/**
	 * Transaction identifier.
	 */
	@NotEmpty(message = "Not null")
	private UUID transacion;
	
	private double amount;
}
