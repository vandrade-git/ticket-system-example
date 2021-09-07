package org.vandrade.support.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * Author: Vitor Andrade
 * Date: 10/11/18
 * Time: 3:43 PM
 */

public class CustomUserDetails implements UserDetails, Serializable {
    public CustomUserDetails(User customUser) {
        this.customUSer = customUser;
    }

    // UserDetails >>
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "{bcrypt}" + customUSer.getPassword();
    }

    @Override
    public String getUsername() {
        return customUSer.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customUSer.getEnabled();
    }
    // << UserDetails

    public long getId() {
        return customUSer.getId();
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "customUser=" + customUSer.toString() +
                '}';
    }

    // Fields >>
    private final User customUSer;
    // << Fields
}
