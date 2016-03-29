package common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a method that should be logged at runtime by the Logger class.
 * Doesn't alter the method in any other way, and does nothing if
 * code insertion and a HotSwap is not performed.
 * @author Mshnik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Logged {}
