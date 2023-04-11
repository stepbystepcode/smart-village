package webplus.smartvillage

import jakarta.servlet.http.HttpServletRequest
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*

@RestController
class DataController(val mongoTemplate: MongoTemplate, @Value("\${myapp.property}") val myProperty: String) {
    @CrossOrigin("*")
    @GetMapping("/api/data/{collection}")
    fun getData(@PathVariable collection: String): List<Document> {
        val query = Document()
        return mongoTemplate.getCollection(collection).find(query).toList()
    }

    @CrossOrigin("*")
    @PostMapping("/api/user/{collection}/{good}")
    fun postOne(
        @PathVariable collection: String,
        @PathVariable good: Number,
        @RequestParam(required = false, defaultValue = "history") type: String,//add
        request: HttpServletRequest
    ): List<Document> {
        if (request.getHeader("Authorization") != null) {
            val token = request.getHeader("Authorization")?.replace("Bearer ", "")
            val interceptor = JwtInterceptor(myProperty)
            val username = interceptor.tokenToName(token)

            //创建 ShopController 实例
            val shopController = ShopController(mongoTemplate )

            // 根据类型选择调用的方法
            when (type) {
                "history" -> shopController.addHistory(good, username)
                "favorite" -> shopController.addFavorite(good, username)
                else -> throw IllegalArgumentException("Invalid request type: $type")
            }
            //调用 addHistory 方法
            //shopController.addHistory(good, username)
        }
        val query = Document("id", good)
        return mongoTemplate.getCollection(collection).find(query).toList()
    }

}
