package com.mapdecorator.controller;

import com.google.gson.GsonBuilder;
import com.mapdecorator.images.ImageStorageService;
import com.mapdecorator.repository.db.MapFeature;
import com.mapdecorator.repository.db.MapFeaturePhoto;
import com.mapdecorator.service.MapDecoratorService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class AJAXRequestController {

  private static final Logger logger = LoggerFactory.getLogger(AJAXRequestController.class);
  private final MapDecoratorService mapDecoratorService;
  private final ImageStorageService imageStorageService;

  @Autowired
  public AJAXRequestController(MapDecoratorService mapDecoratorService, ImageStorageService imageStorageService) {
    this.mapDecoratorService = mapDecoratorService;
    this.imageStorageService = imageStorageService;
  }

  @GetMapping(value = "/images/**")
  public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String pathWithPrefix = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    //Substring starting after "/images/", so 8 characters:
    String pathWithFilename = pathWithPrefix.substring(8);
    response.setContentType("image/jpg");
    imageStorageService.writeImageToOutputStream(pathWithFilename, response.getOutputStream());
  }

  /**
   * Get's all the categories as a single JSON object named allCategories
   */
  @RequestMapping(method = RequestMethod.GET, value = "/allCategories")
  public String getAllCategories() {
    return "var allCategories = " + toJSONString(MapFeature.Category.values()) + ";";
  }

  /**
   * Get's all the features as a single JSON object named allFeatures.
   * The JSON will be a list of MapFeature with only the properties that are tagged with @Expose annotation.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/allFeatures")
  public String getFeaturesJSONObject() {
    return "var allFeatures = " + toJSONStringOnlyExposedFields(mapDecoratorService.getAllFeatures()) + ";";
  }

  @PostMapping("/feature")
  public ResponseEntity<String> insertFeature(@RequestParam String category, @RequestParam String name,
      @RequestParam String description, @RequestParam String googlePlaceId, @RequestParam String latitude,
      @RequestParam String longitude, @RequestParam String imageDataURL) {

    long startTime, startTotal;
    startTotal = System.nanoTime();
    try {
      startTime = System.nanoTime();
      byte[] photoBytes = ImageStorageService.imagaDataURLtoBinary(imageDataURL);
      logTimeDifference("imagaDataURLtoBinary", startTime, System.nanoTime());

      startTime = System.nanoTime();
      imageStorageService.saveMedium(googlePlaceId, photoBytes);
      logTimeDifference("saveMedium ", startTime, System.nanoTime());

      startTime = System.nanoTime();
      imageStorageService.createAndSaveThumbnail(googlePlaceId, photoBytes);
      logTimeDifference("createAndSaveThumbnail ", startTime, System.nanoTime());
    } catch (IOException e) {
      e.printStackTrace();
      return responseError(
          "Application imageStorageService error. Please try again or contact the system administrator");
    }
    startTime = System.nanoTime();
    MapFeature newFeature = new MapFeature();
    try {
      newFeature.setName(name);
      newFeature.setDescription(description);
      newFeature.setCategory(MapFeature.Category.valueOf(category.toUpperCase()));
      newFeature.setGooglePlaceId(googlePlaceId);
      newFeature.setLatitude(new BigDecimal(latitude));
      newFeature.setLongitude(new BigDecimal(longitude));
      MapFeaturePhoto newPhoto = new MapFeaturePhoto();
      newPhoto.setMediumPhotoIdentifier(googlePlaceId);
      newPhoto.setThumbnailIdentifier(googlePlaceId);
      newPhoto.setMapFeature(newFeature);
      newFeature.getPhotoList().add(newPhoto);
      mapDecoratorService.insertMapFeature(newFeature, newPhoto);
    } catch (Exception e) {
      System.out.println("Error inserting feature : " + e);
      System.out.println("error " + e.getMessage());
      e.printStackTrace();
      return responseError("Application database error. Please try again or contact the system administrator");
    }
    logTimeDifference("insertMapFeature", startTime, System.nanoTime());
    logTimeDifference("Total time", startTotal, System.nanoTime());

    return responseOK(toJSONStringOnlyExposedFields(Arrays.asList(newFeature)));
  }

  @RequestMapping(value = {"/_ah/health", "/readiness_check", "/liveness_check"})
  public ResponseEntity<String> healthCheck() {
    return new ResponseEntity<>("Healthy", HttpStatus.OK);
  }

  private static void logTimeDifference(String text, long startNanoseconds, long endNanoseconds){
    long durationMilliseconds = (endNanoseconds - startNanoseconds) / 10000000;
    logger.info(text + " took " + durationMilliseconds + "ms");
  }

  private static String toJSONStringOnlyExposedFields(Object someObject) {
    return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(someObject);
  }

  private static String toJSONString(Object someObject) {
    return new GsonBuilder().create().toJson(someObject);
  }

  private static ResponseEntity<String> responseOK(String responseBody) {
    return ResponseEntity.ok(responseBody);
  }

  private static ResponseEntity<String> responseError(String message) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }
}
