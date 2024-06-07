package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
//@SQLRestriction("delete_flg = false")
public class Report {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 日付
    @Column(nullable = false)
    private LocalDate report_date;

    // タイトル
    @Column(length =100, nullable = false)
    @Length(max = 100)
    private String title;

    //内容
    @Column(nullable = false, columnDefinition="LONGTEXT")
    private String content;

//    //社員番号  -> いらない　JoinColumnで指定しているので別途不要
//    @Column(length =10, nullable = false)
//    private String employee_code;

    //削除フラグ
    @Column(columnDefinition = "TINYINT", nullable = false)
    private boolean delete_flg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime created_at;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
    private Employee employee;

}
