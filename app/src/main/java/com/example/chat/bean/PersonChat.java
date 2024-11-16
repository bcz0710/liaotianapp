package com.example.chat.bean;


import java.io.Serializable;

public class PersonChat implements Serializable {
   private Integer fromUserId;
   private String fromUserName;
   private String fromUserPhoto;
   private Integer toUserId;
   private String toUserName;
   private String toUserPhoto;
   private String content;
   private String date;
   private Integer status;

   public Integer getFromUserId() {
      return fromUserId;
   }

   public void setFromUserId(Integer fromUserId) {
      this.fromUserId = fromUserId;
   }

   public String getFromUserName() {
      return fromUserName;
   }

   public void setFromUserName(String fromUserName) {
      this.fromUserName = fromUserName;
   }

   public String getFromUserPhoto() {
      return fromUserPhoto;
   }

   public void setFromUserPhoto(String fromUserPhoto) {
      this.fromUserPhoto = fromUserPhoto;
   }

   public Integer getToUserId() {
      return toUserId;
   }

   public void setToUserId(Integer toUserId) {
      this.toUserId = toUserId;
   }

   public String getToUserName() {
      return toUserName;
   }

   public void setToUserName(String toUserName) {
      this.toUserName = toUserName;
   }

   public String getToUserPhoto() {
      return toUserPhoto;
   }

   public void setToUserPhoto(String toUserPhoto) {
      this.toUserPhoto = toUserPhoto;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      this.status = status;
   }



   public PersonChat(Integer fromUserId, String fromUserName, String fromUserPhoto, Integer toUserId, String toUserName, String toUserPhoto, String content, String date, Integer status) {
      this.fromUserId = fromUserId;
      this.fromUserName = fromUserName;
      this.fromUserPhoto = fromUserPhoto;
      this.toUserId = toUserId;
      this.toUserName = toUserName;
      this.toUserPhoto = toUserPhoto;
      this.content = content;
      this.date = date;
      this.status = status;
   }

   public PersonChat(Integer fromUserId, String fromUserName, String fromUserPhoto, Integer toUserId, String toUserName, String toUserPhoto) {
      this.fromUserId = fromUserId;
      this.fromUserName = fromUserName;
      this.fromUserPhoto = fromUserPhoto;
      this.toUserId = toUserId;
      this.toUserName = toUserName;
      this.toUserPhoto = toUserPhoto;
   }

}
