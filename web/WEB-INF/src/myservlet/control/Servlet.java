package myservlet.control;

import mybean.data.Bean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by lei02 on 2019/4/13.
 */

public class Servlet extends HttpServlet {
    @Override
    public void init(ServletConfig config)
        throws ServletException{
        super.init();
        //加载数据库的驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        //存储数据库的信息，并将它添加到 request 中
        Bean resultBean = null;
        try {
            resultBean = (Bean)request.getAttribute("resultBean");
            if (resultBean == null) {
                resultBean = new Bean();
                request.setAttribute("resultBean", resultBean);
            }
        } catch (Exception ex) {
            resultBean = new Bean();
            request.setAttribute("resultBean", resultBean);
            ex.printStackTrace();
        }

        //form 表单的 action 提交的内容，也就是 url 数据里面的内容
        String dataBase = request.getParameter("dataBase");
        String tableName = request.getParameter("tableName");
        //由表单提交的信息
        String nu = request.getParameter("number");
        String na = request.getParameter("name");
        String mt = request.getParameter("madeTime");
        String pr = request.getParameter("price");

        if (nu == null || nu.length() == 0) {
            fail(request, response, "添加数据失败，必须给出数据");
            return;
        }
        //将提交的信息存储到数据库中
        float p = Float.parseFloat(pr);
        String codition = "insert into " + tableName + " values (" + "'" + nu + "', '"
                + na +"', '" + mt + "', '" + pr + "')";

        Connection conn;    //建立连接
        Statement sql;      //sql 语句
        ResultSet rs;       //查询的结果集

        try {
            String url = "jdbc:mysql://127.0.0.1/" + dataBase
                    + "?user=root&password=021191&characterEncoding=utf-8";
            conn = DriverManager.getConnection(url);
            sql = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            //插入数据
            sql.executeUpdate(codition);
            //查询数据
            rs = sql.executeQuery("select * from " + tableName);
            //获取数据行数，列名
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnName = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnName[i] = metaData.getColumnName(i + 1);
            }
            resultBean.setColumnName(columnName);

            //跳转到最后一列
            rs.last();
            //获取最后一列的列数
            int rowNumber = rs.getRow();
            //将信息存储到 bean 中，需要先跳转到数据列最前面的前面，逐行获取数据
            String[][] tableRecord = new String[rowNumber][columnCount];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                for (int k = 0; k < columnCount; k++) {
                    tableRecord[i][k] = rs.getString(k + 1);
                }
                i++;
            }
            resultBean.setTableCord(tableRecord);
            //关闭连接
            conn.close();
            //重定向显示数据页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("showRecord.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail(request, response, "添加记录失败：" + ex.toString());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        doPost(request, response);
    }
    //显示错误信息
    public void fail(HttpServletRequest request, HttpServletResponse response, String info){
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.print("<html> <body>");
            out.print("<h2>" + info + "</h2>");
            out.print("返回");
            out.print("<a href=index.jsp>输入记录</a>");
            out.print("</body> </html>");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
