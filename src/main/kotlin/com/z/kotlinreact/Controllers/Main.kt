package com.z.kotlinreact.Controllers

import com.z.kotlinreact.Data.Role
import com.z.kotlinreact.Data.User
import com.z.kotlinreact.Services.UserService
import com.z.kotlinreact.DAO.RoleDAO
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/user")
class UserController(private val userService: UserService) {
    @ApiOperation(value = "Lista de Usuarios",produces = "application/json;charset=UTF-8")
    @ApiImplicitParams(ApiImplicitParam(name = "page", value = "pagina actual (default 0)",required = false, dataType = "int", paramType = "query"),ApiImplicitParam(name = "size", value = "registros por pagina (default 20)",required = false, dataType = "int", paramType = "query"),ApiImplicitParam(name = "sort", value = "campo, dirección(asc(default)|desc) (default unsort). Admite multiples valores: sort=campo&sort=campo,desc..",required = false, dataType = "string", paramType = "query", allowMultiple = true))
    @GetMapping fun findAll(pageable: Pageable): Page<User> = userService.userDAO.findAll(pageable)

    @ApiOperation(value = "Búsqueda de usuario por ID",produces = "application/json;charset=UTF-8")
    @GetMapping("{id}") fun findById(@PathVariable id:String): Optional<User> = userService.userDAO.findById(id)

    @ApiOperation(value = "Insertar usuario",produces = "application/json;charset=UTF-8")
    @PostMapping fun insert(@RequestBody user: User): User = userService.insert(user)

    @ApiOperation(value = "Actualizar usuario",produces = "application/json;charset=UTF-8")
    @PutMapping fun update(@RequestBody user: User): User = userService.update(user)

    @ApiOperation(value = "Eliminar usuario",produces = "application/json;charset=UTF-8")
    @DeleteMapping("{id}") fun delete(@PathVariable id:String): Optional<User> = userService.userDAO.findById(id).apply { ifPresent { userService.userDAO.delete(it) } }
}

@RestController
@RequestMapping("api/role")
class UserRoleController(private val roleDAO: RoleDAO){
    @ApiOperation(value = "Lista de roles",produces = "application/json;charset=UTF-8")
    @ApiImplicitParams(ApiImplicitParam(name = "page", value = "pagina actual (default 0)",required = false, dataType = "int", paramType = "query"),ApiImplicitParam(name = "size", value = "registros por pagina (default 20)",required = false, dataType = "int", paramType = "query"),ApiImplicitParam(name = "sort", value = "campo, dirección(asc(default)|desc) (default unsort). Admite multiples valores: sort=campo&sort=campo,desc..",required = false, dataType = "string", paramType = "query", allowMultiple = true))
    @GetMapping fun findAll(pageable: Pageable):Page<Role> = roleDAO.findAll(pageable)

    @ApiOperation(value = "Búsqueda de rol por ID",produces = "application/json;charset=UTF-8")
    @GetMapping("{id}") fun findById(@PathVariable id:String) = roleDAO.findById(id)

    @ApiOperation(value = "Insertar rol",produces = "application/json;charset=UTF-8")
    @PostMapping fun insert(@RequestBody role:Role) = roleDAO.insert(role)

    @ApiOperation(value = "Actualizar rol",produces = "application/json;charset=UTF-8")
    @PutMapping fun update(@RequestBody role:Role) = roleDAO.save(role)

    @ApiOperation(value = "Eliminar rol",produces = "application/json;charset=UTF-8")
    @DeleteMapping("{id}") fun delete(@PathVariable id:String): Optional<Role>  = roleDAO.findById(id).apply { ifPresent { roleDAO.delete(it) } }
}