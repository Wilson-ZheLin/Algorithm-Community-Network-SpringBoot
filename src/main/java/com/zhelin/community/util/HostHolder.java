package com.zhelin.community.util;

import com.zhelin.community.entity.User;
import org.springframework.stereotype.Component;

/* Hold the current user info */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
