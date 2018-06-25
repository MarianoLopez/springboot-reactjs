package com.z.kotlinreact.Controllers

import com.z.kotlinreact.Configuration.Middlewares.CustomAuthenticationProvider
import com.z.kotlinreact.Services.JWTService
import com.z.kotlinreact.Data.AccountCredentials
import io.swagger.annotations.ApiOperation
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("api/")
class LoginController(private val tokenAuth: JWTService, private val authProvider: CustomAuthenticationProvider) {
    @ApiOperation(value = "Solicitar token de autenticación",produces = "application/json;charset=UTF-8")
    @PostMapping("login") fun login(@RequestBody credentials: AccountCredentials, req: HttpServletRequest) = tokenAuth.createToken(authProvider.authenticate(UsernamePasswordAuthenticationToken(credentials.username, credentials.password, Collections.emptyList())),req)

    @ApiOperation(value = "Solicitar información de un token particular (IPs permitidas: 127.0.0.1)",produces = "application/json;charset=UTF-8")
    @GetMapping("token-info") fun tokenInfo(@RequestParam token:String, req: HttpServletRequest) = tokenAuth.getInfo(token,req)
}