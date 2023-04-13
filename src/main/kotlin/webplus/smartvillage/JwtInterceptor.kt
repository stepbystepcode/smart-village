package webplus.smartvillage

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

class JwtInterceptor(@Value("\${myapp.property}") val myProperty: String): HandlerInterceptor {
    fun createJwt(username: String): String {

        val claims: Claims = Jwts.claims().setSubject(username)
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        //val expMillis = nowMillis + 1000 * 60 * 60 * 24 // 1 day
        //val exp = Date(expMillis)
        //claims["exp"] = exp
        claims["iat"] = now
        val keyBytes = myProperty.toByteArray(Charsets.UTF_8)
        val key = Keys.hmacShaKeyFor(keyBytes)
        return Jwts.builder()
            .setClaims(claims)
            .signWith(key)
            .compact()
    }

    fun tokenToName(token:String? /*request: HttpServletRequest, response: HttpServletResponse, handler: Any*/): String {
        //val token = request.getHeader("Authorization")?.replace("Bearer ", "")
        if (token != null) {

                val keyBytes = myProperty.toByteArray(Charsets.UTF_8)
                val key = Keys.hmacShaKeyFor(keyBytes)
                val res = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                return res.body["sub"] as String

        }
        return "error"
    }


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("Authorization")?.replace("Bearer ", "")
        if (token != null) {
            try {
                val keyBytes = myProperty.toByteArray(Charsets.UTF_8)
                val key = Keys.hmacShaKeyFor(keyBytes)
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                return true
            } catch (e: Exception) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token")
        }
        return false
    }
}
