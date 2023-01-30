package com.xy.dash.service.impl;

import com.xy.dash.enums.TemplatePathType;
import com.xy.dash.service.TemplatesService;
import com.xy.dash.vo.TemplatesAdd;
import com.xy.dash.vo.TemplatesDataSources;
import com.xy.dash.vo.TemplatesFields;
import com.xy.dash.vo.TemplatesTables;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiangwei
 * @Date: 2022/11/14 15:59
 * @Description
 **/
@Service
@Slf4j
public class TemplatesServiceImpl implements TemplatesService {
    @Override
    public void create(TemplatesAdd add) {
        String path = FileWriter.class.getResource("/").getPath();
        String rootPath = path.substring(0, path.indexOf("xy-dash")).concat("GeneratingCode/");
        String name = add.getFileName();

        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Velocity.init(prop);
        VelocityContext context = new VelocityContext();
        context.put("xy", add);

        // 删除文件
        deleteFile(new File(rootPath.concat(name)));
        // 复制固定代码到指定目录
        copyAllFinalFiles(rootPath, path, name);
        // 根据模板生成代码到指定目录
        createAllFiles(context, rootPath.concat(name));

    }

    private void createAllFiles(VelocityContext context, String rootPath) {
        TemplatesAdd add = (TemplatesAdd) context.get("xy");
        List<TemplatesDataSources> templatesDataSources = add.getMigrationDataSources();
        List<TemplatesTables> templatesTables = add.getTemplatesTables();

        for (TemplatePathType e : TemplatePathType.values()) {

            // 生成配置代码
            if (e.isConfig()) {
                createFileByTemplate(e.getKey(), rootPath.concat(e.getValue()), context);
            }

            if (e.isBase()) {
                // 创建base目录
                templatesDataSources.forEach(templatesDataSource -> {
                    String path = String.format(rootPath.concat(e.getValue()), templatesDataSource.getUniqueName());
                    new File(path).mkdirs();
                });

                /**
                 * 需求：迁移表可能存在多个数据，但只需要生成一个实体类，同时需要统计所有数据需要的字段
                 * 1.根据表名+数据源名称去重复
                 * 2.重新计算字段数据
                 */
                List<TemplatesTables> collect = templatesTables.stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TemplatesTables::getEntityName))),
                                ArrayList::new)
                );
                Map<String, List<TemplatesTables>> map = templatesTables.stream().collect(Collectors.groupingBy(TemplatesTables::getEntityName));
                collect.forEach(v -> {
                    List<TemplatesTables> tables = map.get(v.getEntityName());
                    if (tables.size() > 1) {
                        // 拿到所有字段 然后根据字段名称去重复
                        List<TemplatesFields> templatesFields = tables.stream().map(TemplatesTables::getTemplatesFields).flatMap(Collection::stream).collect(
                                Collectors.collectingAndThen(
                                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TemplatesFields::getFieldName))),
                                        ArrayList::new)
                        );
                        v.setTemplatesFields(templatesFields);
                    }
                });
                //生成基础代码
                createFileByTemplateCommon(e, collect, rootPath);
            }

            if (e.isHandler()) {
                //生成handler代码
                List<TemplatesTables> handler = templatesTables.stream().filter(TemplatesTables::getMigration).collect(Collectors.toList());
                createFileByTemplateCommon(e, handler, rootPath);
            }

        }

    }

    private void createFileByTemplateCommon(TemplatePathType e, List<TemplatesTables> templatesTables, String rootPath) {
        templatesTables.stream().forEach(templatesTable -> {
            VelocityContext contexts = new VelocityContext();
            contexts.put("templatesTable", templatesTable);
            StringBuilder path = new StringBuilder(String.format(rootPath.concat(e.getValue()), templatesTable.getUniqueName()));
            path.append(templatesTable.getName(e)).append(".java");
            createFileByTemplate(e.getKey(), path.toString(), contexts);
        });
    }

    private void createFileByTemplate(String vms, String path, VelocityContext context) {
        Template template = Velocity.getTemplate(vms);
        FileWriter sw = null;
        try {
            sw = new FileWriter(path);
            template.merge(context, sw);
            sw.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据模板生成{}失败！", vms.substring(vms.indexOf("/") + 1, vms.indexOf(".")));
        }
    }

    private void copyAllFinalFiles(String savePath, String path, String name) {
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File sourceDir = new File(path.concat("static"));
        try {
            copyAllFinalFiles(sourceDir, saveDir, name);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("复制固定代码失败！");
        }
    }

    private void copyAllFinalFiles(File sourceDir, File saveDir, String name) throws IOException {
        //如果是文件
        if (sourceDir.isFile()) {
            FileInputStream fis = new FileInputStream(sourceDir);
            String path = saveDir.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] b = new byte[1024 * 1024];
            int len = 0;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
        } else { //如果是文件夹
            File[] files = sourceDir.listFiles();
            if (files.length == 0) {
                String srcPath = sourceDir.getName();
                String descPath = (saveDir.getAbsolutePath().endsWith("\\") ? saveDir.getAbsolutePath() : saveDir.getAbsolutePath() + "\\" + srcPath);
                File newFile = new File(descPath);
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }
            } else {
                //如果文件夹不为空
                for (File f : files) {
                    String srcPath = f.getName();
                    String descPath = "";
                    if (f.isFile()) {
                        descPath = saveDir.getAbsolutePath() + "\\" + f.getName();
                    }
                    if (f.isDirectory()) {
                        descPath = (saveDir.getAbsolutePath().endsWith("\\") ? saveDir.getAbsolutePath() : saveDir.getAbsolutePath() + "\\" + srcPath);
                    }
                    if (!descPath.contains(name)) {
                        descPath = descPath.replace("data-migration", name);
                    }
                    File newFile = new File(descPath);
                    if (f.isDirectory()) {
                        if (!newFile.exists()) {
                            newFile.mkdirs();
                        }
                    }
                    //递归调用，此时拿到的newFile为目标路径，f为源路径
                    copyAllFinalFiles(f, newFile, name);
                }
            }
        }
    }


    public void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
