package com.digital.customer.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3StorageServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Utilities s3Utilities;

    @InjectMocks
    private S3StorageServiceImpl storageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageService.bucket = "tst-bucket";
        when(s3Client.utilities()).thenReturn(s3Utilities);
    }

    @Test
    void upload_withEmptyFile_shouldReturnNull() throws IOException {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        String key = storageService.upload(emptyFile);
        assertNull(key);

        verifyNoInteractions(s3Client);
    }

    @Test
    void uploadSuccessfully_shouldReturnKey() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "TEST ABC".getBytes());

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(mock(PutObjectResponse.class));

        String key = storageService.upload(file);

        assertNotNull(key);
        assertTrue(key.contains("test.txt"));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));

    }

    @Test
    void getUrl_withValidKey_shouldReturnUrl() {
        String key = "ABC-test.txt";
        URL url = mock(URL.class);
        when(url.toString()).thenReturn("http://localhost/test.txt");

        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(url);

        String result = storageService.getUrl(key);
        assertEquals("http://localhost/test.txt", result);

        verify(s3Utilities, times(1)).getUrl(any(GetUrlRequest.class));
    }

    @Test
    void getUrl_withNullKey_shouldReturnNull() throws IOException {
        String result = storageService.getUrl(null);
        assertNull(result);
        verifyNoInteractions(s3Utilities);
    }
}