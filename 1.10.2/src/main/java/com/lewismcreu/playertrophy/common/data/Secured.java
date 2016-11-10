/**
 * 
 */
package com.lewismcreu.playertrophy.common.data;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author Lewis_McReu
 */
public @interface Secured
{
	Right value();
}
