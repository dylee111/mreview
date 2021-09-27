package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
  private Long mno;
  private String title;

  @Builder.Default
  private List<MovieImageDTO> imageDTOList = new ArrayList<>();

  private double avg;
  private int reviewCnt;

  // JPA에 의해 Entity에서 넘어온 값을 담기 위한 변수
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}
