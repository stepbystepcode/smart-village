package webplus.smartvillage

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
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
            return createJwt(credentials.username)
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
            Jwts.parserBuilder()
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
