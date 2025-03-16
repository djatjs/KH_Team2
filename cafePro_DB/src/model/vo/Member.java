package model.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Member implements Serializable{

	private static final long serialVersionUID = 1L;
	private String mId;								//M_ID
	private String mPw;								//M_PW
	private String mNumber;							//M_NUMBER
	private String mNickname;						//M_NICKNAME
	private String mAuthority;  					//M_AUTHORITY
	private String mDel;							//M_DEL
	private Date mDelTime;							//M_DEL_TIME
	
	
	public Member() {}
	
	// 회원가입
	public Member(String id, String pw, String number, String nickname) {
		this.mId = id;
		this.mPw = pw;
		this.mNumber = number;
		this.mNickname = nickname;
	}

	// 로그인
	public Member(String id, String pw) {
		this.mId = id;
		this.mPw = pw;
	}

	// 정보수정
	public Member(String mPw, String mNumber, String mNickname) {
		this.mPw = mPw;
		this.mNumber = mNumber;
		this.mNickname = mNickname;
	}
	
	
	
}
