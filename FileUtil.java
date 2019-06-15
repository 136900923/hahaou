package com.up72.sjfeng.util;

import com.up72.sjfeng.model.PrepaidRecords;
import com.up72.sjfeng.model.PrepaidRecordsV;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    private static String regEx_style=" style=\"(.*?)\"";
    private static String regEx_class=" class=\"(.*?)\"";
    /*
    去除style和class样式
     */
    public static String delectStyle(String str){
        Pattern p_script = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(str);
        str = m_script.replaceAll(""); // 过滤style标签
        Pattern p_class = Pattern.compile(regEx_class, Pattern.CASE_INSENSITIVE);
        Matcher m_class = p_class.matcher(str);
        str = m_class.replaceAll(""); // 过滤style标签
        return str;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 下载
     */

    public static void downFile(HttpServletRequest req, HttpServletResponse resp){
        String filePath =  req.getSession().getServletContext().getRealPath("/common/tempFile/model.xls");;
        resp.setContentType("text/html");

        File file = new File(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);// 得到文件名
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            resp.setContentType("application/octet-stream");
            resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            System.out.println(fileName);
            String len = String.valueOf(file.length());
            resp.setHeader("Content-Length", len);// 设置内容长度
            OutputStream out = resp.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void creatExcel(HttpServletRequest req,String[] headers,List<?> list){
        OutputStream out = null;
        try {
            String path = req.getSession().getServletContext().getRealPath("/common/tempFile/");
            out = new FileOutputStream(path+"model.xls");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     //   String[] headers ={  "订单号", "充值金额","充值时间","账户余额","充值类型","充值凭证" };
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("1");
       // sheet.protectSheet("1234");//设置Excel保护密码
        // 设置表格默认列宽度为15个字节
     // sheet.setDefaultColumnWidth((short) 20);
        sheet.setDefaultColumnWidth(30);
       // sheet.DefaultRowHeight=30*20;
        // sheet.setColumnWidth(0, 20 * 256);
        //sheet.setColumnWidth(1, 100);
       // sheet.autoSizeColumn(1, true);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("leno");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        for(int n =0 ; n<list.size(); n++){
            row = sheet.createRow(n+1);
            Object one = list.get(n);
            Field[] fields =one.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
                Class tCls = one.getClass();
                try {
                    Method getMethod = tCls.getMethod(getMethodName,new Class[]{});
                    Object value = getMethod.invoke(one, new Object[]{});
                    String textValue = null;
                    textValue = value.toString();
                    if (textValue != null){
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()){
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString( textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
