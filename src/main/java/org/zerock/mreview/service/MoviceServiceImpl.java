package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.repository.MovieRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class MoviceServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Long register(MovieDTO movieDTO) {
        return null;
    }
}
