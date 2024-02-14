package checkoutapi.entity;

import org.springframework.data.annotation.PersistenceConstructor;

import checkoutapi.security.audit.DateAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;


@SuppressWarnings("deprecation")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceConstructor))
@ToString
@Entity
@Table(name = "user_access")
public class UserAccess extends DateAudit {
	/**Serial auto generated id*/
	private static final long serialVersionUID = -6025785576207008556L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	/** The IP Address of the requester */
    private String ipAddr;
    
    private String browser;

    private String country;
}
