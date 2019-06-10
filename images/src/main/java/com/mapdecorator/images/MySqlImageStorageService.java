package com.mapdecorator.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MySqlImageStorageService implements ImageStorageService {

    private static final Logger logger = LoggerFactory.getLogger(MySqlImageStorageService.class);

    public static final int THUMBNAIL_MAX_WIDTH = 160;
    public static final int THUMBNAIL_MAX_HEIGHT = 120;
    private static final int maxAttemptCount = 5;

    private String imageDirectory;
    private static final int CONDITION_NOT_MET = 412;
    static final String FILE_TYPE = "jpg";

    public MySqlImageStorageService(@Value("${mapdecorator.images.save-directory}")String imageDirectory){
        this.imageDirectory = imageDirectory;
    }

    @Override
    public String saveMedium(String imageFileName, byte[] imageData)  {
        return savePhotoToDisk(imageFileName,imageData);
    }

    @Override
    public String createAndSaveThumbnail(String imageFileName, byte[] imageData) throws IOException {
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

        return savePhotoToDisk(imageFileName, imageInByte);
    }


    private String savePhotoToDisk(String imageFileName, byte[] imageData){
        String pathName = imageDirectory + "/" + imageFileName + FILE_TYPE;
        try (FileOutputStream fos = new FileOutputStream(pathName)) {
            fos.write(imageData);
        }catch(Exception e){
            logger.error("Exception during saveFile :"  + e.getMessage());
        }
//        while (attemptCount < maxAttemptCount) {
//            try {
//                pathName = fileName + suffix + "." + FILE_TYPE;
//                mapDecoratorBucket.create(pathName, new ByteArrayInputStream(imageData), "image/" + FILE_TYPE, Bucket.BlobWriteOption.doesNotExist());
//            } catch (StorageException e) {
//                if (e.getCode() != CONDITION_NOT_MET && !e.isRetryable()) {
//                    throw e;
//                } else {
//                    attemptCount++;
//                    suffix = "" + attemptCount;
//                    continue;
//                }
//            }
//            break;
//        }
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

}
