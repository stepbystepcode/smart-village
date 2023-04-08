package webplus.smartvillage

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtInterceptor : HandlerInterceptor {

    val key: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256) // 使用推荐的方法生成安全的密钥
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURI == "/api/login") { // 如果是登录接口，则直接放行
            return true
        }
        val token = getTokenFromHeader(request)
        if (!token?.let { validateJwt(it) }!!) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT")
            return false
        }
        return true
    }

    private fun getTokenFromHeader(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7)
        }
        return null
    }

    fun validateJwt(jwt: String): Boolean {
        println("Validating JWT: $jwt")
        val isValid = try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .body
                .let { claims ->
                    val now = Date()
                    val subject = claims.subject
                    val expiration = claims.expiration
                    subject != null && expiration != null && now.before(expiration)
                }
        } catch (e: Exception) {
            false
        }
        println("JWT validation result: $isValid")
        return isValid
    }

    companion object {
        fun createJwt(jwtInterceptor: JwtInterceptor, username: String): String {
            println("Creating JWT for user: $username")
            val now = Date()
            val expiration = Date(now.time + 3600000) // 过期时间为当前时间加上1小时
            val token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(jwtInterceptor.key)
                .compact()
            println("Created JWT: $token")
            return token
        }
    }
}
