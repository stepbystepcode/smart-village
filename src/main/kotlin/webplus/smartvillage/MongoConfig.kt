package webplus.smartvillage

import org.springframework.context.annotation.Configuration
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.Document
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*


@Configuration

class MongoConfig(private val environment: Environment) {
    @Bean
    fun mongoClient(): MongoClient {
        val mongoUri: String = environment.getProperty("spring.data.mongodb.uri").toString()
        return MongoClients.create(mongoUri)
    }

    @RestController
    class DataController(val mongoTemplate: MongoTemplate) {
        @GetMapping("/api/data/{collection}")
        fun getData(@PathVariable collection: String): List<Document>{
            val query = Document()
            return mongoTemplate.getCollection(collection).find(query).toList()
        }
    }
}