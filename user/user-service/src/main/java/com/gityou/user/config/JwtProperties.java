package com.gityou.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.PublicKey;


@ConfigurationProperties(prefix = "gityou.jwt")
public class JwtProperties {
    private String secret;  // 密钥
    private String pubKeyPath;

    private Integer expire; // 过期时间
    private String cookieName;

    private PublicKey publicKey;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}// end
