package com.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

/**
 * packageName : com.model.admin.dto
 * className : MainDto
 * user : jwlee
 * date : 2022/10/02
 */
public class MainDto {

    @Data
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class CompanyDto {

        @NotNull
        private Integer cpNo;
        @NotBlank
        private String cpName;
        @NotNull
        private String cpImgUrl;

        @Builder
        public CompanyDto(Integer cpNo, String cpName, String cpImgUrl) {
            this.cpNo = cpNo;
            this.cpName = cpName;
            this.cpImgUrl = cpImgUrl;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class NavigationDto {

        @NotNull
        private Integer naviNo;
        @NotNull
        private String title;
        private Integer sort;
        private String link;
        @NotNull
        private String dataStatus;
        private String regDatetime;
        private String updDatetime;

        @Builder
        public NavigationDto(Integer naviNo, String title, Integer sort, String link, String dataStatus, String regDatetime, String updDatetime) {
            this.naviNo = naviNo;
            this.title = title;
            this.sort = sort;
            this.link = link;
            this.dataStatus = dataStatus;
            this.regDatetime = regDatetime;
            this.updDatetime = updDatetime;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class SliderDto {

        @NotNull
        private Integer sliderNo;
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private String imgUrl;
        @NotNull
        private Integer sort;
        @NotNull
        private String dataStatus;
        @NotNull
        private String regDatetime;
        @NotNull
        private String updDatetime;

        @Builder
        public SliderDto(Integer sliderNo, String title, String content, String imgUrl, Integer sort, String dataStatus, String regDatetime, String updDatetime) {
            this.sliderNo = sliderNo;
            this.title = title;
            this.content = content;
            this.imgUrl = imgUrl;
            this.sort = sort;
            this.dataStatus = dataStatus;
            this.regDatetime = regDatetime;
            this.updDatetime = updDatetime;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class DeleteSliderDto {

        @NotNull
        private Integer sliderNo;

        @Builder
        public DeleteSliderDto(Integer sliderNo) {
            this.sliderNo = sliderNo;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(NON_NULL)
    public static class UpdateContentsDto {

        @NotNull
        private Integer ctNo;
        @NotBlank
        private String ctTxt;
        @Builder
        public UpdateContentsDto(Integer ctNo, String ctTxt) {
            this.ctNo = ctNo;
            this.ctTxt = ctTxt;
        }
    }

}
