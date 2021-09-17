package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    /**
     *  영화 이미지 (mi)
     *  영화 평점의 평균 (r.grade)
     *  Review 개수 (count(r))
     *  List 출력
     */
    /**
     * SELECT m.mno, m.title, mi.*, avg(coalesce(r.grade,0)), COUNT(distinct r.reviewnum)
     * FROM movie m
     * LEFT OUTER JOIN movie_image mi ON mi.movie_mno = m.mno
     * LEFT OUTER JOIN review r ON r.movie_mno = m.mno
     * GROUP BY m.mno ORDER BY m.mno DESC;
     */
    // coalesce : r.grade의 값이 있으면 r.grade 값을 출력, 없으면 0을 출력, 0번 자리 값을 지정 안하면 null로 출력

   /* // 서브 쿼리를 활용하여 N+1문제 해결.(mi에 적용되었던 max()를 걷어 냄)
   @Query("SELECT m, i, count(r) " +
            " FROM Movie m " +
            " LEFT JOIN MovieImage i " +
            " ON i.movie = m " +
            " AND i.inum = (SELECT max(i2.inum) FROM MovieImage i2 WHERE i2.movie = m ) " +
            " LEFT JOIN Review r " +
            " ON r.movie = m " +
            " GROUP BY m ")
    */
    @Query("SELECT m, mi, avg(coalesce(r.grade,0)), count(distinct r) " +
            " FROM Movie m " +
            " LEFT OUTER JOIN MovieImage mi " +
            " ON mi.movie = m " +
            " LEFT OUTER JOIN Review r " +
            " ON r.movie = m " +
            " GROUP BY m ")
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리

/*    @Query("SELECT m, mi " +
            " FROM Movie m " +
            " LEFT OUTER JOIN MovieImage mi " +
            " ON mi.movie = m " +
            " WHERE m.mno = :mno ")
*/
    @Query("SELECT m, mi, avg(coalesce(r.grade,0)), count(r) " +
            " FROM Movie m " +
            " LEFT OUTER JOIN MovieImage mi " +
            " ON mi.movie = m " +
            " LEFT OUTER JOIN Review r " +
            " ON r.movie = m " +
            " WHERE m.mno = :mno " +
            " GROUP BY mi ")
    List<Object[]> getMovieWithAll(Long mno); // 특정 영화 조회
}
