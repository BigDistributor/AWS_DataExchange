package com.bigdistributor.aws.dataexchange.aws.s3.func.read;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AWSReader {
//    protected final S3BucketInstance bucketInstance;
    protected final String uri;
    protected final AmazonS3 s3;

    public AWSReader(AmazonS3 s3, String uri) {
        this.s3 = s3;
        this.uri = uri;
    }

    public String get() throws IOException {
        AmazonS3URI amazonS3URI = new AmazonS3URI(uri);
        GetObjectRequest request = new GetObjectRequest(amazonS3URI.getBucket(),amazonS3URI.getKey());
        System.out.println("Getting file: "+request.getKey() + " from bucket "+ request.getBucketName());
        S3Object object = s3.getObject(request);
        InputStream objectData = object.getObjectContent();
        String text;
        try (Reader reader = new InputStreamReader(objectData)) {
            text = CharStreams.toString(reader);
//            System.out.println(text);
        }
        objectData.close();
//        System.out.println("Text got : " +text);
        return text;
    }
}

