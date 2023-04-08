package webplus.smartvillage

import org.springframework.context.annotation.Configuration
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.bson.Document
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.bind.annotation.*
import java.util.*


@Configuration

class MongoConfig(private val environment: Environment) {
    @Bean
    fun mongoClient(): MongoClient {
        val mongoUri: String = environment.getProperty("spring.data.mongodb.uri").toString()
        return MongoClients.create(mongoUri)
    }

    @RestController
    class DataController(val mongoTemplate: MongoTemplate) {
        @GetMapping("/api/data/{collection}")
        fun getData(@PathVariable collection: String): List<Document>{
            val query = Document()
            return mongoTemplate.getCollection(collection).find(query).toList()
        }
    }


    @RestController
    class LoginController {

        @PostMapping("/api/login")
        fun login(@RequestBody credentials: Credentials): String {
            // 验证用户名和密码
            if (isValidUser(credentials.username, credentials.password)) {
                // 生成 JWT
                val jwt = createJwt(credentials.username)
                return jwt
            } else {
                throw Exception("无效的凭据")
            }
        }

        private fun isValidUser(username: String, password: String): Boolean {

            // 如果未找到对应的用户，则认为该用户不合法
            return username == "admin" && password == "WszHSf@j9JUee&"
        }
        private fun createJwt(username: String): String {
            val key = Keys.secretKeyFor(SignatureAlgorithm.HS256) // 使用推荐的方法生成安全的密钥
            val now = Date()
            val expiration = Date(now.time + 3600000) // 过期时间为当前时间加上1小时
            return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact()
        }

        fun validateJwt(jwt: String): Boolean {
            val secretKey = "mySecretKey".toByteArray() // 秘钥
            return try {
                val claims: Claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .body
                true
            } catch (e: Exception) {
                false
            }
        }

    }

    data class Credentials(val username: String, val password: String)
}