package br.com.alura.mvc.mudi;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> {
			try {
				requests
						.antMatchers("/home/**")
						.permitAll()
						.anyRequest()
							.authenticated()
						.and()
						.formLogin((form) -> form
								.loginPage("/login")
								.defaultSuccessUrl("/usuario/pedido", true)
								.permitAll()	
							).logout((logout) -> logout.logoutUrl("/logout")
									.logoutSuccessUrl("/home"))
						.csrf().disable();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return http.build();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
	  throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
//		UserDetails user =
//				 User.builder()
//					.username("safada66")
//					.password(encoder.encode("safada66"))
//					.roles("ADM")
//					.build();
//		
		
	    auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .passwordEncoder(encoder);
//	      .withUser(user);
	}
}
