package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.MemberRole;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.Optional;
import java.util.UUID;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTests {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  public void insertMembers() {
    IntStream.rangeClosed(1, 100).forEach(new IntConsumer() {
      @Override
      public void accept(int i) {
        Member member = Member.builder()
                .email("r" + i + "@ds.com")
                .pw(passwordEncoder.encode("1111"))
                .nickname("reviewer" + i)
                .build();

        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);
      }
    });
  }

  @Test
  @Commit
  @Transactional
  public void testDeleteMember(){
    Long mid = 1L;
    Member member = Member.builder().mid(mid).build();
//    memberRepository.deleteById(mid);
//    reviewRespository.deleteByMember(member);

    //μμ μ£Όμ
    reviewRepository.deleteByMember(member);
    memberRepository.deleteById(mid);
  }

  @Test
  public void testRead() {
    Optional<Member> result = memberRepository.findByEmail("r20@ds.com");
    Member member = result.get();

    System.out.println(member);
  }
}