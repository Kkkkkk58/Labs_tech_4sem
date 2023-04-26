package ru.kslacker.cats.common.models;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum UserRole implements GrantedAuthority {
	ADMIN(Code.ADMIN),
	USER(Code.USER);

	private final String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

	public static class Code {
		public static final String ADMIN = "ROLE_ADMIN";
		public static final String USER = "ROLE_USER";
	}
}
