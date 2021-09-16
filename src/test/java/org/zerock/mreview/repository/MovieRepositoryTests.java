package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRepositoryTests {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

//    @Commit
//    @Transactional
//    @Test
//    public void insertMovies() {
//        IntStream.rangeClosed(1,100).forEach(new IntConsumer() { // 람다식 사용 X.
//            @Override
//            public void accept(int i) {
//                Movie movie = Movie.builder().title("Movie..." + i).build();
//                System.out.println("----------------------------------------");
//                movieRepository.save(movie);
//
//                int conut = (int) (Math.random() * 5) + 1;
//
//                for(int j = 0; j < conut; j++) { // 하나의 영화에 랜덤으로 count 개수만큼 사진을 추가.
//                    MovieImage movieImage = MovieImage.builder()
//                            .uuid(UUID.randomUUID().toString()) // byte[]로 랜덤한 이름을 지정하여 String으로 변환 (유니크한 이름을 가지도록)
//                            .movie(movie)
//                            .imgName("test" + j + ".jpg")
//                            .build();
//                    imageRepository.save(movieImage);
//                }
//            } // accept()
//        }); // IntCosumer()
//    } // inserMovies()

    @Test
    public void testListPage() {
        // PageRequest는 Pageable을 구현한 구현체이기 때문에 Pageable을 대신해서 사용이 가능하다.
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"mno"));
//        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending()); // 가능

        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        for(Object[] objects : result.getContent()) {
            System.out.println(Arrays.toString(objects));
        }
    } // testListPage()
}