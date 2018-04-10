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

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PhotoStorageService implements InitializingBean {

    @Autowired
    private Environment env;
    private static final String bucketNameProperty = "com.lojasiewicz.mapDecorator.bucketName";

    public static final int THUMBNAIL_MAX_WIDTH = 160;
    public static final int THUMBNAIL_MAX_HEIGHT = 120;
    private static final int maxAttemptCount = 5;
    // Taken from https://github.com/googleapis/googleapis/blob/master/google/rpc/code.proto */
    private static final int CONDITION_NOT_MET = 412;
    private static final String FILE_TYPE = "jpg";
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
     * @param imageData photo to upload
     * @return
     * @throws IOException
     */
    public String saveMedium(String fileName, byte[] imageData) throws IOException {
        return savePhoto(fileName,imageData);
    }
    public String createAndSaveThumbnail(String fileName, byte[] imageData) throws IOException {
        /* Determining newWidth/Height */
        BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageData));
        int newWidth = inputImage.getWidth();
        int newHeight = inputImage.getHeight();
        if (newWidth > THUMBNAIL_MAX_WIDTH) {
            newHeight *= (0.0 + THUMBNAIL_MAX_WIDTH) / newWidth;
            newWidth = THUMBNAIL_MAX_WIDTH;
        }
        if (newHeight > THUMBNAIL_MAX_HEIGHT) {
            newWidth *= (0.0 + THUMBNAIL_MAX_HEIGHT) / newHeight;
            newHeight = THUMBNAIL_MAX_HEIGHT;
        }
        /*Creating new BufferedImage */
        Image temp = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        int type = inputImage.getTransparency() == Transparency.OPAQUE ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight,type);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        /*Writing new image to byteStream */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newImage, FILE_TYPE, baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return savePhoto(fileName, imageInByte);
    }


    private String savePhoto(String fileName, byte[] imageData){
        int attemptCount = 0;
        String suffix = "";
        String pathName = "";
        while (attemptCount < maxAttemptCount) {
            try {
                pathName = fileName + suffix + "." + FILE_TYPE;
                mapDecoratorBucket.create(pathName, new ByteArrayInputStream(imageData), "image/" + FILE_TYPE, Bucket.BlobWriteOption.doesNotExist());
            } catch (StorageException e) {
                if (e.getCode() != CONDITION_NOT_MET && !e.isRetryable()) {
                    throw e;
                } else {
                    attemptCount++;
                    suffix = "" + attemptCount;
                    continue;
                }
            }
            break;
        }
        return pathName;
    }

    private static Image prepareThumbnailImage(BufferedImage original){
        int newWidth = original.getWidth();
        int newHeight = original.getHeight();
        if (newWidth > THUMBNAIL_MAX_WIDTH) {
            newHeight *= ( 0.0 + THUMBNAIL_MAX_WIDTH) / newWidth;
            newWidth = THUMBNAIL_MAX_WIDTH;
        }
        if (newHeight > THUMBNAIL_MAX_HEIGHT) {
            newWidth *= (0.0 + THUMBNAIL_MAX_HEIGHT)/ newHeight;
            newHeight = THUMBNAIL_MAX_HEIGHT;
        }
        return original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    public static final byte[] imagaDataURLtoBinary(String imageDataURL){
        return DatatypeConverter.parseBase64Binary(imageDataURL.substring(imageDataURL.indexOf(",") + 1));
    }
}