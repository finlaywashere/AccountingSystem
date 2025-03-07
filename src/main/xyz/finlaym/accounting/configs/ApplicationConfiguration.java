package xyz.finlaym.accounting.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import xyz.finlaym.accounting.persistance.auth.AccountRepository;

/**
 * Configures the application to use JWT authentication
 */
@Configuration
public class ApplicationConfiguration {
	@Autowired
	private AccountRepository acctRepository;
	
	/**
	 * Gets the service that maps user names to user details
	 * @return The user name to user details mapping service
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return username -> acctRepository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	/**
	 * Gets an instance of the password encoder
	 * @return A BCrypt password encoder
	 */
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Gets the authentication manager
	 * @param config Unused
	 * @return The authentication manager
	 * @throws Exception See AuthenticationConfiguration javadocs
	 */
	@Bean
	public AuthenticationManager authenticationmanager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	/**
	 * Gets the authentication provider
	 * @return The Dao auth provider using the Account model and BCrypt password hashing
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
