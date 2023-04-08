package webplus.smartvillage

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

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
        // 在此处可以实现验证逻辑，例如从数据库中检索用户等
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

}

data class Credentials(val username: String, val password: String)
