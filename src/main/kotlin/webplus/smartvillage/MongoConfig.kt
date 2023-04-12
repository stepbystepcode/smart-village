package webplus.smartvillage

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment


@Configuration

class MongoConfig(private val environment: Environment) {
    @Bean
    fun mongoClient(): MongoClient {
        val mongoUri: String = environment.getProperty("spring.data.mongodb.uri").toString()
        return MongoClients.create(mongoUri)
    }

}