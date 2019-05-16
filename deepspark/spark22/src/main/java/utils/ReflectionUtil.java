package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/15 15:22
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /*创建实例*/
    public static Object newInstance(Class<?> cls){
        Object instance;
        try{
            instance = cls.newInstance();
        } catch(Exception e){
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /*创建实例*/
    public static Object newInstance(String className){
        Class<?> cls = ClassUtil.loadClass(className);
        return newInstance(cls);
    }

    /*反射调用方法*/
    public static Object invokeMethod(Object obj, Method method, Object... args){
        Object result;
        try{
            method.setAccessible(true);
            result = method.invoke(obj,args);
        } catch (Exception e) {
            LOGGER.error("invoke method fail", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /*设置成员变量的值*/
    public static void setField(Object obj, Field field,Object value){
        try{
            field.setAccessible(true);
            field.set(obj,value);
        }catch(Exception e){
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }

}
