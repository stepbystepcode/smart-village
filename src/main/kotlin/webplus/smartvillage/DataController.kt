package webplus.smartvillage

import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DataController(val mongoTemplate: MongoTemplate) {
    @GetMapping("/api/data/{collection}")
    fun getData(@PathVariable collection: String): List<Document>{
        val query = Document()
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
}
