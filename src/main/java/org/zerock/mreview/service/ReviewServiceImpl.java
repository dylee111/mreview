package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {

        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);

        return result.stream().map(movieReview -> entityToDTO(movieReview)).collect(Collectors.toList());
    } // getListOfMovie()

    @Override
    public Long register(ReviewDTO movieReviewDTO) {

        Review movieReview = dtoToEntity(movieReviewDTO);

        reviewRepository.save(movieReview);

        return movieReview.getReviewnum();
    } //register()

    @Override
    public void modify(ReviewDTO movieReviewDTO) {
        // 검색하고자 하는 대상이 하나. 여러 개일 경우는 Collection 사용
        Optional<Review> result = reviewRepository.findById(movieReviewDTO.getReviewnum());
        // DTO의 reviewnum을 불러와서 result에 Review로 형변환하여 저장

        if(result.isPresent()) {
            // result를 <Review>로 형변환했기 때문에 별도의 형변환없이 movieReview에 대입하는 것이 가능
            Review movieReview = result.get();
            movieReview.changeGrade(movieReviewDTO.getGrade());
            movieReview.changeText(movieReviewDTO.getText());

            reviewRepository.save(movieReview);
        }

    } // modify()

    @Override
    public void remove(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    } // remove()
}
