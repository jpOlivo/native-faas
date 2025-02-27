package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(AwsRuntimeHints.class)
public class AwsRuntimeHintsConfig {
}
