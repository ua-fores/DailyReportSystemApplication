package com.techacademy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());

        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{ID}/")
    public String detail(@PathVariable("ID") String id, Model model) {

        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(Report report, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        model.addAttribute("report", report);
        model.addAttribute("name", userDetail.getEmployee().getName());
        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail,
            Model model) {

        // 日付重複チェック
        List<Report> reportList = reportService.findAll();
        for (Report regReport : reportList) {
            if (regReport.getReportDate().equals(report.getReportDate())
                    && regReport.getEmployee().getCode().equals(userDetail.getEmployee().getCode())) {

                model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                        ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            }
        }

        // 入力チェック
        if (res.hasErrors()) {
            return create(report, userDetail, model);
        }

        ErrorKinds result = reportService.save(report, userDetail);

        return "redirect:/reports";

    }

    // 日報更新画面
    @GetMapping(value = "/{ID}/update")
    public String edit(@PathVariable(name = "ID", required = false) String id, Report report, Model model) {
        if (report.getId() == null) {
            model.addAttribute("report", reportService.findById(id));
        } else {
            model.addAttribute("report", report);
        }
        return "reports/update";
    }

    // 日報更新処理
    @PostMapping(value = "/{ID}/update")
    public String update(@PathVariable("ID") String id, @Validated Report report, BindingResult res, Model model) {

        // データチェック用に必要なレポート情報をセット
        report.setId(Integer.valueOf(reportService.findById(id).getId()));
        report.setEmployee(reportService.findById(id).getEmployee());

        // 日付重複チェック
        List<Report> reportList = reportService.findAll();
        for (Report regReport : reportList) {

            if (!(regReport.getId().equals(report.getId())) && regReport.getReportDate().equals(report.getReportDate())
                    && regReport.getEmployee().getCode().equals(report.getEmployee().getCode())) {

                model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                        ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            }
        }

        // 入力チェック
        if (res.hasErrors()) {
            return edit(null, report, model);
        }

        ErrorKinds result = reportService.save(id, report);

        return "redirect:/reports";
    }

    // 日報削除処理
    @PostMapping(value = "/{ID}/delete")
    public String delete(@PathVariable("ID") String id, Model model) {

        ErrorKinds result = reportService.delete(id);

        return "redirect:/reports";
    }

}
