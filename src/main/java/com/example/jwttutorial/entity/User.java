package com.example.jwttutorial.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   @Column(name = "user_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(name = "username", length = 50, unique = true)
   private String username;

   @Column(name = "password", length = 100)
   private String password;

   @Column(name = "nickname", length = 50)
   private String nickname;

   @Column(name = "activated")
   private boolean activated;

   @ManyToMany
   @JoinTable(
      name = "user_authority",//연결테이블의 이름
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
   /*
joinColumns: 현재 클래스(User)와 연결 테이블 사이의 연결을 정의하는 열입니다.
여기서는 "user_id"라는 이름의 컬럼이며, User 클래스의 "user_id"라는 필드를 참조합니다.
inverseJoinColumns: 다른 클래스(Authority)와 연결 테이블 사이의 연결을 정의하는 열입니다.
여기서는 "authority_name"이라는 이름의 컬럼이며, Authority 클래스의 "authority_name" 필드를 참조합니다.
 */
   private Set<Authority> authorities;
}
