package checkoutapi.tools;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorHelper {
	final Logger log = LoggerFactory.getLogger(ErrorHelper.class);

	public static ResponseDefault FAIL_EMAIL;
	public static ResponseDefault FAIL_USERNAME;

	@Autowired
	private UtilCommon utilCommon;

	public String errorTicket(Throwable ex) {
		return errorTicket(UUID.randomUUID().toString(), ex);
	}

	public String errorTicket(String errorIdentified, Throwable ex) {
		String key = "error.500";
		String message = utilCommon.getMessage(key);
		return errorTicket(message, ex, errorIdentified);
	}

	public String errorTicket(String message, Throwable ex, String errorIdentified) {
		String errorResp = message;
		if (errorIdentified != null) {
			String errorId = utilCommon.getMessage("error.code", errorIdentified);
			errorResp = message.concat("\n" + errorId);
		}
		log.error(errorResp, ex);
		return errorResp;
	}
	
	@Autowired
	public void initIt() {
		FAIL_EMAIL = new ResponseDefault(false, utilCommon.getMessage("user.register.email"));
		FAIL_USERNAME = new ResponseDefault(false, utilCommon.getMessage("user.register.username"));
	}
}
