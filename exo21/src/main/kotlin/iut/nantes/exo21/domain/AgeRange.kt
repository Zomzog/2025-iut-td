package iut.nantes.exo21.domain

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraints.Min
import kotlin.reflect.KClass

data class AgeRange(val minAge: Int?, val maxAge: Int?)

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AgeRangeValidator::class])
annotation class ValidAgeRange(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
class AgeRangeValidator: ConstraintValidator<ValidAgeRange, AgeRange> {

    override fun isValid(
        value: AgeRange?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return value == null ||
                (value.minAge == null || value.minAge >= 0) &&
                (value.maxAge == null || value.maxAge >= 0) &&
                (value.minAge == null || value.maxAge == null || value.minAge <= value.maxAge)    }
}