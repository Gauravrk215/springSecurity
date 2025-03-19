package com.realnet.Accesstype_back.Entity;
 import lombok.*;
 import javax.persistence.*;
 import java.time.LocalDateTime;
 import java.util.*;

 @Entity 
 @Data
 public class    Accesstype{ 

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;
 private String description;
 private boolean defaultvalue;

 }