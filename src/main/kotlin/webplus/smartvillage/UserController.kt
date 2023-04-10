package webplus.smartvillage


import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import org.springframework.security.crypto.password.PasswordEncoder

@RestController
class UserController(@Value("\${myapp.property}") val myProperty: String,private val passwordEncoder: PasswordEncoder,val mongoTemplate: MongoTemplate) {


    @PostMapping("/api/login")
    fun login(@RequestBody credentials: Credentials): String {
        // 验证用户名和密码
        if (isValidUser(credentials.username, credentials.password)) {
            // 生成 JWT
            return JwtInterceptor(myProperty).createJwt(credentials.username)
        } else {
            throw Exception("用户名或密码错误")
        }
    }

    @PostMapping("/api/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {

        // Check if user already exists
        val usernameQuery = Document("username", signUpRequest.username)
        val usernameResult = mongoTemplate.getCollection("user").find(usernameQuery).toList()
        if (usernameResult.size == 1) return ResponseEntity.badRequest().body("用户名 '${signUpRequest.username}' 已经被注册。")
        val phoneQuery = Document("phone", signUpRequest.phone)
        val phoneResult = mongoTemplate.getCollection("user").find(phoneQuery).toList()
        if (phoneResult.size == 1) return ResponseEntity.badRequest().body("手机号 '${signUpRequest.phone}' 已经被注册。")

        // Create new user
        signUpRequest.password=passwordEncoder.encode(signUpRequest.password)
        mongoTemplate.save(signUpRequest,"user")
        // Return success response
        return ResponseEntity.ok("注册成功！")
    }

    private fun isValidUser(username: String, password: String): Boolean {
        val query = Document("username", username)
        val result = mongoTemplate.getCollection("user").find(query).toList()
        return result.size == 1 && passwordEncoder.matches(password,result[0].getString("password"))
    }


}

data class Credentials(val username: String, val password: String)
data class SignUpRequest(
    val username: String,
    var password: String,
    val phone: String
)
