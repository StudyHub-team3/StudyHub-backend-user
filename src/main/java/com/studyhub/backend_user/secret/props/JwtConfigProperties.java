package com.studyhub.backend_user.secret.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "jwt", ignoreUnknownFields = true)
@Getter
@Setter
public class JwtConfigProperties {
    private Integer expireIn;
    private Integer mobileExpireIn;
    private Integer tabletExpireIn;
    private String secretKey;
}
