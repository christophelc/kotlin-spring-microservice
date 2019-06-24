package com.ecommerce.microcommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class MicrocommerceApplication

// swagger: http://localhost:9090/v2/api-docs
// swagger ui: http://localhost:9090/swagger-ui.html
fun main(args: Array<String>) {
	runApplication<MicrocommerceApplication>(*args)
}
