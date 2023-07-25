package com.xy.dash.utli;

import java.io.FileWriter;

/**
 * @Author: xiangwei
 * @Date: 2022/10/24 15:08
 * @Description
 **/
public interface XyConstant {
    String FILE_PATH = FileWriter.class.getResource("/").getPath();

    String PATH = FILE_PATH.substring(0, FILE_PATH.indexOf("xy-dash")).concat("GeneratingCode/");

    String LOG_PATH = PATH.substring(1) + "%s/%s.log";

    String CMD_CREATE_JAY = "cmd /c cd " + PATH.substring(1) + "%s" + " && mvn clean package -Dmaven.test.skip=true -Dcheckstyle.skip=true";


    String CMD_RUN_JAY =
            "cmd /c cd " + PATH.substring(1) + "%s/target" + " && java -jar data-migration-1.0-SNAPSHOT.jar >" + LOG_PATH + " 2>&1 &";

    String LINUX_CREATE_JAY = "cmd /c cd " + PATH.substring(1) + "%s" + " && mvn clean package -Dmaven.test.skip=true -Dcheckstyle.skip=true";

    String LINUX_RUN_JAY =
            "cmd /c cd " + PATH.substring(1) + "%s/target" + " && nohup java -jar data-migration-1.0-SNAPSHOT.jar &>" + LOG_PATH + " &";
}
