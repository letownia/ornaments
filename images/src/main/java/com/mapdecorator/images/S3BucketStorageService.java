package com.mapdecorator.images;



import com.mapdecorator.images.util.ImageScalingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

@Service
@Profile("aws-prod")
public class S3BucketStorageService implements ImageStorageService {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final S3Client s3Client;

  private final String bucketName;
  private static final String ROOT_FOLDER = "photos/";
  private static final String MEDIUM_FOLDER = ROOT_FOLDER + "medium";
  private static final String THUMBNAIL_FOLDER = ROOT_FOLDER + "thumbnail";

  public S3BucketStorageService(@Value("${mapdecorator.s3.bucket.name}")String bucketName){
     s3Client = S3Client.builder().region(Region.EU_CENTRAL_1).build();
     this.bucketName = Objects.requireNonNull(bucketName);
  }

  @Override
  public String saveMedium(String identifier, byte[] imageData) {
    logger.info("saveMedium " + identifier);
    return saveObject(ImageStorageService.pathWithFilename(MEDIUM_FOLDER, identifier), imageData);
  }

  @Override
  public String createAndSaveThumbnail(String identifier, byte[] imageData) throws IOException {
    logger.info("createAndSaveThumbnail " + identifier);
    byte[] rescaledImageBytes = ImageScalingUtils.rescaleImage(imageData, THUMBNAIL_MAX_WIDTH, THUMBNAIL_MAX_HEIGHT, FILE_TYPE);

    return saveObject(ImageStorageService.pathWithFilename(THUMBNAIL_FOLDER, identifier), rescaledImageBytes);
  }

  private String saveObject(String fullPath, byte[] imageData) {
    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fullPath)
        .build();

    PutObjectResponse putObjectResponse = s3Client.putObject(objectRequest, RequestBody.fromBytes(imageData));
    return fullPath;
  }
  @Override
  public BufferedImage getImage(String relativePathWithFileName) {
    throw new RuntimeException("getImage -S3BucketStorage only is used for uploads - download via direct access to s3");
  }

  @Override
  public void writeImageToOutputStream(String relativePathWithFileName, OutputStream outputStream) {
    throw new RuntimeException("writeImageToOutputStream - S3BucketStorage only is used for uploads - download via direct access to s3");
  }
}
