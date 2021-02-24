package com.bigdistributor.aws.dataexchange.aws.s3.func.read;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AWSReader {
    protected final S3BucketInstance bucketInstance;
    protected final String path;
    protected final String fileName;

    public AWSReader(S3BucketInstance bucketInstance, String path, String fileName) {
        this.bucketInstance = bucketInstance;
        this.path = path;
        this.fileName = fileName;
    }

    public String get() throws IOException {
        GetObjectRequest request = new GetObjectRequest(bucketInstance.getBucketName(), path + fileName);
        System.out.println("Getting file: "+request.getKey() + " from bucket "+ request.getBucketName());
        S3Object object = bucketInstance.getS3().getObject(request);
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
