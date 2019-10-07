package com.gityou.repository.client;


import com.gityou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient extends UserApi {
}
