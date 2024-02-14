package checkoutapi.payload;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Información de la aplicación.
 * 
 * @see AppInfoDetail
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 16 sep. 2019 15:28:43
 *
 */
@Data
@Builder
public class AppInfoResponse {
	private String system;
	private String application;
	private String version;
	/**
	 * Ambiente en el que se esta ejecutando la aplicación. Solo puede contener los
	 * siguientes valores:
	 * 
	 * <pre>
	 * • DEVELOPMENT • TESTING • STAGING • PRODUCTION
	 * </pre>
	 */
	private String environment;

	/**
	 * Estado general de la aplicación. Debe contener el valor del estado mas
	 * crítico de los submódulos definidos en details. Debe contener alguno de los
	 * siguientes valores (en orden del menos crítico al mas crítico):
	 * 
	 * <pre>
	 * • UP • DOWN
	 * </pre>
	 */
	private String status;

	/**
	 * Mensaje descriptivo que especifica el error ocurrido por el cual la
	 * aplicación no se encuentra operativa ('UP'). Solo se debe especificar en caso
	 * de que la aplicación no se encuentre en estado operativa.
	 */
	private String message;

	/**
	 * Detalles del estado de salud de la aplicación. Puede contener los detalles
	 * de: los módulos de la aplicación, las bases de datos, las aplicaciones
	 * externas con las que se interactua, el ambiente en que se esta corriendo,
	 * etc.
	 */
	private List<AppInfoDetail> details;
}
