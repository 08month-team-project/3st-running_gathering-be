package com.runto.domain.image.type;

import com.runto.domain.image.dto.ImageUrlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageUrlsResponse {

    private List<ImageUrlDto> contentImageUrls;

}
