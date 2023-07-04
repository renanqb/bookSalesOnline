package com.renan.booksalesonline.adapters.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.renan.booksalesonline.adapters.configuration.model.AwsConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Bean
    public AmazonS3 amazonS3(@Autowired AwsConfigProperties awsConfigProperties) {

        var awsRegion = awsConfigProperties.awsRegion;
        var awsS3Endpoint = awsConfigProperties.awsS3Endpoint;
        var awsCredentials = new BasicAWSCredentials(awsConfigProperties.awsUser, awsConfigProperties.awsPass);
        var endpoint = new AwsClientBuilder.EndpointConfiguration(awsS3Endpoint, awsRegion);
        var s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withPathStyleAccessEnabled(true)
                .build();

        return s3Client;
    }
}
