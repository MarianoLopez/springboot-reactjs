package com.z.kotlinreact.Services


import com.z.kotlinreact.Data.User
import com.z.kotlinreact.DAO.UserDAO
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service("UserService")
class UserService(val userDAO: UserDAO, private val encoder: PasswordEncoder): UserDetailsService{
    fun update(user: User): User = userDAO.save(user.apply { this.password = encoder.encode(this.password) })
    fun insert(user: User): User = userDAO.insert(user.apply { this.password =  encoder.encode(this.password) })

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(id: String): UserDetails {//load user info (security core)
        if (id.isNotEmpty()){
            userDAO.getById(id)?.let {
                return org.springframework.security.core.userdetails.User(it.name,it.password,it.state,
                        true,true,true,
                        it.roles.map { role -> SimpleGrantedAuthority("ROLE_${role.name}") })
            }
        }
        throw UsernameNotFoundException("Username not found")
    }
}