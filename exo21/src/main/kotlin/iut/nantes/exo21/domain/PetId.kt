package iut.nantes.exo21.domain

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Min
import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 1)
annotation class PetId(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)