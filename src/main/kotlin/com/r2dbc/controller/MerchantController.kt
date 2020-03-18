package com.r2dbc.controller

import com.r2dbc.model.LoginRequest
import com.r2dbc.model.Merchant
import com.r2dbc.repository.MerchantRepository
import com.r2dbc.service.MerchantService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("merchant")
class MerchantController(private val merchantService: MerchantService) {
    @PostMapping(value = ["login"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@ModelAttribute("merchant") request: LoginRequest): ResponseEntity<*>? {
        val response = (request.password?.let { request.email?.let { it1 -> merchantService.login(it1, it) } })
        return ResponseEntity.status(response.status).body(response.message)
    }

    @GetMapping("all")
    fun findAll(): Flux<Merchant?> {
        return merchantService.findAll()
    }

    @GetMapping("/find/name/{registeredName}")
    fun findByRegisteredName(@PathVariable registeredName: String?): Mono<Merchant?>? {
        return merchantService.findByRegisteredName(registeredName)
    }

    @GetMapping("/find/email/{email}")
    fun findByEmail(@PathVariable email: String?): Mono<Merchant?>? {
        return merchantService.findByEmail(email)
    }
}