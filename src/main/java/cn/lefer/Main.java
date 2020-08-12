package cn.lefer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        //读取配置文件
        String configFileName = "/Users/simq/tool/tools.config";
        Stream<String> configStream = Files.lines(Paths.get(configFileName));
        List<String>  list = configStream.collect(Collectors.toList());
        String DATA_CONFIG = getConfigItemValue(list,"data");//数据文件
        if(DATA_CONFIG==null) return;
        String API_CONFIG = getConfigItemValue(list,"api");//API接口
        if(API_CONFIG==null) return;
        String AUTH_CONFIG = getConfigItemValue(list,"auth");//认证令牌
        String SCHEMA_CONFIG = getConfigItemValue(list,"schema");//数据模式
        if(SCHEMA_CONFIG==null) return;

        Stream<String> dataStream = Files.lines(Paths.get(DATA_CONFIG)).skip(1);
        List<String>  dataList = dataStream.collect(Collectors.toList());
        //转成json
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost();

        //发起http请求
        //结果写入文件
        System.out.println(list);
    }

    private static String getConfigItemValue(List<String> configItemList,String configItem){
        String configItemValue=null;
        Optional<String> str = configItemList.stream().filter(s -> s.contains(configItem)).findFirst();
        if(str.isPresent()){
            configItemValue=str.get().split("=")[1].trim();
        }
        System.out.println(configItem+":"+configItemValue);
        return configItemValue;
    }
}
