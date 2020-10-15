package redisspringboot.com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @RequestMapping("/")
    public String testConnection(){
        redisTemplate.opsForValue().set("test","testvalue");
        return "result";
    }
}
