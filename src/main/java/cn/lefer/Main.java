package cn.lefer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        //读取配置文件
//        String configFileName = "/Users/simq/tool/config.json";
        String configFileName = "config.json";
        if(args.length>1&&args[0].equals("-config")){
            configFileName = args[1];
        }
        Config config = getConfig(configFileName);
        //读取数据文件并转为json
        Stream<String> dataStream = Files.lines(Paths.get(config.getData())).skip(config.getSkip());
        List<String> sourceList = dataStream.collect(Collectors.toList());
        List<String>  datalist = sourceList.stream().map(s -> toJson(s,config.getSchema())).collect(Collectors.toList());
        //发起http请求
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(config.getApi());
        post.setHeader("Authorization",config.getAuth());
        post.setHeader("Accept","application/json");
        post.setHeader("Content-type","application/json");
        for(int i=0;i<datalist.size();i++){
            post.setEntity(new StringEntity(datalist.get(i)));
            HttpResponse response = client.execute(post);
            String sb = sourceList.get(i) + "," +
                    response.getStatusLine().getStatusCode() +
                    "," +
                    EntityUtils.toString(response.getEntity());
            sourceList.set(i, sb);
        }
        //输出执行结果
        List<String> result = new ArrayList<>();
        result.add("\ufeff");
        result.addAll(sourceList);
        String outputFileName = "result_"+LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))+".csv";
        Files.write(Paths.get(outputFileName),result);
    }

    private static Config getConfig(String configFilePathStr) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(configFilePathStr));
        return gson.fromJson(reader, Config.class);
    }

    private static String toJson(String sourceStr,String schema){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        String[] values = sourceStr.split(",");
        String[] names = schema.split(",");
        if(values.length!=names.length){
            throw new RuntimeException("Schema与Data不匹配:"+ Arrays.toString(names) +"-"+ Arrays.toString(values));
        }
        for(int i=0;i<names.length;i++){
            String[] nameAndType = names[i].split("\\|");
            stringBuilder.append("\"");
            stringBuilder.append(nameAndType[0]);
            stringBuilder.append("\":");
            if(nameAndType.length>1&&nameAndType[1].equals("number")){
                stringBuilder.append(values[i]);
            }else{
                stringBuilder.append("\"");
                stringBuilder.append(values[i]);
                stringBuilder.append("\"");
            }
            if(i!=names.length-1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
