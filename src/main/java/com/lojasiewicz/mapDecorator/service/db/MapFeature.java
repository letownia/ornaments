package com.lojasiewicz.mapDecorator.service.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MapFeature {

    @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
    private Long id;
    @NotNull
    private String description;
    @NotNull
    private String googlePlaceId;
    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private java.sql.Timestamp createTimestamp;
    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private java.sql.Timestamp modifyTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="mapFeature")
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
}
