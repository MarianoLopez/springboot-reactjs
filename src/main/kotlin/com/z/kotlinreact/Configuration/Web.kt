package com.z.kotlinreact.Configuration

import com.z.kotlinreact.Configuration.Middlewares.CustomAuthenticationProvider
import com.z.kotlinreact.Configuration.Middlewares.CustomRequestFilter
import com.z.kotlinreact.Services.JWTService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class Web : WebSecurityConfigurerAdapter() {
    //read from application.properties
    @Value("\${jsonWebToken.expiration-time}") private val expirationTime: String? = null
    @Value("\${jsonWebToken.secret}") private val secret: String? = null
    @Value("\${jsonWebToken.token-prefix}") private val tokenPrefix: String? = null
    @Value("\${jsonWebToken.header}") private val headerString: String? = null
    @Value("\${jsonWebToken.issuer}") private val issuer: String? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()//no csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()//no session
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/login").permitAll().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/token-info").access("hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('127.0.0.1')").and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/user/**").hasAnyRole("administrator","guest").and()
                .authorizeRequests().antMatchers("/api/user/**").hasAnyRole("administrator").and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/role/**").authenticated().and()
                .authorizeRequests().antMatchers("/api/role/**").hasAnyRole("administrator","guest").and()
        .addFilterBefore(customRequestFilter(), UsernamePasswordAuthenticationFilter::class.java)// And filter check the presence of JWT in header & stuffs
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) { auth!!.authenticationProvider(authProvider())}//spring custom auth provider

    /*Beans*/
    @Bean @Primary  fun authProvider() = CustomAuthenticationProvider()//auth provider implementation

    @Bean fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean fun jwtService()= JWTService(expirationTime!!.toLong(), secret!!, tokenPrefix!!, headerString!!,issuer!!)//init jwt Services

    @Bean fun customRequestFilter() = CustomRequestFilter()
}