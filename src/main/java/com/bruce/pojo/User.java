package com.bruce.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private long userId;
  private String userLoginName;
  private String userPhone;
  private long userAge;
  private String userPwd;
  private String userName;
  private long state;
  private String createTime;
  private long delState;

  public User(String userLoginName, String userPhone, Integer userAge, String userPwd, String userName) {
    this.userLoginName = userLoginName;
    this.userPhone = userPhone;
    this.userAge = userAge;
    this.userPwd = userPwd;
    this.userName = userName;
  }

}
