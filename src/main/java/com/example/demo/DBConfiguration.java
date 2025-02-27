package com.example.demo;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkTelemetry;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DBConfiguration {

	@Bean
	public TableSchema<CustomEntity> myEntityTableSchema() {
		// create and return a TableSchema object for the MyEntity class
		return StaticTableSchema.builder(CustomEntity.class).newItemSupplier(CustomEntity::new)
				.addAttribute(String.class,
						a -> a.name("id").getter(CustomEntity::getId).setter(CustomEntity::setId)
								.tags(StaticAttributeTags.primaryPartitionKey()))
				.addAttribute(String.class,
						a -> a.name("title").getter(CustomEntity::getTitle).setter(CustomEntity::setTitle))
				.build();
	}

	@Autowired
	private OpenTelemetry openTelemetry;

	@Value("${aws.dynamodb.accessKey}")
	private String accessKey;

	@Value("${aws.dynamodb.secretKey}")
	private String secretKey;

	@Value("${aws.dynamodb.endpoint}")
	private String endpoint;
	

	@Bean
	public DynamoDbClient getDynamoDbClient() {
		
		return DynamoDbClient.builder().endpointOverride(URI.create(endpoint)).region(Region.US_EAST_1)
				//.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.overrideConfiguration(ClientOverrideConfiguration.builder()
					    .addExecutionInterceptor(AwsSdkTelemetry.create(openTelemetry).newExecutionInterceptor())
					    .build())
				.build();
	}

	@Bean
	public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(getDynamoDbClient()).build();
	}
	
	//TODO: set the db clients to dynamodbtemplate

}
