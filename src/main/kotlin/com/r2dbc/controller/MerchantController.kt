package com.r2dbc.controller

import com.r2dbc.model.Merchant
import com.r2dbc.repository.MerchantRepository
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
class MerchantController {
    private val LOG = LoggerFactory.getLogger(MerchantController::class.java)

    @Autowired
    private val merchantRepository: MerchantRepository? = null

    fun login(email: String, password: String): ResponseEntity<*> {
        val authenticated = AtomicBoolean(false)
        val queue: BlockingQueue<ResponseEntity<*>> = LinkedBlockingQueue<ResponseEntity<*>>()
        merchantRepository!!.findByEmail(email)?.subscribe(
                { value ->
                    if (value != null) {
                        authenticated.set(password == value.password)
                    }
                    if (authenticated.get()) {
                        queue.put(ResponseEntity.ok("AUTHENTICATED : $email"))
                    } else {
                        LOG.debug("UNAUTHORIZED:$email")
                        queue.put(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password"))
                    }
                }
        ) {
            error: Throwable ->
                LOG.error("Exception", error)
        }
        var response = queue.poll(10, TimeUnit.SECONDS)
        return response ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email")
    }

    @PostMapping(value = ["/login"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@ModelAttribute("merchant") merchant: Merchant): ResponseEntity<*>? {
        return merchant.password?.let { merchant.email?.let { it1 -> login(it1, it) } }
    }

    @GetMapping("/flux")
    fun findAll(): Flux<Merchant?> {
        return merchantRepository!!.findAll()
    }

    @GetMapping("/mono/name/{registeredName}")
    fun findByRegisteredName(@PathVariable registeredName: String?): Mono<Merchant?>? {
        return merchantRepository!!.findByRegisteredName(registeredName)
    }

    @GetMapping("/mono/email/{email}")
    fun findByEmail(@PathVariable email: String?): Mono<Merchant?>? {
        return merchantRepository!!.findByEmail(email)
    }
}