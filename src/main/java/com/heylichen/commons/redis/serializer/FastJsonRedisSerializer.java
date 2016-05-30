package com.heylichen.commons.redis.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created by lc on 2016/5/28.
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer {
  private byte[] EMPTY_BYTES = new byte[]{};
  private Class<T> clazz;

  public FastJsonRedisSerializer(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public byte[] serialize(Object o) throws SerializationException {
    if(o==null){
      return  EMPTY_BYTES;
    }
    return JSON.toJSONBytes(o);
  }

  @Override
  public Object deserialize(byte[] bytes) throws SerializationException {
    if(bytes==null||bytes.length==0){
      return null;
    }
    return JSON.parseObject(bytes,clazz);
  }
}
