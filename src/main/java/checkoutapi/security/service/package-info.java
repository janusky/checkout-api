/**
 * <p>
 * Aquí las implementaciones donde se necesita utilizar transacción diferente
 * entre varios objetos de acceso a datos (DAO).
 * </p>
 * <p>
 * También
 * 
 * <pre>
 * Aplicar conversión de Entity to DTO (*)
 * Aplicar cache por encima de los objetos DAO
 * </pre>
 * </p>
 * <p>
 * * Al utilizar Hibernate para referenciar relaciones como lazy (fetch =
 * FetchType.LAZY) se debe leer la Entity dentro de la transacción para
 * recuperar dichas relaciones a demanda.<br/>
 * Leer:
 * 	<dd>https://www.baeldung.com/jpa-entity-graph</dd>
 * 	<dd>https://www.baeldung.com/java-performance-mapping-frameworks</dd>
 * </p>
 * <p>
 * NOTA: Solo utilizar cuando sea necesario. La mayoría de las veces se puede
 * resolver con las instancias de objetos DAO.
 * </p>
 * 
 * @author janusky@mail.com
 * @version 1.0 - Aug 31, 2018 4:12:10 PM
 *
 */
package checkoutapi.security.service;
