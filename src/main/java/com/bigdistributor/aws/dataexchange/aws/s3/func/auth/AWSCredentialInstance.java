package com.bigdistributor.aws.dataexchange.aws.s3.func.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.bigdistributor.aws.dataexchange.Log;
import com.bigdistributor.aws.utils.AWS_DEFAULT;

import java.lang.invoke.MethodHandles;

public class AWSCredentialInstance {
    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static AWSCredentials instance;


    public static boolean isInitiated() {
        return instance != null;
    }

    private AWSCredentialInstance(AWSCredentials credentials) {
        instance = credentials;
    }


    public static synchronized AWSCredentials get() {
        if (instance == null) {
            logger.error("Credential file not available, set default");
//            throw new IllegalAccessException("Init credential before!");
        }
        return instance;
    }

    public static synchronized AWSCredentialInstance initFromText(String text) {
        System.out.println("Cred Text" + text);
        AWSCredentials cred = AWSCredentialsReader.getFromText(text);
        return new AWSCredentialInstance(cred);
    }

    public static synchronized AWSCredentialInstance initWithKey(String publicKey, String privateKey) {
        AWSCredentials credentials = new BasicAWSCredentials(
                publicKey, privateKey
        );
        return new AWSCredentialInstance(credentials);
    }

    public static synchronized AWSCredentialInstance init(String path) {
        AWSCredentials credentials;
        try {
            AWSCredentialsReader reader = new AWSCredentialsReader(path);
            credentials = reader.getCredentials();
        } catch (Exception e) {
            logger.error("No credentials evaluable, set default");
            credentials = new ProfileCredentialsProvider().getCredentials();
        }
        return new AWSCredentialInstance(credentials);
    }

    public static void main(String[] args) throws IllegalAccessException {
        AWSCredentials credentials = AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH).get();
        System.out.println("key: " + credentials.getAWSAccessKeyId());
    }

}
