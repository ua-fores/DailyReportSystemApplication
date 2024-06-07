package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report, UserDetail userDetail) {

        // 日付重複チェック
        List<Report> reportList = findAll();
        for (Report regReport: reportList) {

            if (regReport.getEmployee().getCode().equals(userDetail.getEmployee().getCode())
                    &&
                    regReport.getReport_date().equals(report.getReport_date())) {
                return ErrorKinds.DATECHECK_ERROR;
            }
        }

        report.setDelete_flg(false);
        report.setEmployee(userDetail.getEmployee());

        LocalDateTime now = LocalDateTime.now();
        report.setCreated_at(now);
        report.setUpdated_at(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

}
