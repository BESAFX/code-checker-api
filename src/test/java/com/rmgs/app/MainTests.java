package com.rmgs.app;

import com.rmgs.app.model.Project;
import com.rmgs.app.sonar.SonarController;
import com.rmgs.app.utils.CommandUtils;
import com.rmgs.app.utils.StreamGobbler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTests {

    private final static Logger LOG = LoggerFactory.getLogger(MainTests.class);

    @Value(value = "${yasca.location}")
    private String yascaLocation;

    @Value(value = "${analysis.location}")
    private String analysisLocation;

    @Autowired
    private SonarController sonarController;

    @Test
    public void contextLoads() throws Exception {

//        CommandUtils.excecuteCommand(new String[]{"cmd.exe", "/c", "cd " + yascaLocation + " && yasca.bat " + "E:\\code-checker\\codeChecker\\codeChecker"});

//        ProcessBuilder builder = new ProcessBuilder();
//        if (CommandUtils.isWindows()) {
//            builder.command("cmd.exe", "/c", "yasca.bat --debug " + "E:\\crm-app");
//        } else {
//            builder.command("sh", "-c", "ls");
//        }
//        builder.directory(new File(yascaLocation));
//        Process process = builder.start();
//        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
//        Executors.newSingleThreadExecutor().submit(streamGobbler);
//        int exitCode = process.waitFor();
//        assert exitCode == 0;

        Project project = new Project();
        project.setName("sdfd");
        project.setProjKey("sddsd");
        sonarController.createProject(project);

    }

}

