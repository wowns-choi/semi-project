package com.project.semi.register.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterMessage {

	private int messageNo;
	private int memberNo;
	private int loginMemberNo;
	private String messageTitle;
	private String messageContent;
	private String messageCheck;
	private String messageRegdate;
	private int registeredMemberNo;
}
