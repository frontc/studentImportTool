package cn.lefer;

public class Config {
    private String data;
    private int skip;
    private String api;
    private String auth;
    private String schema;

    public Config(String data, int skip, String api, String auth, String schema) {
        this.data = data;
        this.skip = skip;
        this.api = api;
        this.auth = auth;
        this.schema = schema;
    }

    public Config() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "Config{" +
                "data='" + data + '\'' +
                ", skip=" + skip +
                ", api='" + api + '\'' +
                ", auth='" + auth + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
