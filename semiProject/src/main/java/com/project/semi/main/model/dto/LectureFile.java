package com.project.semi.main.model.dto;

import java.util.Date;

import com.project.semi.member.model.dto.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder // 인스턴스 쉽게 만들게해줌.
public class LectureFile {
    private Integer lectureFileNo;
    private Integer lectureNo;
    private String filePath;
    private String originalName;
    private String fileRename;
    private Date uploadDate;
}
