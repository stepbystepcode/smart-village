package webplus.smartvillage

import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*
@RestController
@CrossOrigin("*")
@RequestMapping("/api/data/{collection}")
class DataController(val mongoTemplate: MongoTemplate) {
    @GetMapping("/")
    fun getData(@PathVariable collection: String): List<Document>{
        val query = Document()
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
    @GetMapping("/{id}")
    fun getOne(@PathVariable collection: String,@PathVariable id: Number): List<Document>{
        val query = Document("id",id)
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
}
