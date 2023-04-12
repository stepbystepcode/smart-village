package webplus.smartvillage

import jakarta.servlet.http.HttpServletRequest
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/data/{collection}")
class DataController(val mongoTemplate: MongoTemplate,@Value("\${myapp.property}") val myProperty: String) {
    @CrossOrigin("*")
    @GetMapping("/")
    fun getData(@PathVariable collection: String): List<Document>{
        val query = Document()
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
    @CrossOrigin("*")
    @GetMapping("/{goodId}")
    fun getOne(
        @PathVariable collection: String,
        @PathVariable goodId: Long,
        @RequestParam(required = false, defaultValue = "history") type: String,
        request: HttpServletRequest
    ): List<Document> {
        if (request.getHeader("Authorization") != null) {
            val token = request.getHeader("Authorization")?.replace("Bearer ", "")
            val interceptor = JwtInterceptor(myProperty)
            val username = interceptor.tokenToName(token)
            val shopController = ShopController(mongoTemplate )
            shopController.addItem(goodId,username,type)
        }
        val query = Document("id", goodId)
        return mongoTemplate.getCollection(collection).find(query).toList()
    }
}
