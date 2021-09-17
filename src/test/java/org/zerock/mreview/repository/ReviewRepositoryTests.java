package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertReview() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long) (Math.random() * 100) + 1;
            // 리뷰어 번호
            Long mid = (long) (Math.random() * 100) + 1;
            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("영화에 대한 느낌..." + i)
                    .build();
            reviewRepository.save(review);
        });
    } // insertReview()

    @Test
    public void testFindByMovie() {
        Movie movie = Movie.builder().mno(70L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {
            System.out.print(movieReview.getReviewnum());
            System.out.print("\t" + movieReview.getGrade());
            System.out.print("\t" + movieReview.getText());
            System.out.print("\t" + movieReview.getMember().getEmail());
        });
    } // testFindByMovie()

    @Test
    @Commit
    @Transactional
    public void testDeleteMember() {
        Long mid = 1L;

        Member member = Member.builder().mid(mid).build();

        // 순서 주의!!!!!!!!!!!!!!!!!!!!!!
        reviewRepository.deleteByMember(member); // 해당 member가 작성한 review가 먼저 삭제
        memberRepository.deleteById(mid);        // 다음 member가 삭제
    } // testDeleteMember()

}