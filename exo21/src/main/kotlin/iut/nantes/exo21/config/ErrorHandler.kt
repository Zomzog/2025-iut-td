package iut.nantes.exo21.config

import iut.nantes.exo21.errors.ImATeapotException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException) = ResponseEntity.badRequest().body("Failure: ${e.message}")

    @ExceptionHandler(ImATeapotException::class)
    fun handleImATeapot(e: ImATeapotException) = ResponseEntity.status(418).body("I'm a teapot !")
    
    @ExceptionHandler(Exception::class)
    fun fallback(e: Exception) = ResponseEntity.internalServerError().body("Failure: ${e.message}")

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatusCode, request: WebRequest): ResponseEntity<in Any>? {
        return ResponseEntity.badRequest().body(ErrorResponse(400, ex.message?: "Malformed request body"))
    }

    class ErrorResponse(
        val status: Int,
        val message: String
    )
}