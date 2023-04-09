package webplus.smartvillage


import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LoginController(@Value("\${myapp.property}") val myProperty: String) {

    @PostMapping("/api/login")
    fun login(@RequestBody credentials: Credentials): String {
        // 验证用户名和密码
        if (isValidUser(credentials.username, credentials.password)) {
            // 生成 JWT
            return JwtInterceptor(myProperty).createJwt(credentials.username)
        } else {
            throw Exception("无效的凭据")
        }
    }

    private fun isValidUser(username: String, password: String): Boolean {

        // 如果未找到对应的用户，则认为该用户不合法
        return username == "admin" && password == "WszHSf@j9JUee&"
    }


}

data class Credentials(val username: String, val password: String)
