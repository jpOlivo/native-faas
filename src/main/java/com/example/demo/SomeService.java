package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.java.Log;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;


@Service
@Log
public class SomeService {
	//@Autowired
	//private RestTemplate restTemplate;
	
	@Autowired
	DynamoDbTemplate dynamoDbTemplate;
	
	@Autowired
	DynamoDbClient dynamoDbClient;
	

	@Observed(name = "callToExternalService", contextualName = "callToExternalService")
	public void callToExternalService() {
		log.info("Will send a request to the server");

		//String response = restTemplate.getForObject("https://httpbin.org/get", String.class);
		

		log.info("Response from external service: fooo");

	}

	@Observed(name = "performOperationOnDB", contextualName = "performOperationOnDB_1")
	public void performOperationOnDB() {
		log.info("Performing tasks on database");
		
		//CustomEntity customEntity = new CustomEntity(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		//CustomEntity savedCustomEntity =  dynamoDbTemplate.save(customEntity);
		
		
		HashMap<String,AttributeValue> itemValues = new HashMap<String,AttributeValue>();

        // Add all content to the table
        itemValues.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
        itemValues.put("title", AttributeValue.builder().s(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("custom_entity")
                .item(itemValues)
                .build();
		
		dynamoDbClient.putItem(request);
	}
}
