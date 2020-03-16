package com.r2dbc

import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@CrossOrigin(origins = ["http://127.0.0.1:8088", "http://localhost:8088"], maxAge = 3600)
@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
class MerchantController {
    private val LOG = LoggerFactory.getLogger(MerchantController::class.java)

    @Autowired
    private val merchantRepository: MerchantRepository? = null

    //    @GetMapping("/login/{email}/{password}")
    @ResponseBody
    fun login(@PathVariable email: String, @PathVariable password: String): ResponseEntity<*> {
        LOG.debug("\nlogin, email:$email, password:$password\n")
        val found = AtomicBoolean(false)
        val authenticated = AtomicBoolean(false)
        val result = AtomicReference<ResponseEntity<*>>(ResponseEntity.notFound().build<Any>())
        merchantRepository!!.findByEmail(email)?.subscribe(
                { value ->
                    LOG.debug("\nFOUND:$value\n")
                    found.set(true)
                    if (value != null) {
                        authenticated.set(password == value.password)
                    }
                    if (authenticated.get()) {
                        LOG.debug("\nAUTHENTICATED:$email\n")
                        result.set(ResponseEntity.ok().build<Any>())
                    } else {
                        LOG.debug("\nUNAUTHORIZED:$email\n")
                        result.set(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Any>())
                    }
                }
        ) { error: Throwable ->
            error.printStackTrace()
            result.set(ResponseEntity.notFound().build<Any>())
        }
        LOG.info("Returning")
        return result.get()
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