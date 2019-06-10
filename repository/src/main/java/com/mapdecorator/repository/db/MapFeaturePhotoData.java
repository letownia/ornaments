package com.mapdecorator.repository.db;

import com.google.gson.annotations.Expose;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class MapFeaturePhotoData {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
  private long id;

  @NotNull @Expose
  private byte[] data;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
