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


@CrossOrigin("*")
@RestController
class ShopController(val mongoTemplate: MongoTemplate) {
    @GetMapping("/api/user/item/{id}")
    fun getItem(@PathVariable id: String,@RequestParam(required = false, defaultValue = "history") type: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0][type] as List<Document>
    }
    fun addItem(id: Number, username: String, type: String) {
        val filter = eq("username", username)
        val update = Updates.push("history", id)
        val options = FindOneAndUpdateOptions()
            .returnDocument(ReturnDocument.AFTER)
        val collection = mongoTemplate.getCollection("user")
        val result: Document? = collection.findOneAndUpdate(filter, update, options)
        if (result != null) {
            println(result.toJson())
        }
    }
}
