package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "m_member")
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mid;

  @JoinColumn(name = "email", referencedColumnName = "email")
  private String email;

  private String pw;
  private String nickname;

  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<MemberRole> roleSet = new HashSet<>();

  public void addMemberRole(MemberRole memberRole) {
      roleSet.add(memberRole);
  }
}
