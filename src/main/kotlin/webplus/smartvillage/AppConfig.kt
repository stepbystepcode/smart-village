package webplus.smartvillage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig(@Value("\${myapp.property}") val myProperty: String) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        val jwtInterceptor = JwtInterceptor(myProperty) // create a new instance of JwtInterceptor
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/data/**")
    }
}