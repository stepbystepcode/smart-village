package webplus.smartvillage

/**
 * Date：2023/4/10  22:08
 * @version 1.0
 */

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*


@RestController
class ShopController(val mongoTemplate: MongoTemplate) {
    @CrossOrigin("*")
    @GetMapping("/api/user/history/{id}")
    fun getHistory(@PathVariable id: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0]["history"] as List<Document>
    }

        fun addHistory(id:Number, username: String){
            val filter = eq("username", username)
            val update = Updates.push("history", id.toString())
            val options = FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
            val collection = mongoTemplate.getCollection("user")
            val result: Document? = collection.findOneAndUpdate(filter, update, options)
            if (result != null) {
                println(result.toJson())
            }
        }
    @CrossOrigin("*")
    @GetMapping("/api/user/favourite/{id}")
    fun getFavourite(@PathVariable id: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0]["favourite"] as List<Document>
    }
    fun addFavourite(id:Number, username: String){
        val filter = eq("username", username)
        val update = Updates.push("favourite", id.toString())
        val options = FindOneAndUpdateOptions()
            .returnDocument(ReturnDocument.AFTER)
        val collection = mongoTemplate.getCollection("user")
        val result: Document? = collection.findOneAndUpdate(filter, update, options)
        if (result != null) {
            println(result.toJson())
        }
    }

//    @CrossOrigin("*")
//    @PostMapping("/api/user/history/{good}")
//    fun addHistory(@PathVariable good: Number,username: String) {
//        val query = Document("username", username)
//        val update = Document("\$push", Document("history", good))
//        mongoTemplate.getCollection("user").updateOne(query, update)
//    }
    @CrossOrigin("*")
    @PostMapping("/api/user/favourite/{good}")
    fun addFavorite(@PathVariable good: Number, username: String) {
        val query = Document("username", username)
        val update = Document("\$push", Document("favorite", good))
        mongoTemplate.getCollection("user").updateOne(query, update)
    }


}
