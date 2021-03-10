package com.bigdistributor.aws.dataexchange.aws.s3.headless.s3;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.dataexchange.utils.AWS_DEFAULT;

public class ListFiles {
    public static void main(String[] args) {
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, "bigstitcher");


    }
}
