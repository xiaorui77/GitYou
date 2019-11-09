package com.gityou.common.pojo;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "settings")
public class Settings {
    @Id
    private Integer user;
    private Integer notificationWatching;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getNotificationWatching() {
        return notificationWatching;
    }

    public void setNotificationWatching(Integer notificationWatching) {
        this.notificationWatching = notificationWatching;
    }
}
