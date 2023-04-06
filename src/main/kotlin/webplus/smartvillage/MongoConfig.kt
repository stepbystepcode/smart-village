package webplus.smartvillage

import org.springframework.context.annotation.Configuration
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.Document
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Configuration

class MongoConfig(private val environment: Environment) {
    @Bean
    fun mongoClient(): MongoClient {
        val mongoUri: String = environment.getProperty("spring.data.mongodb.uri").toString()
        return MongoClients.create(mongoUri)
    }

    @RestController
    @RequestMapping("/seed")
    class SeedController(val mongoTemplate: MongoTemplate) {
        @GetMapping
        fun getSeed(): List<Document> {
            val query = Document()
            return mongoTemplate.getCollection("seed").find(query).toList()
        }
    }
}