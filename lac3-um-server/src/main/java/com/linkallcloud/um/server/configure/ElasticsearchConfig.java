package com.linkallcloud.um.server.configure;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hosts}")
    private String hosts;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;
    // http连接超时时间
    @Value("${elasticsearch.connectTimeout:3000}")
    private int connectTimeout;
    // socket连接超时时间
    @Value("${elasticsearch.socketTimeout:3000000}")
    private int socketTimeout;
    // 获取连接的超时时间
    @Value("${elasticsearch.connectionRequestTimeout:2000}")
    private int connectionRequestTimeout;
    // 最大连接数
    @Value("${elasticsearch.maxConnTotal:100}")
    private int maxConnTotal;
    // 最大路由连接数
    @Value("${elasticsearch.maxConnPerRoute:100}")
    private int maxConnPerRoute;

    @Bean
    public RestHighLevelClient clientDev() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        // 初始化ES客户端的构造器
        RestClientBuilder builder = RestClient.builder(httpHostHandlerDev());
        // 异步的请求配置
        builder.setRequestConfigCallback(builder1 -> {
            // 连接超时时间 默认-1
            builder1.setConnectTimeout(connectTimeout);
            //
            builder1.setSocketTimeout(socketTimeout);
            // 获取连接的超时时间 默认-1
            builder1.setConnectionRequestTimeout(connectionRequestTimeout);
            return builder1;
        });
        // 异步的httpclient连接数配置
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            // 最大连接数
            httpAsyncClientBuilder.setMaxConnTotal(maxConnTotal);
            // 最大路由连接数
            httpAsyncClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
            // 赋予连接凭证
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpAsyncClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }

    /**
     * 为了应对集群部署的es，使用以下写法，返回HttpHost数组
     */
    private HttpHost[] httpHostHandlerDev() {
        String[] hosts = this.hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            httpHosts[i] = HttpHost.create(hosts[i]);
        }
        return httpHosts;
    }
}
