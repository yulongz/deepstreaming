package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/14 16:57
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /*获取类加载器*/
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /*加载类*/
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try{
            cls = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /*加载类，默认初始化*/
    public static Class<?> loadClass(String className){
        return loadClass(className, true);
    }

    //获取指定包名下的所有类
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
//                        try {
//                            String packagePath = url.getClass().getResource("").toURI().getPath();
//                        } catch (URISyntaxException e) {
//                            e.printStackTrace();
//                        }
                        String packagePath = url.getPath().replaceAll("%20"," ");
                        addClass(classSet,packagePath,packageName);
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error("get class set failure", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return ((file.isFile()) && file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for(File file:files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)){
                    className = packageName + "." + className;
                }
                doAddClass(classSet,className);
            }else{
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(packagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)){
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className,false);
        classSet.add(cls);
    }
}
