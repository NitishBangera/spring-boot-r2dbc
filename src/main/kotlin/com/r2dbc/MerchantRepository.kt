package com.r2dbc

import org.springframework.data.r2dbc.repository.query.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MerchantRepository : ReactiveCrudRepository<Merchant?, Long?> {
    @Query("SELECT * FROM merchant m WHERE m.registeredName = :registeredName")
    fun findByRegisteredName(registeredName: String?): Mono<Merchant?>?

    @Query("SELECT * FROM merchant m WHERE m.email = :email")
    fun findByEmail(email: String?): Mono<Merchant?>?
}