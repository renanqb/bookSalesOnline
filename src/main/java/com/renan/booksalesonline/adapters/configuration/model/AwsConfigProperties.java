package com.renan.booksalesonline.adapters.configuration.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfigProperties {

    @Value("${aws.region}") public String awsRegion;
    @Value("${aws.credentials.user}") public String awsUser;
    @Value("${aws.credentials.pass}") public String awsPass;
    @Value("${aws.s3.endpoint}") public String awsS3Endpoint;
    @Value("${aws.s3.bucket}") public String awsS3Bucket;
}
