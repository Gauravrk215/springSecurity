package com.realnet.Dashboard_builder.Entity;
 import lombok.*;
 import javax.persistence.*;
 import java.time.LocalDateTime;
 import java.util.*;

 @Entity 
 @Data
 public class    DashboardSchedule_t{ 

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String cron;
 private String every;
 private String gateway;
 private String template;
 private Date startTime;
 private Date endTime;
 private String attachment;
 private String sendTo;
 private String gatewaydone;
 private String cc;
 private String replacementString;
 private String type;

 }