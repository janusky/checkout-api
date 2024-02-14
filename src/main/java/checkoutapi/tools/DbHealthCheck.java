package checkoutapi.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class DbHealthCheck implements HealthIndicator {
	@Autowired
	JdbcTemplate template;

	@Autowired
	UtilCommon utilCommon;

	@Override
	public Health health() {
		int errorCode = check(); // perform some specific health check
		if (errorCode != 1) {
			String key = "error.code";
			String message = utilCommon.getMessage(key, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Health.down().withDetail(message, HttpServletResponse.SC_UNAUTHORIZED).build();
		}
		return Health.up().build();
	}

	public int check() {
		String query = "select 1 from dual";
		List<Object> results = template.query(query, new SingleColumnRowMapper<>());
		return results.size();
	}
}
