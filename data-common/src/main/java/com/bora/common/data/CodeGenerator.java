package com.bora.common.data;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-05 14:09:06
 *****/
public class CodeGenerator {

    private final JSONObject param;

    public CodeGenerator(JSONObject param) {
        this.param = param;
    }

    private static final String DIR = System.getProperty("user.dir");

    public synchronized void generateCode(String rootDir,String[] tableName,
                                          String[] tablePrefix,String authorName,
                                          boolean override,boolean onlyDal){
        for(int i = 0; i < Type.values().length; i++){
            Type type = Type.getByCode(i);
            if(onlyDal){
                continue;
            }
            AutoGenerator mpg = new AutoGenerator();
            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            int lastIndexOf = DIR.lastIndexOf("\\");
            String rootContent = DIR.substring(0,lastIndexOf);
            gc.setOutputDir(rootContent + type.getDesc() + "/src/main/java");
            gc.setFileOverride(override);
            // 不需要ActiveRecord特性的请改为false
            gc.setActiveRecord(false);
            // XML 二级缓存
            gc.setEnableCache(false);
            // XML ResultMap
            gc.setBaseResultMap(true);
            // XML columList
            gc.setBaseColumnList(true);
            //设置时间类型
            gc.setDateType(DateType.ONLY_DATE);
            gc.setAuthor(authorName);
            gc.setSwagger2(true);
            gc.setEntityName("%sDo");
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDbType(DbType.MYSQL);
            dsc.setTypeConvert(new MySqlTypeConvert(){
                // 自定义数据库表字段类型转换【可选】
                @Override
                public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                    System.out.println("转换类型：" + fieldType);
                    // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                    return super.processTypeConvert(gc,fieldType);
                }
            });
            dsc.setDriverName(param.getString("driverClassName"));
            dsc.setUsername(param.getString("username"));
            dsc.setPassword(param.getString("password"));
            dsc.setUrl(param.getString("url"));
            mpg.setDataSource(dsc);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            // 表名生成策略
            strategy.setNaming(NamingStrategy.underline_to_camel);
            //前缀
            strategy.setTablePrefix(tablePrefix);
            // 需要生成的表
            strategy.setInclude(tableName);
            strategy.setEntityColumnConstant(true);
            strategy.setRestControllerStyle(true);
            strategy.setEntityLombokModel(true);
            strategy.setEntityColumnConstant(false);
            strategy.setLogicDeleteFieldName("is_delete");


            mpg.setStrategy(strategy);
            // 包配置
            PackageConfig pc = new PackageConfig();
            pc.setParent(rootDir + type.getValue());
            pc.setEntity("domain");
            pc.setMapper("dao");
            pc.setService("service");
            pc.setServiceImpl("impl");
            pc.setController("");
            mpg.setPackageInfo(pc);
            // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】  ${cfg.abc}
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                    this.setMap(new HashMap<>(8));
                }
            };
            // 自定义模板配置
            String templatePath = "/templates/mapper.xml.vm";
            if(i == 0) {
                List<FileOutConfig> focList = new ArrayList<>();
                focList.add(new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        //自定义输出文件的位置
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(rootContent)
                                .append("/")
                                .append(Type.DAL.getDesc())
                                .append("/src/main/resources/mapper/")
                                .append(tableInfo.getEntityName())
                                .append("Mapper")
                                .append(StringPool.DOT_XML);
                        return stringBuffer.toString();
                    }
                });
                cfg.setFileOutConfigList(focList);
            }
            // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
            TemplateConfig tc = new TemplateConfig();
            tc.setXml(null);
            if(i == 0){
                tc.setEntity("/template/entity.java.vm");
                tc.setMapper("/templates/mapper.java.vm");
                tc.setController(null);
                tc.setService(null);
                tc.setServiceImpl(null);
            }else if(i == 1){
                tc.setEntity(null);
                tc.setMapper(null);
                tc.setController(null);
                tc.setService("/template/service.java.vm");
                tc.setServiceImpl("/templates/serviceImpl.java.vm");
            }else {
                /*tc.setEntity(null);
                tc.setMapper(null);
                tc.setController("/template/common.java.vm");
                tc.setService(null);
                tc.setServiceImpl(null);*/
            }

            // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
            mpg.setCfg(cfg);
            mpg.setTemplate(tc);
            // 执行生成
            mpg.execute();

            System.out.println("生成完毕");
        }
    }


    /**
     * 生成类型
     */
    private enum Type{
        //dal
        DAL(0,".dal","/config-dal"),
        //service
        SERVICE(1,".service","/config-service"),
        //web
        WEB(2,".controller","/config-controller");

        private Integer code;
        private String value;
        private String desc;

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        Type(Integer code,String value, String desc){
            this.code = code;
            this.value = value;
            this.desc = desc;
        }
        static Type getByCode(Integer code){
            for(Type type : Type.values()){
                if(type.getCode().equals(code)){
                    return type;
                }
            }
            return null;
        }

    }
}
