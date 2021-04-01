package com.bigdistributor.aws.dataexchange.aws.s3.func.bucket;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.*;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class S3BucketInstance {

    private static S3BucketInstance instance;
    private AmazonS3 s3;
    private String bucketName;
    private final String path;

    private S3BucketInstance(AmazonS3 s3client, String bucket,String path) {
        this.s3 = s3client;
        this.path = path;
        this.bucketName = bucket;
    }

    public synchronized static S3BucketInstance get() throws IllegalAccessException {
        if (instance == null) {
            throw new IllegalAccessException("Init S3 client instance before request!");
        }
        return instance;
    }

    public static S3BucketInstance init(AWSCredentials credentials, Regions region, String bucket,String path) {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
        instance = new S3BucketInstance(s3client, bucket,path);
        return instance;
    }

    public static AmazonS3 initS3(AWSCredentials credentials, Regions region) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public void upload(File file) throws IOException, InterruptedException {
        upload(file, "");
    }

    public void upload(File file, String path) throws IOException, InterruptedException {
        if (!file.exists())
            throw new IOException(file.getAbsolutePath() + " not exist ! ");
        System.out.println("uploading " + file.getAbsolutePath());
        if (file.isDirectory())
            uploadFolder(file, path);
        else if (file.isFile())
            uploadFile(file, path);
        System.out.println("Complete");
    }

    public void createBucket() {
        s3.createBucket(bucketName);
    }

    public void showAll() throws IllegalAccessException {
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }
    }

    public AmazonS3 getS3() {
        return s3;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void uploadFile(File file, String path) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
        String filePath = (path == "") ? file.getName() : new File(path, file.getName()).getPath();
        Upload upload = tm.upload(bucketName, filePath, file);
        upload.waitForCompletion();
    }

    public void uploadFolder(File file, String path) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
        String filePath = (path == "") ? file.getName() : new File(path, file.getName()).getPath();
        MultipleFileUpload upload = tm.uploadDirectory(bucketName, filePath, file, true);
        upload.waitForCompletion();
    }

    public void uploadFolder(File file) throws InterruptedException {
        uploadFolder(file, "");
    }

    public void uploadFile(File file) throws InterruptedException {
        uploadFile(file, "");
    }

    public File download(File localFolder, String file, String path) {
        try {
            TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
            String filePath = path.isEmpty() ? file : new File(path, file).getPath();
            System.out.println("File path: " + filePath);
            File localFile = new File(localFolder, file);
            System.out.println("Local file: " + localFile);
            Download upload = tm.download(bucketName, filePath, localFile);
            upload.waitForCompletion();
            return localFile;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File: " + file + " not found!");
        }
        return null;
    }

    public void downloadFolder(File localFolder, String file, String path) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
        String filePath = path.isEmpty() ? file : new File(path, file).getPath();
        MultipleFileDownload upload = tm.downloadDirectory(bucketName, filePath, localFolder);
        upload.waitForCompletion();
    }

    public void downloadFrom(File outputFolder, String path, String[] files) throws InterruptedException, IOException {
        if (!outputFolder.exists())
            outputFolder.mkdirs();
        for (String f : files) {
            downloadFolder(outputFolder, f, path);
            File f1 = new File(new File(outputFolder, path), f);
            File f2 = new File(outputFolder, f);
            if (!f1.getAbsolutePath().equals(f2.getAbsolutePath()))
                Files.move(f1, f2);
        }
        new File(outputFolder, path).delete();
    }

    public String getPath() {
        return path;
    }
}
