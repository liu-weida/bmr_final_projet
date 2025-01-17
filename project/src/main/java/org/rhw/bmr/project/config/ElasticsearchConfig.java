package org.rhw.bmr.project.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.username}")
    String username;

    @Value("${elasticsearch.password}")
    String password;

    @Value("${elasticsearch.host}")
    String host;

    @Value("${elasticsearch.port}")
    int port;

    @Value("${elasticsearch.protocol}")
    String protocol;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        try {

            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            // 配置 SSL 上下文（信任所有证书，仅用于测试）
            TrustStrategy trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true; // 信任所有证书
                }
            };
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(trustStrategy)
                    .build();

            // 配置 RestClientBuilder
            RestClientBuilder builder = RestClient.builder(
                    new HttpHost(host, port, protocol) // 使用 HTTPS 协议
            ).setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider) // 设置认证
                    .setSSLContext(sslContext) // 设置 SSL 上下文
                    .setSSLHostnameVerifier((hostname, session) -> true) // 忽略主机名验证
                    .setDefaultIOReactorConfig(IOReactorConfig.custom()
                            .setIoThreadCount(4) // 设置 IO 线程数
                            .build())
                    .setMaxConnTotal(100) // 设置最大连接数
                    .setMaxConnPerRoute(100) // 设置每个路由的最大连接数
            ).setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                    .setConnectTimeout(10000) // 增加连接超时时间（10 秒）
                    .setSocketTimeout(30000)  // 增加套接字超时时间（30 秒）
            );

            // 创建 ElasticsearchClient
            RestClient restClient = builder.build();
            RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            return new ElasticsearchClient(transport);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }
}
