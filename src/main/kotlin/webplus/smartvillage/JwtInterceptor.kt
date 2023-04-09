package webplus.smartvillage
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.web.servlet.HandlerInterceptor
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.*

class JwtInterceptor : HandlerInterceptor {


    fun createJwt(username: String): String {

        val claims: Claims = Jwts.claims().setSubject(username)
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val expMillis = nowMillis + 1000 * 60 * 60 * 24 // 1 day
        val exp = Date(expMillis)
        claims["exp"] = exp
        claims["iat"] = now
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }



    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("Authorization")?.replace("Bearer ", "")
        if (token != null) {
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
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