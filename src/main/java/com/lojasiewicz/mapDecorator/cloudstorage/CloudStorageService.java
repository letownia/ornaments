package com.lojasiewicz.mapDecorator.cloudstorage;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class CloudStorageService implements InitializingBean {

    @Autowired
    private Environment env;
    private static final String bucketNameProperty = "com.lojasiewicz.mapDecorator.bucketName";

    private static final int maxAttemptCount = 5;
    // Taken from https://github.com/googleapis/googleapis/blob/master/google/rpc/code.proto */
    private static final int CONDITION_NOT_MET = 412;
    private static final String FILE_TYPE = ".png";
    private Bucket mapDecoratorBucket;
    private Storage googleCloudStorage;
    public void  afterPropertiesSet(){
        googleCloudStorage = StorageOptions.getDefaultInstance().getService();
        Page<Bucket> bucketPage = googleCloudStorage.list(Storage.BucketListOption.prefix(env.getProperty(bucketNameProperty)));
        mapDecoratorBucket = bucketPage.getValues().iterator().next();
    }

    /**
     * Uploads a blob to google-cloud-storage and returns the potentially modified file-name.
     *
     * @param fileName potentially modified if blob already exists
     * @param imageDataURL photo to upload
     * @return
     * @throws IOException
     */
    public String addBlob(String fileName, String imageDataURL) throws IOException {
        int attemptCount = 0;
        String suffix = "";
        byte[] imageData = DatatypeConverter.parseBase64Binary(imageDataURL.substring(imageDataURL.indexOf(",") + 1));

        while(attemptCount < maxAttemptCount){
            try{
                mapDecoratorBucket.create(fileName + suffix + FILE_TYPE, new ByteArrayInputStream(imageData), "image/png", Bucket.BlobWriteOption.doesNotExist());
            }catch(StorageException e){
                if(e.getCode() != CONDITION_NOT_MET && ! e.isRetryable()){
                    throw e;
                }else{
                    attemptCount++;
                    suffix = "" + attemptCount;
                    continue;
                }
            }
            break;
        };
        return fileName + suffix + FILE_TYPE;
    }
}