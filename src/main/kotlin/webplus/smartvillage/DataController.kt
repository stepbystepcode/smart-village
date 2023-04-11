package webplus.smartvillage

import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")
class DataController(val mongoTemplate: MongoTemplate) {
    @GetMapping("/api/data/{collection}")
    fun getData(@PathVariable collection: String): List<Document>{
        val query = Document()
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
    @GetMapping("/api/data/{collection}/{id}")
    fun getOne(@PathVariable collection: String,@PathVariable id: Number): List<Document>{
        val query = Document("id",id)
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
}
