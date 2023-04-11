package webplus.smartvillage

/**
 * Date：2023/4/10  22:08
 * @version 1.0
 */
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.mongodb.client.model.Filters.eq
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestController
class ShopController(val mongoTemplate: MongoTemplate) {
    @CrossOrigin("*")
    @GetMapping("/api/user/history/{id}")
    fun getHistory(@PathVariable id: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0]["history"] as List<Document>
    }

    //    fun addHistory(id:Number){
    //      val user = mongoTemplate.getCollection("user").find(eq("_id",ObjectId(id))).toList()
    //      val filterQuery = Query(Criteria.where("_id").`is`("some_value"))
    //      val updateQuery = Update().push("history", id)
    //      mongoTemplate.updateFirst(filterQuery, updateQuery, "user")
    //    }


    @CrossOrigin("*")
    @GetMapping("/api/user/favourite/{id}")
    fun getFavourite(@PathVariable id: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0]["favourite"] as List<Document>
    }


    @CrossOrigin("*")
    @PostMapping("/api/user/history/{good}")
    fun addHistory(@PathVariable good: Number,username: String) {
        val query = Document("username", username)
        val update = Document("\$push", Document("history", good))
        mongoTemplate.getCollection("user").updateOne(query, update)
    }
    @CrossOrigin("*")
    @PostMapping("/api/user/favourite/{good}")
    fun addFavorite(@PathVariable good: Number, username: String) {
        val query = Document("username", username)
        val update = Document("\$push", Document("favorite", good))
        mongoTemplate.getCollection("user").updateOne(query, update)
    }


}
