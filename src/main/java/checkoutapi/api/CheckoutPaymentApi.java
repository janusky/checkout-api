package checkoutapi.api;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import checkoutapi.payload.CheckeoutPaymentResponse;
import checkoutapi.payload.CheckeoutSubmitRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Checkout Payment Api", description = "Interacción con API de pagos que permite realizar transacciones asociadas a una sesión de checkout.")
public class CheckoutPaymentApi {
	private static final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private RestTemplate restTemplateCheckoutPaymentApi;

	@Value("${app.route.checkout-payment-api.submit}")
	private String checkoutPaymentApiSubmit;

	@Value("${app.route.checkout-payment-api.payment}")
	private String checkoutPaymentApiPayment;

	@Operation(summary = "Create a Order the Session Checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = { @
                    Content(mediaType = "application/json", schema = @Schema(implementation = CheckeoutPaymentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping("${app.route.api:/v1}/payments/orders")
    public ResponseEntity<CheckeoutPaymentResponse> submit(
    		@Parameter(description = "Order object to be created") @RequestBody @Valid CheckeoutSubmitRequest submit) {
		log.debug("Send payment Order {}, for company template id={}", checkoutPaymentApiSubmit, submit.getIdCheckoutCompany());

        ResponseEntity<Object> responseApi = restTemplateCheckoutPaymentApi
        	.exchange(checkoutPaymentApiSubmit, HttpMethod.POST, null, Object.class);
        
        CheckeoutPaymentResponse mapModel = modelMapper.map(responseApi.getBody(), CheckeoutPaymentResponse.class);

        return new ResponseEntity<CheckeoutPaymentResponse>(mapModel, mapModel.getStatus().getHttpStatus());
    }
	
	@Operation(summary = "Process Payment a Order the Session Checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Process payment Order", content = { @
                    Content(mediaType = "application/json", schema = @Schema(implementation = CheckeoutPaymentResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping("${app.route.api:/v1}/payments/orders/{id}/payment/{amount}")
    public ResponseEntity<CheckeoutPaymentResponse> Payment(
    		@PathVariable UUID id, @PathVariable Double amount) {
		String uriCreate = checkoutPaymentApiPayment
								.replaceFirst("\\{id\\}", id.toString())
								.replaceFirst("\\{amount\\}", amount.toString());
		
		ResponseEntity<Object> responseApi = restTemplateCheckoutPaymentApi
        	.exchange(uriCreate, HttpMethod.POST, null, Object.class);
        
        CheckeoutPaymentResponse mapModel = modelMapper.map(responseApi.getBody(), CheckeoutPaymentResponse.class);

        return new ResponseEntity<CheckeoutPaymentResponse>(mapModel, mapModel.getStatus().getHttpStatus());
    }
}
