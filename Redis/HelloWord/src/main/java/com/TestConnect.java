package com;

import redis.clients.jedis.Jedis;

public class TestConnect {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.113.97.26", 6379);
        System.out.println(jedis.ping());
        System.out.println(jedis.get("name"));
        jedis.set("age","40");
        jedis.close();
    }
}
