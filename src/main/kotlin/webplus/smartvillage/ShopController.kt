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
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value


@CrossOrigin("*")
@RestController
class ShopController(val mongoTemplate: MongoTemplate) {
    @Value("\${myapp.property}")
    private lateinit var myProperty:String

    @GetMapping("/api/user/item/")
    fun getItem(@RequestParam(required = false, defaultValue = "history") type: String,
                request: HttpServletRequest
                ): List<Document> {
        val token = request.getHeader("Authorization")?.replace("Bearer ", "")
        val interceptor=JwtInterceptor(myProperty)
        val username = interceptor.tokenToName(token)
        val user = mongoTemplate.getCollection("user").find(eq("username", username)).toList()
        return user[0][type] as List<Document>
    }
    fun addItem(id: Number, username: String, type: String) {
        val filter = eq("username", username)
        //val update = Updates.push("history", id)
        val update = when (type) {
            "favourite" -> {
                val favouriteList = (mongoTemplate.getCollection("user").find(filter).first()?.get("favourite") as? List<*>)?.map { it.toString().toLong() }?.toMutableList() ?: mutableListOf()
                if (favouriteList.contains(id)) {
                    favouriteList.remove(id)
                } else {
                    favouriteList.add(id as Long)
                }
                Updates.set("favourite", favouriteList)
            }
            "history" -> {
                val historyList = (mongoTemplate.getCollection("user").find(filter).first()?.get("history") as? List<*>)?.map { it.toString().toLong() }?.toMutableList() ?: mutableListOf()
                if (historyList.isNotEmpty() && historyList.last() == id) {
                    // do nothing
                } else {
                    historyList.add(id as Long)
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
