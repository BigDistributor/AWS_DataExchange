package com.bigdistributor.aws.dataexchange.aws.s3.headless.s3;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;

import java.io.File;

public class DownloadFiles {
    public static void main(String[] args) throws IllegalAccessException, InterruptedException {
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        File folder = new File("/Users/Marwan/Desktop/down");
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, AWS_DEFAULT.bucket_name);
        File file = new File(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        S3BucketInstance.get().downloadFolder(folder,"dooc/", "data/");
    }
}
