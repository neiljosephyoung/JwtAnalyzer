package com.ny.jwtanalyzer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OshiHelper {
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

    public OshiHelper() {
        log.info("os family: {}", operatingSystem.getFamily());
    }

    public static double getCpuLoad() throws InterruptedException {
        var processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        TimeUnit.SECONDS.sleep(1);

        return processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    }

    public static String getMemoryUsage() {
        return FileUtils.byteCountToDisplaySize(systemInfo.getHardware().getMemory().getTotal() - systemInfo.getHardware().getMemory().getAvailable());
    }
}
