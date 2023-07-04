package com.renan.booksalesonline.adapters.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.renan.booksalesonline.application.ports.out.storage.StorageService;
import com.renan.booksalesonline.domain.PublicationImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class S3StorageService implements StorageService {

    private AmazonS3 amazonS3Client;

    public S3StorageService(@Autowired AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public String save(String bucket, PublicationImage publicationImage) throws IOException, URISyntaxException {

        var requestMetadata = new ObjectMetadata();
        requestMetadata.addUserMetadata("original-contentName", publicationImage.getImageContent().getContentName());
        requestMetadata.setContentType(publicationImage.getImageContent().getContentExtension().getContentType());
        requestMetadata.setContentLength(publicationImage.getImageContent().getContentLength());
        requestMetadata.setLastModified(publicationImage.getImageContent().getContentDate());

        var putRequest = new PutObjectRequest(
                bucket,
                publicationImage.getName(),
                publicationImage.getImageContent().getContentStream(),
                requestMetadata
        );

        amazonS3Client.putObject(putRequest);

        return amazonS3Client
                .getUrl(bucket, publicationImage.getName())
                .toURI()
                .toString();
    }
}
