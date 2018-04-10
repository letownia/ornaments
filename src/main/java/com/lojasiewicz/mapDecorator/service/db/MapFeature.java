package com.lojasiewicz.mapDecorator.service.db;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MapFeature implements Serializable {

    @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
    private Long id;

    @NotNull
    @Expose
    private String description;

    @NotNull
    @Expose
    private String name;

    @NotNull
    @Expose
    private String googlePlaceId;

    @NotNull
    @Expose
    private BigDecimal longitude;

    @NotNull
    @Expose
    private BigDecimal latitude;

    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private  java.sql.Timestamp createTimestamp;

    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private  java.sql.Timestamp modifyTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="mapFeature")
    @Expose
    private List<MapFeaturePhoto> photoList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }


    public java.sql.Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(java.sql.Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }


    public java.sql.Timestamp getModifyTimestamp() {
        return modifyTimestamp;
    }

    public void setModifyTimestamp(java.sql.Timestamp modifyTimestamp) {
        this.modifyTimestamp = modifyTimestamp;
    }

    public List<MapFeaturePhoto> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<MapFeaturePhoto> photoList) {
        this.photoList = photoList;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
