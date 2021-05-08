package com.nmsl.utils.quartz;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.joda.time.DateTime;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created with IntelliJ IDEA.
 * Time: 2021/4/24 10:50
 * Description: MySql使用Quartz自动备份
 * @author paracosm
 */
@Component("mysqlService")
@Configuration
public class MysqlUtil {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MysqlUtil.class);

    /**
     * 文件路径
     */
//    private final static String MYSQL_DUMP = "/usr/local/mysql/bin/mysqldump";

    /**
     * 用ThreadLocal记录过程时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 导出数据库
     */
    public void exportDataBase() {
        //记录开始时间
        startTime.set(System.currentTimeMillis());

        //获取配置
        logger.info(String.valueOf(DateTime.now()));
        logger.info("开始备份数据库");
        String username = LoadConfig.getProperty("jdbc.username_dev");
        String password = LoadConfig.getProperty("jdbc.password_dev");
        String database = LoadConfig.getProperty("jdbc.database");
        String host = LoadConfig.getProperty("jdbc.host_dev");
        String mysqlDumpPath = LoadConfig.getProperty("mysql.dump");
        //系统类型
        String os = System.getProperty("os.name");
        String file_path = null;

        /*
        if (os.toLowerCase().startsWith("win")) {       //根据系统类型
            file_path = System.getProperty("user.dir") + "\\sql\\";
        } else {
            file_path = System.getProperty("user.dir") + "/sql/";//保存的路径
        }
        */

        String property = System.getProperty("blog.path");
        if (property == null) {
            file_path = "/home/blog/" + "bak";
        } else {
            file_path = System.getProperty("blog.path") + "sql";
        }
//        String file_name = "/blog" + DateTime.now().toString("yyyyMMddHHmmss") + ".sql";
        String file_name = "/blog" + LocalDate.now() + ".sql";
        String file = file_path + file_name;
        logger.info("文件路径和文件名为：: " + file);

        //服务器
        String s_host = LoadConfig.getProperty("server.host");
        Integer s_port = LoadConfig.getIntProperty("server.port");
        String s_username = LoadConfig.getProperty("server.username");
        String s_password = LoadConfig.getProperty("server.password");
        try {
            //拼接sql语句
            StringBuilder sb = new StringBuilder();
//            /usr/local/mysql/bin/mysqldump -u ? -p? -h 127.0.0.1 blog >/home/blog/bak/blog20210427235400.sql
            sb.append(mysqlDumpPath)
                    .append(" -u ").append(username)
                    .append(" -p").append(password)
                    .append(" -h ").append(host)
                    .append(" ")
                    .append(database)
                    .append(" >")
                    .append(file);
            String sql = sb.toString();
            logger.info("sql语句为:"+sql);

            //连接服务器
            Connection connection = new Connection(s_host, s_port);
            connection.connect();
            boolean isAuth = connection.authenticateWithPassword(s_username, s_password);
            if (!isAuth) {
                logger.error("服务器验证失败");
            }

            Session session = connection.openSession();
            //cmd –在远程主机上执行的命令。
            session.execCommand(sql);
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            //输出消耗时间
            logger.info("备份耗时 : " + (System.currentTimeMillis() - startTime.get())+"ms");

            //关闭流
            session.close();
            connection.close();
            stdout.close();
            br.close();

            //防止内存泄露，要及时删除
            startTime.remove();
            //输出日志
            logger.info("备份成功,时间为 : {}", LocalTime.now());
        } catch (Exception e) {
            logger.error("备份过程中出现错误: {}", e);
//            MailUtil mailUtil = new MailUtil();
//            mailUtil.sendMail("数据库备份错误", "时间为：" + LocalDate.now() + "。。。错误日志：" + e.getLocalizedMessage(), "paracosm@foxmail.com");
        }
    }
}