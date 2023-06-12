package com.example.jwttutorial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

   private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

   private SecurityUtil() {}

   //
   public static Optional<String> getCurrentUsername() {
      //시큐리티 컨텍스트에서 authentication객체를 꺼내와서  이를 통해서 유저네임을 리턴해줌
      //시큐리티컨텍스트에 authentication객체가 저장되는시점은 jwtFilter의 dofilter메소드에서 request가 들어올때 시큐리티컨텍스트에
      //authentication객체를 저장해서 사용하게 됩니다.
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         logger.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(username);
   }
}
