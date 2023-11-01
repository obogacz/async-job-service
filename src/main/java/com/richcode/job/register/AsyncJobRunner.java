package com.richcode.job.register;

import com.richcode.job.dto.AsyncJobType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncJobRunner {

    AsyncJobType value();

}
