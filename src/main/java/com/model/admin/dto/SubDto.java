package com.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

/**
 * packageName : com.model.admin.dto
 * className : SubDto
 * user : jwlee
 * date : 2022/10/02
 */
public class SubDto {

    @Data
    @NoArgsConstructor
    public static class SubOneItemDto {

        /** 일련번호 **/
        private int awardNo;
        /** 수상명 **/
        private String awardName;
        /** 연결 주소 **/
        private String url;
        /** 순번 **/
        private int rank;
        /** 데이터 상태 **/
        private String dataStatus;
        /** 등록일 **/
        private String regDatetime;
        private int awardType;
        private String awardTypeName;

        @Builder
        public SubOneItemDto(int awardNo, String awardName, String url, int rank, String dataStatus, String regDatetime, int awardType, String awardTypeName) {
            this.awardNo = awardNo;
            this.awardName = awardName;
            this.url = url;
            this.rank = rank;
            this.dataStatus = dataStatus;
            this.regDatetime = regDatetime;
            this.awardType = awardType;
            this.awardTypeName = awardTypeName;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SubOneTypeDto {

        private int id;
        private String name;
        private int rank;
        private String dataStatus;

        @Builder
        public SubOneTypeDto(int id, String name, int rank, String dataStatus) {
            this.id = id;
            this.name = name;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubOneRegReqDto {

        @NotBlank
        private String awardName;               // 수상명
        @NotNull
        private Integer awardType;
        private String url;

        @Builder
        public SubOneRegReqDto(String awardName, Integer awardType, String url) {
            this.awardName = awardName;
            this.awardType = awardType;
            this.url = url;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubOneUpdReqDto {

        @NotNull
        private Integer awardNo;
        @NotBlank
        private String awardName;
        @NotNull
        private Integer awardType;
        private String url;
        @NotBlank
        private String rank;
        @NotBlank
        private String dataStatus;

        @Builder
        public SubOneUpdReqDto(Integer awardNo, String awardName, Integer awardType, String url, String rank, String dataStatus) {
            this.awardNo = awardNo;
            this.awardName = awardName;
            this.awardType = awardType;
            this.url = url;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubOneTypeUpdReqDto {

        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotNull
        private Integer rank;
        @NotBlank
        private String dataStatus;

        @Builder
        public SubOneTypeUpdReqDto(Integer id, String name, Integer rank, String dataStatus) {
            this.id = id;
            this.name = name;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubOneDelReqDto {

        @NotNull
        private Integer awardNo;

        @Builder
        public SubOneDelReqDto(Integer awardNo) {
            this.awardNo = awardNo;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class ResSubTwoDto {

        private List<List<SubTwoDto>> result;

        @Builder
        public ResSubTwoDto(List<List<SubTwoDto>> result) {
            this.result = result;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubTwoDto {

        @NotNull
        private Integer id;
        @NotNull
        private Integer groupId;
        @NotNull
        private String title;
        @NotNull
        private String description;
        @NotNull
        private String img;
        @NotNull
        private String url;
        @NotNull
        private Integer rank;
        @NotNull
        private String regDatetime;
        @NotNull
        private String updDatetime;

        @Builder
        public SubTwoDto(Integer id, Integer groupId, String title, String description, String img, String url, Integer rank, String regDatetime, String updDatetime) {
            this.id = id;
            this.groupId = groupId;
            this.title = title;
            this.description = description;
            this.img = img;
            this.url = url;
            this.rank = rank;
            this.regDatetime = regDatetime;
            this.updDatetime = updDatetime;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class SubTwoTextDto {

        @NotNull
        private Integer txtNo;
        @NotBlank
        private String txtContents;

        @Builder
        public SubTwoTextDto(Integer txtNo, String txtContents) {
            this.txtNo = txtNo;
            this.txtContents = txtContents;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    @NoArgsConstructor
    public static class ResSubTwoTextDto {

        @NotNull
        private Integer txtNo;
        @NotBlank
        private String txtContents;

        @Builder
        public ResSubTwoTextDto(Integer txtNo, String txtContents) {
            this.txtNo = txtNo;
            this.txtContents = txtContents;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SubTwoTypeDto {

        private int id;
        private String name;
        private int rank;
        private String dataStatus;

        @Builder
        public SubTwoTypeDto(int id, String name, int rank, String dataStatus) {
            this.id = id;
            this.name = name;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

    @Data
    @JsonInclude(NON_NULL)
    public static class SubTwoTypeUpdReqDto {

        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotNull
        private Integer rank;
        @NotBlank
        private String dataStatus;

        @Builder
        public SubTwoTypeUpdReqDto(Integer id, String name, Integer rank, String dataStatus) {
            this.id = id;
            this.name = name;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SubTwoGroupDto {

        private int id;
        private String name;
        private int rank;
        private String dataStatus;

        @Builder
        public SubTwoGroupDto(int id, String name, int rank, String dataStatus) {
            this.id = id;
            this.name = name;
            this.rank = rank;
            this.dataStatus = dataStatus;
        }
    }

}
