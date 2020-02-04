package io.swagger.configuration;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(
		value = "io.swagger.codegen.languages.SpringCodegen",
		date = "2019-07-03T08:26:08.029-07:00")

@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
public class SwaggerDocumentationConfig extends WebSecurityConfigurerAdapter {

	private final String			adminContextPath	= "";

	protected static final Logger	logger				= LoggerFactory
			.getLogger(SwaggerDocumentationConfig.class);

	@Value("${CORSOrigins:http://localhost:8100}")
	public String					CORSOrigins;

	@Value("${CORSMapping:/**}")
	public String					CORSMapping;

	/**
	 * the CORS configuration for the REST api
	 *
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		logger.warn("Init CORS Config Origins: CORSOrigins "
				+ CORSOrigins);
		logger.warn("Init CORS Config Mapping: CORSMapping "
				+ CORSMapping);
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");
		configuration.addExposedHeader("X-Content-Type-Options");
		configuration.addExposedHeader("WWW-Authenticate");
		configuration.addExposedHeader("Access-Control-Allow-Origin");
		configuration.addExposedHeader("Access-Control-Allow-Headers");

		configuration.setAllowedOrigins(Arrays.asList(CORSOrigins));
		configuration.setAllowedMethods(Arrays
				.asList("GET", "POST", "INSERT", "DELETE", "HEAD", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(CORSMapping, configuration);
		return source;
	}

	// @Override
	protected void configureXX(HttpSecurity http) throws Exception {
		// @formatter:off
        final SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");

        http.authorizeRequests()
            .antMatchers(adminContextPath + "/assets/**").permitAll()
            .antMatchers(adminContextPath + "/login**").permitAll()
            .anyRequest().authenticated()
            .and()

        .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).loginProcessingUrl("/login").and()
        .logout().logoutUrl(adminContextPath + "/logout").and()
        .httpBasic().and()
        .csrf().disable();
        // @formatter:on
	}

	// @Override
	protected void configureXXA(final AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
        // @formatter:on
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
        http
                .csrf().disable().cors();
                // .authorizeRequests()
                // .antMatchers("/admin/**").hasRole("ADMIN")
                // .antMatchers("/anonymous*").anonymous()
                // .antMatchers("/login*").permitAll()
                // .anyRequest().authenticated()
                //.and()
                //.formLogin()
                //.loginPage("/login")
                //.loginProcessingUrl("/perform_login")
                //.defaultSuccessUrl("/homepage.html", true)
                //.failureUrl("/login.html?error=true")
                //.failureHandler(authenticationFailureHandler())
                //.and()
                //.logout()
                //.logoutUrl("/perform_logout")
                //.deleteCookies("JSESSIONID")
                // .logoutSuccessHandler(logoutSuccessHandler());
        //.and()
        //.exceptionHandling().accessDeniedPage("/accessDenied");
        //.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        // @formatter:on
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
        auth.inMemoryAuthentication()
        .withUser("user1").password("user1Pass").roles("USER")
        .and()
        .withUser("user2").password("user2Pass").roles("USER")
        .and()
        .withUser("admin").password("admin0Pass").roles("ADMIN");
        // @formatter:on
	}

	// @Override
	protected void configureNEW(final HttpSecurity http) throws Exception {
		// @formatter:off
        http.cors().and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/anonymous*").anonymous()
        .antMatchers(HttpMethod.GET, "/index*", "/static/**", "/*.js", "/*.json", "/*.ico").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/index.html")
        .loginProcessingUrl("/perform_login")
        .defaultSuccessUrl("/homepage.html",true)
        .failureUrl("/index.html?error=true")
        .and()
        .logout()
        .logoutUrl("/perform_logout")
        .deleteCookies("JSESSIONID");
        // @formatter:on
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Starter StackGen API")
				.description("This is the Starter StackGen API")
				.license("AGPL 3.0")
				.licenseUrl("https://www.gnu.org/licenses/agpl-3.0.html")
				.termsOfServiceUrl("").version("1.0.0")
				.contact(new Contact("", "", "info@StackGen.io")).build();
	}

	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors
						.basePackage("io.starter.stackgen.api"))
				.build()
				.directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
				.directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
				.apiInfo(apiInfo());
	}

}
