package com._108287.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class MyUserDetails implements UserDetails {
  private final String userSub;

  @Override
  public String getUsername() {
    return userSub;
  }

  // Not required for this case
  // No roles
  // No password
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return null;
  }
}
