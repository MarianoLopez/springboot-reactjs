package com.z.kotlinreact.Services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.mec.api.users.Util.asLocalDateTime
import com.z.kotlinreact.Data.LoginResponse


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.servlet.http.HttpServletRequest

class JWTService(private val EXPIRATIONTIME: Long, private val SECRET: String, private val TOKEN_PREFIX: String, private val HEADER_STRING: String,private val ISSUER:String, private val ipAsAudience:Boolean = false){
    @Qualifier("UserService") @Autowired private lateinit var userDetails: UserDetailsService
    private val  defaultAudience = "REMOTE_USER"
    //create token
    @Throws(JWTCreationException::class)
    fun createToken(auth: Authentication,req: HttpServletRequest): LoginResponse {
        val expires = Date(System.currentTimeMillis() + EXPIRATIONTIME)
        val audience = if(ipAsAudience) getIp(req) else defaultAudience
        val token = JWT.create().apply {
            withSubject(auth.name)
            withIssuer(ISSUER)
            withExpiresAt(expires)
            withArrayClaim("roles", auth.authorities.map { it.authority }.toTypedArray())
            withAudience(audience)
        }.sign(Algorithm.HMAC512(SECRET))
        return LoginResponse(token=token,expires = expires.asLocalDateTime(), username = auth.name,audience = audience,roles = auth.authorities.map { it.authority })

    }

    //get auth from token
    @Throws(UsernameNotFoundException::class,JWTVerificationException::class)
    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)?:request.getParameter("token")//token from header-parameter
        decode(token,request)?.let {
            userDetails.loadUserByUsername(it.subject).let {
                return UsernamePasswordAuthenticationToken(it.username, it.password, it.authorities)
            }
        }
        throw object : AuthenticationException("invalid token") {}
    }


    fun getInfo(token:String,request: HttpServletRequest): LoginResponse {
        decode(token,request)?.let {jwt->
            userDetails.loadUserByUsername(jwt.subject).let {
                return LoginResponse(token=jwt.token,expires = jwt.expiresAt.asLocalDateTime(), username = jwt.subject,audience = jwt.audience.joinToString(),roles = it.authorities.map { it.authority },enable = it.isEnabled)
            }
        }
        throw object : AuthenticationException("invalid token") {}
    }

    private fun decode(token:String,request: HttpServletRequest): DecodedJWT? {
        val audience = if(ipAsAudience) getIp(request) else defaultAudience
        val verifier = JWT.require(Algorithm.HMAC512(SECRET)).apply {
            withIssuer(ISSUER)
            withAudience(audience)
        } .build() //config decoder
        return verifier.verify(token.replace(TOKEN_PREFIX, "").trim({ it <= ' ' })) //decode token

    }

    private fun getIp(req:HttpServletRequest)=req.remoteAddr?.replace("0:0:0:0:0:0:0:1","127.0.0.1")?.replace("localhost","127.0.0.1") ?: "unknown"
}