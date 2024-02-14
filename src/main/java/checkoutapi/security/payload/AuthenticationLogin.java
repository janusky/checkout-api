package checkoutapi.security.payload;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationLogin {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

	/** The IP Address of the requester */
    private String ipAddr;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
}
