package com.z.kotlinreact.Data

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime


data class Message(val title:String, @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") val date: LocalDateTime = LocalDateTime.now(), val message: Any?= null)
data class AccountCredentials(val username: String, val password: String)
data class LoginResponse(val token:String, val expires:LocalDateTime,val roles:List<String>,val username:String, val audience:String = "",val enable:Boolean=true)