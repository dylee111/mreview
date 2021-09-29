package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

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

  @Test
  public void insertMembers() {
    IntStream.rangeClosed(1, 100).forEach(new IntConsumer() {
      @Override
      public void accept(int i) {
        Member member = Member.builder().email("r" + i + "@ds.com")
                .pw("1111").nickname("reviewer" + i).build();
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

    //순서 주의
    reviewRepository.deleteByMember(member);
    memberRepository.deleteById(mid);
  }
}