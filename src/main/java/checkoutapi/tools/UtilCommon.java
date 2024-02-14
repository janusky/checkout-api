package checkoutapi.tools;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UtilCommon {
	private static final String ENCODING = "UTF-8";

	@Autowired
	private MessageSource messageSource;

	/**
	 * Message finder in <tt>MessageSource</tt> (internalization).
	 * 
	 * @param key  String clave en archivo de propiedades.
	 * @param args String[] Array con argumentos en valor de la clave.
	 * @return String valor de la clave encontrada con sus argumentos.
	 */
	public String getMessage(String key, Object... args) {
		String message = null;
		try {
			if (key != null) {
				Locale locale = LocaleContextHolder.getLocale();
				message = messageSource.getMessage(key, args, locale);
			}
		} catch (Exception e) {
			log.warn("find message {} Args: {} Error: {}", key, args, e.getMessage());
		}
		return message;
	}

	public String format(String name, Object value) {
		return "\"&" + name + '=' + UriUtils.encode(value.toString(), ENCODING) + "\"";
	}
}
