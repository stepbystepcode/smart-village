package webplus.smartvillage

/**
 * Date：2023/4/10  22:08
 * Description：TODO
 *
 * @author Leon
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

@RestController
class ShopController(val mongoTemplate: MongoTemplate) {
    @CrossOrigin("*")
    @GetMapping("/api/user/history/{id}")
    fun getHistory(@PathVariable id: String): List<Document>{
        val user = mongoTemplate.getCollection("user").find(eq("_id",ObjectId(id))).toList()
        return user[0]["history"] as List<Document>
    }

    @CrossOrigin("*")
    @GetMapping("/api/user/favourite/{id}")
    fun getFavourite(@PathVariable id: String): List<Document>{
        val user = mongoTemplate.getCollection("user").find(eq("_id",ObjectId(id))).toList()
        return user[0]["favourite"] as List<Document>
    }
}
