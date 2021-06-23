package com.bigdistributor.aws.dataexchange.aws.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.google.common.io.CharStreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class S3Utils {

    public static File download(AmazonS3 s3, File localFolder, String uri) {
        try {
            TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
            AmazonS3URI amazonS3URI = new AmazonS3URI(uri);
            System.out.println("File  " + uri);
            File localFile = new File(localFolder, amazonS3URI.getKey());
            System.out.println("Local file: " + localFile);
            Download upload = tm.download(amazonS3URI.getBucket(), amazonS3URI.getKey(), localFile);
            upload.waitForCompletion();
            return localFile;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File: " + uri + " not found!");
        }
        return null;
    }

    public static String get(AmazonS3 s3, String uri) throws IOException {
        AmazonS3URI amazonS3URI = new AmazonS3URI(uri);
        GetObjectRequest request = new GetObjectRequest(amazonS3URI.getBucket(), amazonS3URI.getKey());
        System.out.println("Getting file: " + request.getKey() + " from bucket " + request.getBucketName());
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

    public static AmazonS3 initS3(AWSCredentials credentials, Regions region) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public static void upload(AmazonS3 s3, File file, AmazonS3URI uri) throws IOException, InterruptedException {
        if (!file.exists())
            throw new IOException(file.getAbsolutePath() + " not exist ! ");
        System.out.println("uploading " + file.getAbsolutePath());
        if (file.isDirectory())
            uploadFolder(s3, file, uri);
        else if (file.isFile())
            uploadFile(s3, file, uri);
        System.out.println("Complete");
    }

    public static void createBucket(AmazonS3 s3, String bucketName) {
        s3.createBucket(bucketName);
    }

    public static void showAll(AmazonS3 s3, String bucketName) {
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }
    }

    public static void uploadFile(AmazonS3 s3, File file, AmazonS3URI uri) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
        Upload upload = tm.upload(uri.getBucket(), uri.getKey(), file);
        upload.waitForCompletion();
    }

    public static void uploadFolder(AmazonS3 s3, File file, AmazonS3URI uri) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
        MultipleFileUpload upload = tm.uploadDirectory(uri.getBucket(), uri.getKey(), file, true);
        upload.waitForCompletion();
    }

//    public void downloadFolder(AmazonS3 s3, File file, AmazonS3URI uri) throws InterruptedException {
//        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
//        MultipleFileDownload upload = tm.downloadDirectory(uri.getBucket(), uri.getKey(), file);
//        upload.waitForCompletion();
//    }
//
//    public void downloadFrom(AmazonS3 s3, File outputFolder, String path, String[] files) throws InterruptedException, IOException {
//        if (!outputFolder.exists())
//            outputFolder.mkdirs();
//        for (String f : files) {
//            downloadFolder(s3,outputFolder, f, path);
//            File f1 = new File(new File(outputFolder, path), f);
//            File f2 = new File(outputFolder, f);
//            if (!f1.getAbsolutePath().equals(f2.getAbsolutePath()))
//                Files.move(f1, f2);
//        }
//        new File(outputFolder, path).delete();
//    }

}

