package com.mapdecorator.repository.db;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import javax.validation.constraints.Size;

@Entity
//@Access(AccessType.PROPERTY)
public class MapFeaturePhoto implements Serializable {
  @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
  private  long id;
  @ManyToOne(fetch=FetchType.LAZY) @NotNull
  @JoinColumn(name="map_feature_id")
  private  MapFeature mapFeature;

  @Size(min = 1, max = 512)
  @NotNull
  @Expose
  private String mediumPhotoIdentifier;

  @Size(min = 1, max = 512)
  @NotNull
  @Expose
  private String thumbnailIdentifier;

//  @Lob @Basic(fetch = FetchType.LAZY) @NotNull
//  @Column(length=16777215)
//  private byte[] photo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MapFeature getMapFeature() {
    return mapFeature;
  }

  public void setMapFeature(MapFeature mapFeature) {
    this.mapFeature = mapFeature;
  }

  public String getMediumPhotoIdentifier() {
    return mediumPhotoIdentifier;
  }

  public void setMediumPhotoIdentifier(String mediumPhotoIdentifier) {
    this.mediumPhotoIdentifier = mediumPhotoIdentifier;
  }

  public String getThumbnailIdentifier() {
    return thumbnailIdentifier;
  }

  public void setThumbnailIdentifier(String thumbnailIdentifier) {
    this.thumbnailIdentifier = thumbnailIdentifier;
  }
}
