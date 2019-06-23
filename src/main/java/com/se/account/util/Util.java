package com.se.account.util;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static String Md5(String content) throws NoSuchAlgorithmException {
        //生成实现指定摘要算法的 MessageDigest 对象。
        MessageDigest md = MessageDigest.getInstance("MD5");
        //使用指定的字节数组更新摘要。
        md.update(content.getBytes());
        //通过执行诸如填充之类的最终操作完成哈希计算。
        byte[] b = md.digest();
        //生成具体的md5密码到buf数组
        int i;
        StringBuilder buf = new StringBuilder("");
        for (byte b1 : b) {
            i = b1;
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    public static String httpGet(String uri, Map<String, String> args)throws ServiceException{
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        StringBuilder uriBuilder = new StringBuilder(uri + "?");
        for(String k : args.keySet()){
            uriBuilder.append(k).append("=").append(args.get(k)).append("&");
        }
        uri = uriBuilder.toString();
        uri = uri.substring(0, uri.length() - 1);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new ServiceException(ErrorEnum.ERROR_GET_NO_RESPONSE_FROM_HOST);
        }
        return response.getBody();
    }

    public static void main(String[] args){
        try {
            System.out.println(Md5("itemzheng1234561"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            Map<String, String> arg = new HashMap<>();
            arg.put("account_id", "passwd");
            arg.put("id_number", "1");
            System.out.println(httpGet("http://10.180.29.168:8081/checkID", arg));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
