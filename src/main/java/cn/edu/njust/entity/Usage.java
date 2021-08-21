package cn.edu.njust.entity;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author TYX
 * @name Usage
 * @description usage of resource
 * @createTime 2021/6/25 14:54
 **/
public class Usage {
    private String CPUAmountStr = "0m";//形式上是100m
    private double CPUAmount = 0;//形式上是100
    private double CPURatio = 0;
    private String memoryStr = "0Mi";//形式上是70Mi
    private double memory = 0;//形式上是70
    private double memoryRatio = 0;
    private double Disk = 0;
    private double diskRatio = 0;

    public String getCPUAmountStr() {
        return CPUAmountStr;
    }

    public double getCPUAmount() {
        return CPUAmount;
    }

    public double getCPURatio() {
        return CPURatio;
    }

    public String getMemoryStr() {
        return memoryStr;
    }

    public double getMemory() {
        return memory;
    }

    public double getMemoryRatio() {
        return memoryRatio;
    }

    public double getDisk() {
        return Disk;
    }

    public double getDiskRatio() {
        return diskRatio;
    }

    public void setCPUAmountStr(String CPUAmountStr) {
        this.CPUAmountStr = CPUAmountStr;
    }

    public void setCPUAmount(double CPUAmount) {
        this.CPUAmount = CPUAmount;
    }

    public void setCPURatio(double CPURatio) {
        this.CPURatio = CPURatio;
    }

    public void setMemoryStr(String memoryStr) {
        this.memoryStr = memoryStr;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public void setMemoryRatio(double memoryRatio) {
        this.memoryRatio = memoryRatio;
    }

    public void setDisk(double disk) {
        Disk = disk;
    }

    public void setDiskRatio(double diskRatio) {
        this.diskRatio = diskRatio;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "CPUAmountStr='" + CPUAmountStr + '\'' +
                ", CPUAmount=" + CPUAmount +
                ", CPURatio=" + CPURatio +
                ", memoryStr='" + memoryStr + '\'' +
                ", memory=" + memory +
                ", memoryRatio=" + memoryRatio +
                ", Disk=" + Disk +
                ", diskRatio=" + diskRatio +
                '}';
    }
}
