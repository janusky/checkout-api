package checkoutapi.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import checkoutapi.payload.AppInfoDetail;
import checkoutapi.payload.AppInfoResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente que permite recuperar información de la aplicación.
 * 
 * @see {@link http://biblioint.afip.gob.ar/pdf/IG_5_18_DIECCS_MONITSALUD.pdf}
 * @see afip.app.payload.AppInfoResponse
 * 
 * @author manuelhernandez@afip.gob.ar
 * @version 1.0 - 16 sep. 2019 16:04:37
 *
 */
@Component
@Slf4j
public class ApplicationInfo {
	@Autowired
	private DbHealthCheck dbHealthCheck;

	@Value("${info.app.system:janusky}")
	private String system;

	@Value("${info.app.name:checkout-api}")
	private String application;

	@Value("${info.app.version:demo}}")
	private String version;

	@Value("${info.app.environment:production}")
	private String environment;

	public AppInfoResponse report() {
		List<AppInfoDetail> details = new ArrayList<>();
		details.add(dataBaseStatus());
		AppInfoResponse response = AppInfoResponse.builder()
				.system(system)
				.application(application)
				.version(version)
				.environment(environment)
				.details(details)
				.build();
		log.debug(" Application status {}", response);
		return response;
	}

	private AppInfoDetail dataBaseStatus() {
		Health health = dbHealthCheck.health();

		AppInfoDetail appInfoDetail = AppInfoDetail.builder()
				.name("DB Conection")
				.status(health.getStatus().getCode())
				.details(health.getDetails())
				.build();
		return appInfoDetail;
	}
}
