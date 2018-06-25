package com.z.kotlinreact.DAO

import com.z.kotlinreact.Data.Role
import com.z.kotlinreact.Data.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface RoleDAO : MongoRepository<Role, String> {
    override fun findAll(pageable: Pageable): Page<Role>
}
interface UserDAO: MongoRepository<User, String> {
    override fun findAll(pageable: Pageable): Page<User>
    fun findByState(state: Boolean): List<User>?
    @Query("{ '_id': ?0}") fun getById(id:String):User?
}