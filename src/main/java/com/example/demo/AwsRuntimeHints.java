package com.example.demo;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import lombok.SneakyThrows;
import net.logstash.logback.pattern.EnhancedPropertyConverter;
 
public class AwsRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	@SneakyThrows
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

		// Register resources
		hints.resources().registerPattern("io/awspring/cloud/core/SpringCloudClientConfiguration.properties");
		hints.reflection().registerConstructor(CustomEntity.class.getDeclaredConstructor(), ExecutableMode.INVOKE);
		 
	
		hints.reflection().registerConstructor(EnhancedPropertyConverter.class.getDeclaredConstructor(), ExecutableMode.INVOKE);
		
	}
}