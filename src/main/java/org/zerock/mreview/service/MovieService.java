package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.entity.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MovieService {

    Long register(MovieDTO movieDTO);

//    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
//        Map<String, Object> entityMap = new HashMap<>();
//
//        Movie movie = Movie.builder()
//                .mno(movieDTO.getMno())
//                .title(movieDTO.getTitle())
//                .build();
//
//        entityMap.put("movie", movie);
//
//        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
//
//        if(imageDTOList != null && imageDTOList.size() > 0)
//    } // dtoToEntity()
}
