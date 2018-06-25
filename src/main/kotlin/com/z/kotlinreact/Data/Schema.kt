package com.z.kotlinreact.Data

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

/*Schemas*/
@Document
data class User (@Id val id: String?,
                 var name:String,
                 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) var password:String,
                 var state:Boolean=true,
                 var roles:List<Role>,
                 val createdDate: LocalDateTime = LocalDateTime.now())

@Document data class Role(@Id val id: String?, val name:String)