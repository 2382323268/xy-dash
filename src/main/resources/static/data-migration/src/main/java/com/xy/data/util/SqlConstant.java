package com.xy.data.util;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xy.data.annotation.Inset;
import com.xy.data.annotation.SqlServer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Author: xiangwei
 * @Date: 2022/7/20 15:07
 * @Description
 **/
public class SqlConstant {
    private static final String SQL = "insert into %s (%s) VALUES ";
    public static Map<Class, String> sqlMap = new HashMap<>();
    public static Map<Class, String> sqlSelect = new HashMap<>();
    public static Map<Class, Field[]> fieldMap = new HashMap<>();
    public static Map<String, Method> methodMap = new HashMap<>();

    static {
        getClasses("com.xy.data.entity", null).forEach(value -> {
            if (value.isAnnotationPresent(Inset.class)) {
                Field[] declaredFields = value.getDeclaredFields();
                fieldMap.put(value, declaredFields);

                StringBuilder field = new StringBuilder();
                for (Field declaredField : declaredFields) {
                    if (declaredField.isAnnotationPresent(TableId.class)) {
                        field.append(declaredField.getAnnotation(TableId.class).value()).append(", ");
                        continue;
                    }
                    if (declaredField.isAnnotationPresent(TableField.class)) {
                        field.append(declaredField.getAnnotation(TableField.class).value()).append(", ");
                    }
                }
                sqlMap.put(value, String.format(SQL, value.getDeclaredAnnotation(TableName.class).value(),
                        field.substring(0, field.length() - 2)));
            }

            if (value.isAnnotationPresent(SqlServer.class)) {
                Field[] declaredFields = value.getDeclaredFields();
                StringBuilder field = new StringBuilder();
                for (Field declaredField : declaredFields) {
                    if (declaredField.isAnnotationPresent(TableId.class)) {
                        field.append(declaredField.getAnnotation(TableId.class).value()).append(" ").append(declaredField.getName()).append(", ");
                        continue;
                    }
                    if (declaredField.isAnnotationPresent(TableField.class)) {
                        field.append(declaredField.getAnnotation(TableField.class).value()).append(" ").append(declaredField.getName()).append(", ");
                    }
                }
                sqlSelect.put(value, field.substring(0, field.length() - 2));
            }
        });
        for (Method declaredMethod : DistributeTransactionService.class.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(DS.class)) {
                methodMap.put(declaredMethod.getAnnotation(DS.class).value(), declaredMethod);
            }
        }
    }

    /**
     * 从包package中获取所有的Class
     *
     * @return
     */
    public static List<Class<?>> getClasses(String packageName, Class<? extends Annotation> annotation) {

        //第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes, annotation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes, Class<? extends Annotation> annotation) {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes, annotation);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> aClass = Class.forName(packageName + '.' + className);
                    //添加到集合中去
                    if (annotation != null) {
                        boolean annotationPresent = aClass.isAnnotationPresent(annotation);
                        if (annotationPresent) {
                            classes.add(aClass);
                        }
                    } else {
                        classes.add(aClass);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
