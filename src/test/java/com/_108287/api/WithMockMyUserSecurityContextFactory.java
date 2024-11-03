package com._108287.api;

import com._108287.api.entities.MyUserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class WithMockMyUserSecurityContextFactory implements WithSecurityContextFactory<WithMockMyUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockMyUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    MyUserDetails principal = new MyUserDetails(customUser.username());
    UsernamePasswordAuthenticationToken auth =
      new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

    context.setAuthentication(auth);
    return context;
  }
}
