package com.heylichen.redis.timeseries;

/**
 * Created by lichen2 on 2016/5/31.
 */
public class TimeGranularity {

  private String name;
  private int ttl;
  private int duration;
  private int quantity;

  public TimeGranularity(String name, int ttl, int duration) {
    this.name = name;
    this.ttl = ttl;
    this.duration = duration;
  }

  public TimeGranularity(String name, int ttl, int duration, int quantity) {
    this.name = name;
    this.ttl = ttl;
    this.duration = duration;
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTtl() {
    return ttl;
  }

  public void setTtl(int ttl) {
    this.ttl = ttl;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getQuantity() {
    return quantity;
  }
}
