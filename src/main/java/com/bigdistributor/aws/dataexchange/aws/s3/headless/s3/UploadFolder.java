package com.bigdistributor.aws.dataexchange.aws.s3.headless.s3;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;

import java.io.File;
import java.io.IOException;

public class UploadFolder {
    private final static String folder = "/Users/Marwan/Desktop/WingProject/data/raw";

    public static void main(String[] args) throws IllegalAccessException, InterruptedException, IOException {
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);

        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.US_EAST_1, "wing-project-data","");

        File file = new File(folder);
//        for (File f : file.listFiles())
        S3BucketInstance.get().upload(file);
    }


}
