package com.lojasiewicz.mapDecorator.service.db;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
//@Access(AccessType.PROPERTY)
public class MapFeaturePhoto implements Serializable {
  @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
  private  Long id;
  @ManyToOne(fetch=FetchType.LAZY) @NotNull
  @JoinColumn(name="map_feature_id")
  private  MapFeature mapFeature;

  @NotNull
  @Expose
  private String blobName;

//  @Lob @Basic(fetch = FetchType.LAZY) @NotNull
//  @Column(length=16777215)
//  private byte[] photo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

//  public byte[] getPhoto() {
//    return photo;
//  }
//
//  public void setPhoto(byte[] photo) {
//    this.photo = photo;
//  }

  public MapFeature getMapFeature() {
    return mapFeature;
  }

  public void setMapFeature(MapFeature mapFeature) {
    this.mapFeature = mapFeature;
  }

  public String getBlobName() {
    return blobName;
  }

  public void setBlobName(String blobName) {
    this.blobName = blobName;
  }


//  public long getMapFeatureId() {
//    return mapFeatureId;
//  }
//
//  public void setMapFeatureId(long mapFeatureId) {
//    this.mapFeatureId = mapFeatureId;
//  }


}
