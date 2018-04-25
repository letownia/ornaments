package com.lojasiewicz.mapDecorator.service.db;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
//@Access(AccessType.PROPERTY)
public class MapFeaturePhoto implements Serializable {
  @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
  private  Long id;
  @ManyToOne(fetch=FetchType.LAZY) @NotNull
  @JoinColumn(name="map_feature_id")
  private  MapFeature mapFeature;

  @Size(min = 1, max = 512)
  @NotNull
  @Expose
  private String mediumIdentifier;

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

  public String getMediumIdentifier() {
    return mediumIdentifier;
  }

  public void setMediumIdentifier(String mediumIdentifier) {
    this.mediumIdentifier = mediumIdentifier;
  }

  public String getThumbnailIdentifier() {
    return thumbnailIdentifier;
  }

  public void setThumbnailIdentifier(String thumbnailIdentifier) {
    this.thumbnailIdentifier = thumbnailIdentifier;
  }

}
