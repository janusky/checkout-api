package checkoutapi.payload;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Detalles de información para {@link AppInfoResponse}
 * 
 * @see AppInfoResponse
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 16 sep. 2019 15:45:43
 *
 */
@Data
@Builder
public class AppInfoDetail {
	private String name;
	private String status;
	private Map<String, Object> details;
}
