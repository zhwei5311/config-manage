package com.bora.common.reflect;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-14 14:03:27
 *****/
public class ReflectUtil {

    /**
     * 将map对象转化成类对象
     * @param paramsValues 实际参数
     * @param keyToColumn 参数主键与类属性映射关系，主键是参数主键
     * @param tClass 待转化对象类
     * @param <T> 类
     * @return
     */
    private static final String EXT_KEY = "extInfo";
    public static  <T>  T mapToObject(Map<String,Object> paramsValues,Map<String,String> keyToColumn, Class<T> tClass){
        if(CollectionUtils.isEmpty(paramsValues)){
            return null;
        }
        try {
            T t = tClass.newInstance();
            Iterator<Map.Entry<String, Object>> iterator = paramsValues.entrySet().iterator();
            final boolean flag ;
            JSONObject extInfo = new JSONObject();
            if(keyToColumn.containsKey(EXT_KEY)){
                flag = true;
            }else {
                flag = false;
            }

            while(iterator.hasNext()){
                Map.Entry<String, Object> next = iterator.next();
                //判断该类是否有该属性，没有该属性则放到json对象中
                if(!keyToColumn.containsKey(next.getKey())){
                    if(flag) {
                        extInfo.put(next.getKey(), next.getValue());
                    }
                    continue;
                }
                //该类有属性
                setValue(keyToColumn.get(next.getKey()), tClass, t, next.getValue());
            }

            if(flag && existProperty(keyToColumn.get(EXT_KEY),tClass)){
                setValue(keyToColumn.get(EXT_KEY), tClass, t, extInfo);
            }

            return t;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static <V> Map<String,Object> objectToMap(V obj){
        if(obj == null){
            return Collections.EMPTY_MAP;
        }
        Class<?> cls = obj.getClass();
        Map<String,Object> objectMap = new HashMap<>(4);
        try{
            Field[] declaredFields = cls.getDeclaredFields();
            if(declaredFields == null){
                return Collections.EMPTY_MAP;
            }
            for(Field field : declaredFields){
                field.setAccessible(true);
                objectMap.put(field.getName(),field.get(obj));
                field.setAccessible(false);
            }
        }catch (Exception e){
            return Collections.EMPTY_MAP;
        }
        return objectMap;
    }


    private static <T> void setValue(String name, Class<T> tClass, T t, Object value) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field declaredField = tClass.getDeclaredField(name);
        String methodName = "set" + name.substring(0,1).toUpperCase()+name.substring(1);
        Method method =  tClass.getDeclaredMethod(methodName, new Class[]{declaredField.getType()});
        Object classTypeValue = getClassTypeValue(declaredField.getType(), value);
        method.invoke(t, new Object[]{classTypeValue});
    }

    private static <T> boolean existProperty(String fieldName, Class<T> tClass){
        try{
            return tClass.getDeclaredField(fieldName) != null;
        }catch (Exception e){
            return false;
        }
    }


    private static Object getClassTypeValue(Class<?> typeClass, Object value){
        System.out.println(typeClass.getName() + "\t" + value);
        if(typeClass == int.class || typeClass == Integer.class  ){
            if(null == value || "".equals(value)){
                return 0;
            }else if(value instanceof Integer){
                return value;
            }else{
                return Integer.valueOf(value.toString());
            }

        }else if(typeClass == short.class || typeClass == Short.class){
            if(null == value){
                return 0;
            }else if(value instanceof Short){
                return value;
            }else{
                return Short.valueOf(value.toString());
            }
        }else if(typeClass == byte.class || typeClass == Byte.class){
            if(null == value){
                return 0;
            }else if(value instanceof Byte){
                return value;
            }else{
                return Byte.valueOf(value.toString());
            }
        }else if(typeClass == double.class || typeClass == Double.class){
            if(null == value){
                return 0;
            }else if(value instanceof Double){
                return value;
            }else{
                return Double.valueOf(value.toString());
            }
        }else if(typeClass == long.class || typeClass == Long.class){
            if(null == value){
                return 0;
            }else if(value instanceof Long){
                return value;
            }else{
                return Long.valueOf(value.toString());
            }
        }else if(typeClass == String.class){
            return value;
        }else if(typeClass == boolean.class || typeClass == Boolean.class){
            if(null == value){
                return true;
            }else if(value instanceof Boolean){
                return value;
            }else{
                return Boolean.valueOf(value.toString());
            }
        }else if(typeClass == BigDecimal.class){
            if(null == value){
                return new BigDecimal(0);
            }
            return new BigDecimal(value+"");
        }else if(typeClass == Date.class){
                return new Date();
        }
        else {
            return typeClass.cast(value);
        }
    }


}
