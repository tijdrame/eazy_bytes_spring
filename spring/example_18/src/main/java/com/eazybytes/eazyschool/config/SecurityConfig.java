package com.eazybytes.eazyschool.config;

//import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//@AllArgsConstructor
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * Default configurations which will secure all the requests
         */
         //http .authorizeRequests() .anyRequest().authenticated() .and()
         //.formLogin().and() .httpBasic();
        /**
         * Configuration to permit all the requests
         */
        /*
         * http .authorizeRequests() .anyRequest().permitAll().and() .formLogin().and()
         * .httpBasic();
         */

        /**
         * Custom configurations as per our requirement
         */
        /*http
                // pour dire a spring sec de ne pas gérer la session (il ne vas creer de token comme JSessionId)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        // pour ajouter au header une balise authorization avec le token
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and()
                //.csrf().disable()//par defaul mais pas conseillé coté sécurité
                .csrf().disable()
                //pas besoin de csrf car avec jwt il y a un controle qui est fait
                //.ignoringAntMatchers("/contact")//pas forcement conecte donc pas de xsrf a envoyé pour au backend
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                //le filter sera executer avant BasicAuthenticationFilter (usrdetailService) donc desactiver AuthenticationProvider
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                //sexec apres Basic...
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                //add filter jwt
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                // spring secu va choisir de maniére aléattoire lequel exec le 1er a chaqu fois
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/myAccount").hasRole("ADMIN")//sans prefix
                .antMatchers("/myBalance").hasRole("USER")
                .antMatchers("/myLoans").authenticated()
                .antMatchers("/myCards").hasAuthority("ROLE_USER")
                .antMatchers( "/user").authenticated()
                // .anyRequest().permitAll()
                .antMatchers("/notices", "/contact", "/login").permitAll()
                // .antMatchers("/contact").permitAll()
                .and().formLogin().and().httpBasic();*/
        /**
         * Configuration to deny all the requests
         */
          http.csrf()
                  .ignoringRequestMatchers("/saveMsg")
                  .ignoringRequestMatchers("/public/**")
                  .ignoringRequestMatchers("/api/**")
                  .ignoringRequestMatchers("/data-api/**")
                  .ignoringRequestMatchers("/eazyschool/actuator/**")
                  //.ignoringRequestMatchers(PathRequest.toH2Console())
                  .and()
                  .authorizeHttpRequests()
                  //.requestMatchers("/profile/**").permitAll()
                  .requestMatchers("/data-api/**").authenticated()
                  .requestMatchers("/dashboard").authenticated()
                  .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                  .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                  .requestMatchers("/displayProfile").authenticated()
                  .requestMatchers("/admin/**").hasRole("ADMIN")
                  .requestMatchers("/eazyschool/actuator/**").hasRole("ADMIN")
                  .requestMatchers("/api/**").authenticated()
                  .requestMatchers("/updateProfile").authenticated()
                  .requestMatchers("/student/**").hasRole("STUDENT")
                  .requestMatchers("/home").permitAll()
                  //.requestMatchers(PathRequest.toH2Console()).permitAll()
                  .requestMatchers("/holidays/**").permitAll()
                  .requestMatchers("/contact").permitAll()
                  .requestMatchers("/saveMsg").permitAll()
                  .requestMatchers("/courses").permitAll()
                  .requestMatchers("/about").permitAll()
                  .requestMatchers("/login").permitAll()
                  .requestMatchers("/public/**").permitAll()
                  .requestMatchers("/logout").permitAll()
                  .requestMatchers("/assets/**").permitAll()
                  .and().formLogin().loginPage("/login")
                  .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll()
                  .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
                  .and().httpBasic();
        //http.headers().frameOptions().disable();//for h2 db
        return http.build();

    }

    /*protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("user").password(passwordEncoder().encode("12345")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("12345")).roles("USER", "ADMIN")
                .and();
                //.passwordEncoder(passwordEncoder());
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/assets/**");
    }

    /*@Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails admin = User.builder()
                .username("user")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("12345"))
                .roles("USER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }*/


    /*@Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    public JdbcUserDetailsManager userDetailsService(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        //JdbcUserDetailsManager
        /*UserDetails user = User.
        withDefaultPasswordEncoder()
            .username("tij").password("passer").roles("USER")
            .username("helgi").password("passer").roles("USER")
            .build();

        return null;
        //return new InMemoryUserDetailsManager(user);
    }*/
}
