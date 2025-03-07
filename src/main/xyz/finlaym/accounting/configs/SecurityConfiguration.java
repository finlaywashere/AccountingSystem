package xyz.finlaym.accounting.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import xyz.finlaym.accounting.subsystem.auth.JwtAuthenticationFilter;

/**
 * Configures security filters and CORS for the system
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Creates a security filter chain that enforces auth on endpoints
	 * @param http The HttpSecurity instance
	 * @return A security filter chain with CSRF disabled and custom auth requirements for API endpoints
	 * @throws Exception See DefaultSecurityFilterChain build() docs
	 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                		.requestMatchers("/auth/**").permitAll()
                		.requestMatchers("/cash/**").hasAuthority("READ_CASH")
                		.anyRequest().authenticated())
                .sessionManagement(sess -> sess
                		.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS settings
     * @return A CORS configuration that allows our origin and the methods in use by this app
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
