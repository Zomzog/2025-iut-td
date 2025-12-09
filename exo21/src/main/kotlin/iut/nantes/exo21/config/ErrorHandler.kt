package iut.nantes.exo21.config

import iut.nantes.exo21.errors.ImATeapotException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException) = ResponseEntity.badRequest().body("Failure: ${e.message}")

    @ExceptionHandler(ImATeapotException::class)
    fun handleImATeapot(e: ImATeapotException) = ResponseEntity.status(418).body("I'm a teapot !")
    
    @ExceptionHandler(Exception::class)
    fun fallback(e: Exception) = ResponseEntity.internalServerError().body("Failure: ${e.message}")
}