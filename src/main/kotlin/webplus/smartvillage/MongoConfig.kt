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
    @GetMapping("/api/user/item/{id}")
    fun getItem(@PathVariable id: String,@RequestParam(required = false, defaultValue = "history") type: String): List<Document> {
        val user = mongoTemplate.getCollection("user").find(eq("_id", ObjectId(id))).toList()
        return user[0][type] as List<Document>
    }
    @CrossOrigin("*")
    @PostMapping("/api/user/item/{id}")
    fun addItem(@PathVariable id: Long, username: String?, @RequestParam(required = false, defaultValue = "history") type: String) {
        val filter = eq("username", username)

        val update = when (type) {
            "favourite" -> {
                val favouriteList = (mongoTemplate.getCollection("user").find(filter).first()?.get("favourite") as? List<*>)?.map { it.toString().toLong() }?.toMutableList() ?: mutableListOf()
                if (favouriteList.contains(id)) {
                    favouriteList.remove(id)
                } else {
                    favouriteList.add(id)
                }
                Updates.set("favourite", favouriteList)
            }
            "history" -> {
                val historyList = (mongoTemplate.getCollection("user").find(filter).first()?.get("history") as? List<*>)?.map { it.toString().toLong() }?.toMutableList() ?: mutableListOf()
                if (historyList.isNotEmpty() && historyList.last() == id) {
                    // do nothing
                } else {
                    historyList.add(id)
                }
                Updates.set("history", historyList)
            }
            else -> throw IllegalArgumentException("Invalid type parameter value")
        }

        val options = FindOneAndUpdateOptions()
            .returnDocument(ReturnDocument.AFTER)
        val collection = mongoTemplate.getCollection("user")
        val result: Document? = collection.findOneAndUpdate(filter, update, options)
        if (result != null) {
            println(result.toJson())
        }
    }
}