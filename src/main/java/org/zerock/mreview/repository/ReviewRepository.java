package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 쿼리 메서드 활용 : 메서드 이름 자체가 쿼리의 구문으로 처리되는 기능
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // Member를 삭제되기 위해서 review가 먼저 삭제되고 member가 삭제되어야 함.
    @Modifying
    @Query("DELETE FROM Review mr WHERE mr.member = :member ")
    void deleteByMember(Member member); // 삭제는 반환타입이 X.
}
