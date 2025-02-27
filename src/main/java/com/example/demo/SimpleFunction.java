package com.example.demo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.AWSLambdaUtils;
import org.springframework.messaging.Message;

import com.amazonaws.services.lambda.runtime.Context;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.java.Log;

@Log
//public class SimpleFunction implements Function<String, String> {
public class SimpleFunction implements Function<Message<String>, String> {

	private MeterRegistry registry;

	@Autowired
	private SomeService service;

	public SimpleFunction(MeterRegistry registry) {
		this.registry = registry;
	}

	@Override
	@Observed(name = "applyFunction", contextualName = "applyFunction")
	//public String apply(String s) {
	public String apply(Message<String> s) {

		//Context c = s.getHeaders().get(AWSLambdaUtils.AWS_CONTEXT, Context.class);
		//log.info(c.getInvokedFunctionArn());
		
		String message = "Hello " + s + ", and welcome to Spring Cloud Function!!!";
		log.info(message);

		// Custom metric
		computeMetric();

		// instrument db operation on new span
		service.callToExternalService();

		// instrument db operation on new span
		service.performOperationOnDB();

		log.info("Finish apply");

		return message;
	}
	

	private void computeMetric() {
		Counter counter = Counter.builder("custom.metric")
								.description("indicates instance count of the object")
								.tag("version", "v1")
								.tag("country", "MX")
								.register(registry);

		counter.increment(ThreadLocalRandom.current().nextInt(0, 5));

		counter.increment();
	}

}