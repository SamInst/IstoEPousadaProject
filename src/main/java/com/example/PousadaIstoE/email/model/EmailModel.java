//package com.example.PousadaIstoE.email.model;
//
//import com.example.PousadaIstoE.email.enums.StatusEmail;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "tb_email")
//public class EmailModel {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;
//    private Long emailid;
//    private String ownerRef;
//    private String emailFrom;
//    private String emailTo;
//    private String subject;
//    @Column(columnDefinition = "TEXT")
//    private String text;
//    private LocalDateTime sendDateEmail;
//    private StatusEmail statusEmail;
//}
