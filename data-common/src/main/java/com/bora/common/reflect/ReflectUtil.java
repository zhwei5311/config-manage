package com.bora.common.reflect;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

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
        if(typeClass == int.class  || value instanceof Integer){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == short.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == byte.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == double.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == long.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == String.class){
            if(null == value){
                return "";
            }
            return value;
        }else if(typeClass == boolean.class){
            if(null == value){
                return true;
            }
            return value;
        }else if(typeClass == BigDecimal.class){
            if(null == value){
                return new BigDecimal(0);
            }
            return new BigDecimal(value+"");
        }else {
            return typeClass.cast(value);
        }
    }
}
