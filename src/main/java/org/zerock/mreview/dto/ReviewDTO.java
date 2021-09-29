package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long reviewnum; // Review Num

    private Long mno; // Movie Num

    private Long mid; // Member ID
    private String nickname; // Member NickName;
    private String email; // Member email

    private int grade;

    private String text;

    private LocalDateTime regDate, modDate;
}
