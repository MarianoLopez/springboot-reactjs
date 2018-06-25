package com.z.kotlinreact.Configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.kotlinreact.Data.Role
import com.z.kotlinreact.DAO.RoleDAO
import com.z.kotlinreact.Data.User
import com.z.kotlinreact.Services.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class OnBoot(private val userService: UserService, private val roleDAO: RoleDAO, private val mapper: ObjectMapper):ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        createAdminIfDoesNotExists()
        createTesterIfDoesNotExists()
    }

    fun createAdminIfDoesNotExists() {
        val adminRole = roleDAO.findById("administrator").let{ if(!it.isPresent)roleDAO.insert(Role("administrator", "administrator")) else it.get() }
        if(!userService.userDAO.findById("administrator").isPresent) userService.insert(User("administrator", "administrator", "administrator", true, listOf(adminRole))).also { println("admin user:\n\t" + mapper.writeValueAsString(it)) }
    }

    fun createTesterIfDoesNotExists() {
        val guestRole = roleDAO.findById("guest").let{ if(!it.isPresent)roleDAO.insert(Role("guest", "guest")) else it.get() }
        if(!userService.userDAO.findById("guest").isPresent) userService.insert(User("guest", "guest", "guest", true, listOf(guestRole))).also { println("guest user:\n\t" + mapper.writeValueAsString(it)) }
    }
}