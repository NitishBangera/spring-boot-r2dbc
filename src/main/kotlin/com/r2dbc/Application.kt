package com.r2dbc

import com.r2dbc.repository.MerchantRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

@SpringBootApplication
open class Application {
    private val LOG = LoggerFactory.getLogger(Application::class.java)
    @Bean
    open fun seeder(client: DatabaseClient, repository: MerchantRepository): ApplicationRunner {
        return ApplicationRunner { args: ApplicationArguments? ->
            schema
                    .flatMap { sql: String -> executeSql(client, sql) }
                    .doOnSuccess { count: Int? -> LOG.info("Schema created, rows updated: {}", count) }
        }
    }

    private fun executeSql(client: DatabaseClient, sql: String): Mono<Int?> {
        return client.execute(sql).fetch().rowsUpdated()
    }

    @get:Throws(URISyntaxException::class)
    private val schema: Mono<String>
        get() {
            val path = Paths.get(ClassLoader.getSystemResource("schema.sql").toURI())
            return Flux
                    .using({ Files.lines(path) }, { s: Stream<String>? -> Flux.fromStream(s) }) { obj: Stream<String> -> obj.close() }
                    .reduce { line1: String, line2: String ->
                        """
     $line1
     $line2
     """.trimIndent()
                    }
        }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}