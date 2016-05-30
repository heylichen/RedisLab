package com.heylichen.redis.datatypes.hash;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lc on 2016/5/28.
 */
public class BookVoting {
  private RedisTemplate<String, Object> template;

  public BookVoting(RedisTemplate<String, Object> template) {
    this.template = template;
  }

  public void saveLink(String id, String author, String title) {
    Map<String, Object> map = new HashMap<>();
    map.put("author", author);
    map.put("title", title);
    map.put("score", new Integer(0));
    template.opsForHash().putAll(getKey(id), map);
  }


  public void upVote(String id) {
    template.opsForHash().increment(getKey(id), "score", 1);
  }

  public void downVote(String id) {
    template.opsForHash().increment(getKey(id), "score", -1);
  }

  public Map<String, Object> getInfo(String id) {
    return template.<String,Object>opsForHash().entries(getKey(id));
  }

  public void removeAll(String id) {
    template.delete(getKey(id));
  }

  private String getKey(String id) {
    return "book" + id;
  }


}
