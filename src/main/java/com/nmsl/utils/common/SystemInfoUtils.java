package com.nmsl.utils.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 系统消息工具类
 *
 * @author paracosm
 */
public class SystemInfoUtils {
    private static final int OSHI_WAIT_SECOND = 1000;
    private static SystemInfo systemInfo = new SystemInfo();
    private static HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private static OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

    public static JSONObject getCpuInfo () {
        JSONObject cpuInfo = new JSONObject();
        CentralProcessor processor = hardware.getProcessor();
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        //cpu核数
        cpuInfo.put("cpu核数", processor.getLogicalProcessorCount());
        //cpu系统使用率
        cpuInfo.put("cpu系统使用率", new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
        //cpu用户使用率
        cpuInfo.put("cpu用户使用率", new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
        //cpu当前等待率
        cpuInfo.put("cpu当前等待率", new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
        //cpu当前使用率
        cpuInfo.put("cpu当前使用率", new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
        return cpuInfo;
    }

    /**
     * 系统jvm信息
     */
    public static JSONObject getJvmInfo () {
        JSONObject cpuInfo = new JSONObject();
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        long jvmTotalMemoryByte = runtime.totalMemory();
        long freeMemoryByte = runtime.freeMemory();
        //jvm总内存
        cpuInfo.put("jvm总内存", formatByte(runtime.totalMemory()));
        //空闲空间
        cpuInfo.put("空闲空间", formatByte(runtime.freeMemory()));
        //jvm最大可申请
        cpuInfo.put("jvm最大可申请", formatByte(runtime.maxMemory()));
        //vm已使用内存
        cpuInfo.put("vm已使用内存", formatByte(jvmTotalMemoryByte - freeMemoryByte));
        //jvm内存使用率
        cpuInfo.put("jvm内存使用率", new DecimalFormat("#.##%").format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        //jdk版本
        cpuInfo.put("jdk版本", props.getProperty("java.version"));
        //jdk路径
        cpuInfo.put("jdk路径", props.getProperty("java.home"));
        return cpuInfo;
    }

    /**
     * 系统内存信息
     */
    public static JSONObject getMemInfo () {
        JSONObject cpuInfo = new JSONObject();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();
        //总内存
        cpuInfo.put("总内存", formatByte(totalByte));
        //使用
        cpuInfo.put("使用", formatByte(totalByte - acaliableByte));
        //剩余内存
        cpuInfo.put("剩余内存", formatByte(acaliableByte));
        //使用率
        cpuInfo.put("使用率", new DecimalFormat("#.##%").format((totalByte - acaliableByte) * 1.0 / totalByte));
        return cpuInfo;
    }

    /**
     * 系统盘符信息
     */
    public static JSONArray getSysFileInfo () {
        JSONObject cpuInfo;
        JSONArray sysFiles = new JSONArray();
        FileSystem fileSystem = operatingSystem.getFileSystem();

//        OSFileStore[] fsArray = fileSystem.getFileStores();
        OSFileStore[] fsArray = fileSystem.getFileStores().toArray(new OSFileStore[0]);

        for (OSFileStore fs : fsArray) {
            cpuInfo = new JSONObject();
            //盘符路径
            cpuInfo.put("盘符路径", fs.getMount());
            //盘符类型
            cpuInfo.put("盘符类型", fs.getType());
            //文件类型
            cpuInfo.put("文件类型", fs.getName());
            //总大小
            cpuInfo.put("总大小", formatByte(fs.getTotalSpace()));
            //剩余大小
            cpuInfo.put("剩余大小", formatByte(fs.getUsableSpace()));
            //已经使用量
            cpuInfo.put("已经使用量", formatByte(fs.getTotalSpace() - fs.getUsableSpace()));
            if (fs.getTotalSpace() == 0) {
                //资源的使用率
                cpuInfo.put("usage", 0);
            } else {
                cpuInfo.put("usage", new DecimalFormat("#.##%").format((fs.getTotalSpace() - fs.getUsableSpace()) * 1.0 / fs.getTotalSpace()));
            }
            sysFiles.add(cpuInfo);
        }
        return sysFiles;
    }

    /**
     * 系统信息
     */
    public static JSONObject getSysInfo () throws UnknownHostException {
        JSONObject cpuInfo = new JSONObject();
        Properties props = System.getProperties();
        //操作系统名
        cpuInfo.put("操作系统名", props.getProperty("os.name"));
        //系统架构
        cpuInfo.put("系统架构", props.getProperty("os.arch"));
        //服务器名称
        cpuInfo.put("服务器名称", InetAddress.getLocalHost().getHostName());
        //服务器Ip
        cpuInfo.put("服务器Ip", InetAddress.getLocalHost().getHostAddress());
        //项目路径
        cpuInfo.put("项目路径", props.getProperty("user.dir"));
        return cpuInfo;
    }

    /**
     * 所有系统信息
     */
    public static JSONObject getInfo () throws UnknownHostException {
        JSONObject info = new JSONObject();
        info.put("系统盘符信息", getSysFileInfo());
        info.put("系统信息", getSysInfo());
        info.put("jvm信息", getJvmInfo());
        info.put("内存信息", getMemInfo());
        info.put("cpu信息", getCpuInfo());
        return info;
    }

    /**
     * 单位转换
     */
    private static String formatByte (long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    /**
     * 获取当前时间，格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }
}