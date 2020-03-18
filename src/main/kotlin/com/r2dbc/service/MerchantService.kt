package com.r2dbc.service

import com.r2dbc.model.LoginResponse
import com.r2dbc.repository.MerchantRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@Service
class MerchantService(private val merchantRepository: MerchantRepository) {
    private val LOG = LoggerFactory.getLogger(MerchantService::class.java)

    fun login(email: String, password: String): LoginResponse {
        val authenticated = AtomicBoolean(false)
        val queue = LinkedBlockingQueue<LoginResponse>()
        merchantRepository.findByEmail(email)?.subscribe(
                { value ->
                    if (value != null) {
                        authenticated.set(password == value.password)
                    }
                    if (authenticated.get()) {
                        queue.put(LoginResponse(message="AUTHENTICATED : $email"))
                    } else {
                        LoginResponse(HttpStatus.UNAUTHORIZED, "Invalid password")
                        queue.put(LoginResponse(HttpStatus.UNAUTHORIZED, "Invalid password"))
                    }
                }
        ) {
            error: Throwable ->
            LOG.error("Exception", error)
        }
        val response = queue.poll(10, TimeUnit.SECONDS)
        return response ?: LoginResponse(HttpStatus.UNAUTHORIZED, "Invalid email")
    }
}