package checkoutapi.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;
import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import checkoutapi.security.audit.UserDateAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


/*
 * https://www.baeldung.com/hibernate-inheritance
 * 
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 * @DiscriminatorColumn(name="user_type", discriminatorType = DiscriminatorType.INTEGER)
 * -----
 * @Inheritance(strategy = InheritanceType.JOINED)
 * -----
 * @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(columnNames = { "username" }),
	@UniqueConstraint(columnNames = { "email" })
})
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String fullname;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
    	joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Boolean verified = Boolean.FALSE;

	/**
	 * Código de verificación registración (email or sms).
	 */
	private String verifyCode;	//verification: uuid.v4()

	@NotNull
	private Boolean enabled = Boolean.FALSE;
	
	private Integer loginAttempts = 0;

	private Date blockExpires = new Date();
	
    public User() {
    }

    public User(String fullname, String username, String email, String password) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Cacheable
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public Date getBlockExpires() {
		return blockExpires;
	}

	public void setBlockExpires(Date blockExpires) {
		this.blockExpires = blockExpires;
	}
}