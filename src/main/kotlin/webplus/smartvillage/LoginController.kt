package webplus.smartvillage


import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import webplus.smartvillage.JwtInterceptor.Companion.createJwt
import java.util.*

@RestController
class LoginController {

    @PostMapping("/api/login")
    fun login(@RequestBody credentials: Credentials): String {
        // 验证用户名和密码
        if (isValidUser(credentials.username, credentials.password)) {
            // 生成 JWT
            return createJwt(JwtInterceptor(),credentials.username)
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
