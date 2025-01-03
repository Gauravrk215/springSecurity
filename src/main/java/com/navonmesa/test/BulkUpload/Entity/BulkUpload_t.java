package com.realnet.BulkUpload.Entity;
 import lombok.*;
 import javax.persistence.*;
 import java.time.LocalDateTime;
 import java.util.*;

 @Entity 
 @Data
 public class    BulkUpload_t{ 

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String entity_name;
 private String description;
 private String rule_line;
 private boolean active;
 private String fileType;

 }