package com.bigdistributor.aws.dataexchange.aws.s3.headless.s3;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;

import java.io.File;
import java.io.IOException;

public class UploadFile {
    private static final String path = "/Users/Marwan/Desktop/RS-FISH project/RS-FISH-Spark/target/RS-Fish-jar-with-dependencies.jar";

    public static void main(String[] args) throws IllegalAccessException, InterruptedException, IOException {
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.US_EAST_1, "preibischlab-release-jars", "");
        File file = new File(path);

        System.out.println(file.getAbsolutePath());
        S3BucketInstance.get().uploadFile(file, "",true);
        System.out.println("File uploaded");


    }
}
