package com.apis.cerana.model;

import android.content.Context;
import org.json.JSONObject;

public class User {
  private String token;
  private String username;
  private String name;
  private Integer phone;
  private Integer idIdentityCard;
  private String province;
  private String district;
  private String subDistrict;
  private String village;
  private String img;
  private Integer status;
  private Cache cache;

  public User(Context context) {
    cache = new Cache(context);
    try {
      JSONObject auth = cache.get();
      setToken((String) auth.get("token"));
    }catch (Exception e) {
      setToken("");
    }
  }

  public void set(String value) {
    cache.set(value);
  }

  public void clear() {
    cache.remove();
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPhone() {
    return phone;
  }

  public void setPhone(Integer phone) {
    this.phone = phone;
  }

  public Integer getIdIdentityCard() {
    return idIdentityCard;
  }

  public void setIdIdentityCard(Integer idIdentityCard) {
    this.idIdentityCard = idIdentityCard;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getSubDistrict() {
    return subDistrict;
  }

  public void setSubDistrict(String subDistrict) {
    this.subDistrict = subDistrict;
  }

  public String getVillage() {
    return village;
  }

  public void setVillage(String village) {
    this.village = village;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
