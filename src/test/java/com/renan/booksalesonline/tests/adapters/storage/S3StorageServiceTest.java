package com.renan.booksalesonline.tests.adapters.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.renan.booksalesonline.adapters.storage.S3StorageService;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.PublicationImageContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class S3StorageServiceTest {

    @Mock private AmazonS3Client s3Client;
    @InjectMocks private S3StorageService s3StorageService;

    @Test
    public void shouldCreateFileInStorageSuccessfully() throws IOException, URISyntaxException {

        var putObject = new PutObjectResult();
        var url = new URL("http", "localhost", 4566, "/bucket/file.jpg");

        when(s3Client.putObject(any())).thenReturn(putObject);
        when(s3Client.getUrl(anyString(), anyString())).thenReturn(url);

        var image = new PublicationImage(
                new PublicationImageContent(
                        "file.png",
                        1000,
                        new StubInputStream(4, "test")
                ));

        var resultUrl = s3StorageService.save("bucket", image);

        assertThat(resultUrl).isEqualTo("http://localhost:4566/bucket/file.jpg");
    }
}
