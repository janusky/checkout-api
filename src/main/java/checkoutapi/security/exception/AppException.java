package checkoutapi.security.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Agrupa a las excepciones controladas, que deben estar relacionadas con alguna
 * clave del archivo de mensajes {@link MessageSource}.
 * </p>
 * NOTA: Solo se debe crear instancias de esta por medio del método
 * <tt>AppException.of</tt>.<br/>
 * Extiende de {@link RuntimeException} para no obligar la captura. Porque no es 
 * necesario si es capturada en {@link GlobalExceptionHandlerController} 
 * (@ExceptionHandler(AppException.class)).
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 28 nov. 2019 11:28:08
 *
 */
@Component
public class AppException extends RuntimeException {
	/** Default serial. */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MessageSource messageSource;

	private String code;

	AppException() {
	}

	private AppException(String key, String message) {
		super(message);
		this.code = key;
	}

	public AppException of(String key, Object... args) /* throws NoSuchMessageException */ {
		Locale locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage(key, args, locale);
		return new AppException(key, message);
	}

	public String getCode() {
		return code;
	}
}
