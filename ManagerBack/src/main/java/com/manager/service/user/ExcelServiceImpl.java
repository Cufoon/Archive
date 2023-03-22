package com.manager.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.manager.dao.StudentEnterpriseDAO;
import com.manager.dao.TableIdentifyDAO;
import com.manager.dao.UserInfoDAO;
import com.manager.entity.StudentEnterprise;
import com.manager.entity.TableIdentify;
import com.manager.entity.UserInfo;
import com.manager.vo.user.ExcelUploadVO;
import com.manager.vo.user.UserVO;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private final UserInfoDAO userInfoDAO;

    private final TableIdentifyDAO tableIdentifyDAO;

    private final StudentEnterpriseDAO studentEnterpriseDAO;

    public ExcelServiceImpl(UserInfoDAO userInfoDAO, TableIdentifyDAO tableIdentifyDAO, StudentEnterpriseDAO studentEnterpriseDAO) {

        this.userInfoDAO = userInfoDAO;

        this.tableIdentifyDAO = tableIdentifyDAO;

        this.studentEnterpriseDAO = studentEnterpriseDAO;
    }

    @Override
    public ExcelUploadVO saveExcel(InputStream inputStream) {
//        ExcelReader reader = ExcelUtil.getReader("D:\\mould.xlsx");
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("学/工号", "id");
        reader.addHeaderAlias("姓名", "name");
        reader.addHeaderAlias("用户类型（学生1，老师2）", "type");
        reader.addHeaderAlias("账户状态（启用1，禁用0）", "state");
        reader.addHeaderAlias("隶属班级/学院", "className");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("年龄", "age");
        reader.addHeaderAlias("手机", "phone");
        reader.addHeaderAlias("邮箱", "email");
        List<UserInfo> excelList = reader.readAll(UserInfo.class);
        List<UserVO> excelUploadVOList = new ArrayList<>();

        for (UserInfo userInfo : excelList) {
            UserInfo user = userInfoDAO.findAllById(userInfo.getId());
            if(user == null){
                System.out.println(userInfo);
                userInfo.setPassword(userInfo.getId());
                userInfo.setPeriodId(userInfo.getId().substring(0, 4));
                userInfoDAO.saveAndFlush(userInfo);
                UserVO userVO = new UserVO(
                        userInfo.getId(),
                        userInfo.getName(),
                        true
                );
                excelUploadVOList.add(userVO);
            }
            else {
                UserVO userVO = new UserVO(
                       userInfo.getId(),
                       userInfo.getName(),
                       false
                );
                excelUploadVOList.add(userVO);
            }
        }
        ExcelUploadVO excelUploadVO = new ExcelUploadVO();
        excelUploadVO.setUserVOList(excelUploadVOList);
        return excelUploadVO;
    }

    @Override
    public OutputStream giveExcel(OutputStream outputStream, String studengID) {
        UserInfo userInfo = userInfoDAO.findAllById(studengID);
        TableIdentify tableIdentify = tableIdentifyDAO.findByStudentId(studengID);
        if (tableIdentify == null) {
            tableIdentify = new TableIdentify();
        }
        StudentEnterprise studentEnterprise = studentEnterpriseDAO.findByStudentId(studengID);
        if (studentEnterprise == null) {
            studentEnterprise = new StudentEnterprise();
        }
        String sex = "男";
        if (userInfo.getSex() == "F") {
            sex = "女";
        }
        List<String> row1 = CollUtil.newArrayList("专业班级", userInfo.getClassName(), "学生姓名", userInfo.getName(), "性别", sex, "学号", studengID);
        List<String> row2 = CollUtil.newArrayList("实习起止日期", (tableIdentify.getStartDate() == null ? "   " : tableIdentify.getStartDate().toString()) + "至" + (tableIdentify.getEndDate() == null ? "   " : tableIdentify.getEndDate().toString()), "", "", "实习单位名称", studentEnterprise.getEnterpriseName() == null ? "" : studentEnterprise.getEnterpriseName(), "", "");
        List<String> row3 = CollUtil.newArrayList("参与项目名称", tableIdentify.getProject() == null ? "" : tableIdentify.getProject(), "", "", "", "", "实习城市", studentEnterprise.getEnterpriseCity() == null ? "" : studentEnterprise.getEnterpriseCity());
        List<String> row4 = CollUtil.newArrayList("个人小结", tableIdentify.getPersonalSummary() == null ? "" : tableIdentify.getPersonalSummary(), "", "", "", "", "", "");
        List<String> row5 = CollUtil.newArrayList("出勤情况", tableIdentify.getAttendance() == null ? "" : tableIdentify.getAttendance(), "", "", "", "", "", "");
        List<String> row6 = CollUtil.newArrayList("实习单位企业导师意见", tableIdentify.getEptSuggestions() == null ? "" : tableIdentify.getEptSuggestions(), "", "", "", "", "", "");
        List<String> row7 = CollUtil.newArrayList("实习单位意见", tableIdentify.getEpSuggestions() == null ? "" : tableIdentify.getEpSuggestions(), "", "", "", "", "", "");
        List<String> row8 = CollUtil.newArrayList("院内老师意见", tableIdentify.getTeacherSuggestions() == null ? "" : tableIdentify.getTeacherSuggestions(), "", "", "", "", "", "");
        String evaluation = "";
        switch (tableIdentify.getEvaluation() == null ? 0 : tableIdentify.getEvaluation()) {
            case 1:
                evaluation = "不合格";
                break;
            case 2:
                evaluation = "合格";
                break;
            case 3:
                evaluation = "中等";
                break;
            case 4:
                evaluation = "良好";
                break;
            case 5:
                evaluation = "优秀";
                break;
        }
        List<String> row9 = CollUtil.newArrayList("总评成绩", evaluation, "", "", "", "", "", "");
        List<String> row10 = CollUtil.newArrayList("备注", "", "", "", "", "", "", "");
        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5, row6, row7, row8, row9, row10);

        // 通过工具类创建writer，默认创建xls格式
//        ExcelWriter writer = ExcelUtil.getWriter();
        //创建xlsx格式的
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(row1.size() - 1, "湖南大学学生校外实践鉴定表");
        writer.merge(2, 2, 1, 3, "", false);
        writer.merge(2, 2, 5, 7, "", false);
        writer.merge(3, 3, 1, 5, "", false);
        for (int i = 4; i < 11; i++) {
            writer.merge(i, i, 1, 7, "", false);
        }
        writer.getStyleSet().setAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        writer.getHeadCellStyle().setAlignment(HorizontalAlignment.CENTER);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        writer.setCurrentRow(1);
        writer.setColumnWidth(0, 25);
        for (int i = 1; i < 8; i++) {
            writer.setColumnWidth(i, 13);
        }
        writer.setRowHeight(0, 30);
        for (int i = 1; i < 4; i++) {
            writer.setRowHeight(i, 45);
        }
        for (int i = 4; i < 8; i++) {
            writer.setRowHeight(i, 200);
        }
        for (int i = 8; i < 11; i++) {
            writer.setRowHeight(i, 45);
        }
//        out为OutputStream，需要写出到的目标流
        writer.flush(outputStream, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流

        // 关闭writer，释放内存
        writer.close();
        return outputStream;
    }
}
