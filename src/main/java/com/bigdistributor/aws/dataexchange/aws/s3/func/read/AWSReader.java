package com.bigdistributor.aws.dataexchange.aws.s3.func.read;

import com.amazonaws.services.s3.AmazonS3;
import com.bigdistributor.aws.dataexchange.aws.s3.S3Utils;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;

import java.io.IOException;


public class AWSReader {
    protected final String uri;
    protected final AmazonS3 s3;

    public AWSReader(AmazonS3 s3, String uri) {
        this.s3 = s3;
        this.uri = uri;
    }

    @Deprecated
    public AWSReader(S3BucketInstance bucketInstance, String path, String fileName) {
    this(bucketInstance.getS3(),format(bucketInstance.getBucketName(),path,fileName) );
    }

    private static String format(String bucketName, String path, String fileName) {
        String uri = "s3://"+bucketName;
        String folder = (path =="")?"":(path.endsWith("/")?path:path+"/");
        uri = uri+folder+folder;
        return uri;
    }

    public String get() throws IOException {
        return S3Utils.get(s3,uri);
    }
}

