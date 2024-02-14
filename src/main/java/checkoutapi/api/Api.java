package checkoutapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import checkoutapi.payload.AppInfoResponse;
import checkoutapi.tools.ApplicationInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${app.route.api:/v1}")
@Slf4j
@Tag(name = "API Checkout Session", description = "Information of the API")
public class Api {
	@Autowired
	private ApplicationInfo applicationInfo;

	@Operation(
		security = {}, 
		description = "Status (public access)"
	)
	@GetMapping(value = { "/status" })
	public ResponseEntity<AppInfoResponse> version() {
		log.debug("status");
		AppInfoResponse report = applicationInfo.report();		
		return ResponseEntity.ok(report);
	}

	public final ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

	public final void setApplicationInfo(final ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
}
