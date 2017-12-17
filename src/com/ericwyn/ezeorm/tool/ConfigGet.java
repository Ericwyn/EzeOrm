package com.ericwyn.ezeorm.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * ConfigGet工具，用以获取本地config文件
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-07-15
 */
public class ConfigGet {
    private static SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    //配置文件
    private File configFile;
    //配置的configMap
    private HashMap<String ,String > configMap;
    //配置文件是否存在
    private boolean haveConfig=false;
    //是否依据默认值自动生成配置文件
    private boolean createConfigFile=false;
    //配置文件最后 一行是否为空行

    public ConfigGet(File configFile){
        this.configFile=configFile;
        readAllConfig();
    }

    /**
     * 是否新建一个配置文件
     * @param configFile
     * @param createConfigFile  是否按照读取到的默认内容，新建一个固定配置文件
     */
    public ConfigGet(File configFile,boolean createConfigFile){
        this(configFile);
        this.createConfigFile=createConfigFile;
    }

    public ConfigGet(String configFilePath){
        this(new File(configFilePath));
    }

    /**
     * 是否新建一个配置文件
     * @param configFilePath
     * @param createConfigFile  是否按照读取到的默认内容，新建一个固定的配置文件
     */
    public ConfigGet(String configFilePath,boolean createConfigFile){
        this(new File(configFilePath));
        this.createConfigFile=createConfigFile;
    }

    private void readAllConfig(){
        this.configMap=new HashMap<>();
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(configFile));
            String line=null;
            while ((line=bufferedReader.readLine())!=null){

                line=line.trim().replaceAll(" ","");
                if(!line.startsWith(";")){
                    if(line.contains(";")){
                        String[] temps=line.split(";");
                        if(temps[0]!=null && !temps[0].equals("")){
                            line=temps[0];
                        }
                    }
                    if(!line.trim().equals("")){
                        String[] temps=line.split("=");
                        if(temps[0]!=null && temps[1]!=null
                                && !temps[0].equals("") && !temps[1].equals("")){
                            configMap.put(temps[0],temps[1]);
                        }
                    }
                }
            }
            haveConfig=true;
            bufferedReader.close();
        }catch (FileNotFoundException e){
            haveConfig=false;
        }catch (IOException e){
            errorFileOupFile(e);
        }
    }

    /**
     * 获取配置文件里面配置的值
     * @param defaultValue  默认返回的值（在没有配置文件或者配置文件当中没有这个值的情况下）
     * @param valueKey  配置项的名称
     * @return  返回配置的值，以Stirng的形式
     */
    public String getValue(String defaultValue,String valueKey){
        if(haveConfig && configMap.get(valueKey)!=null){
            return configMap.get(valueKey);
        }else {
            if(createConfigFile){
                writeVale(valueKey,defaultValue);
            }
            return defaultValue;
        }
    }

    /**
     * 建一个配置项目写入到配置文件当中
     * @param key   项目的key
     * @param value 项目的值
     */
    private void writeVale(String key,String value){
        if(!configFile.isFile()){
            try {
                configFile.createNewFile();
            }catch (IOException e){
                errorFileOupFile(e);
            }
        }
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(configFile,true));
            bufferedWriter.newLine();
            bufferedWriter.write(key+" = "+value +" ; auto create by ConfigGet");
            bufferedWriter.newLine();
            bufferedWriter.close();
            readAllConfig();
        }catch (IOException e){
            errorFileOupFile(e);
        }

    }

    /**
     * 配置文件无法读取的时候将错误信息输出成错误文件
     */
    private void errorFileOupFile(Exception e){
        File errorFile=new File("ConfigGet_error.log");
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(errorFile,true));
            bufferedWriter.write(sdf.format(new Date())+":"+e.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
