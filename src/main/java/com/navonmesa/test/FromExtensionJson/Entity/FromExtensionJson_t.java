package com.realnet.FromExtensionJson.Entity;
 import lombok.*;
 import javax.persistence.*;
 import java.time.LocalDateTime;
 import java.util.*;

 @Entity 
 @Data
 public class    FromExtensionJson_t{ 

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String form_code;
 private String account_id;
 @Lob
 private String jsonObject;

 }