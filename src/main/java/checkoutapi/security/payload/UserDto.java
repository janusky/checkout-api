package checkoutapi.security.payload;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import checkoutapi.entity.Role;

@JsonInclude(Include.NON_NULL)
public class UserDto {
	private Long id;
	private String username;
	private String email;
	private String fullname;
	private Instant joinedAt;
	private Set<String> roles;

	public UserDto(Long id, String username, String email, String fullname, Instant joinedAt, Set<Role> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.joinedAt = joinedAt;
		if (roles != null && !roles.isEmpty()) {
			this.roles = roles.stream().map(role -> role.getName().toString()).collect(Collectors.toSet());
		}
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
