package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.expection.EzeExpection;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class EzeDBBuilder{

    private String url =null;
    private Class tableClass =null;

    public EzeDBBuilder(){

    }

    public EzeDBBuilder url(String url){
        this.url=url;
        return this;
    }

    public EzeDBBuilder setClass(Class tableClass){
        this.tableClass=tableClass;
        return this;
    }

    public EzeDB build() throws EzeExpection{
        if(url==null || url.equals("")){
            throw new EzeExpection("builder 的数据库连接url 为空");
        }
        if(tableClass ==null){
            throw new EzeExpection("builder 的对象映射类为空，请使用 setClass() 方法设置对象映射类");
        }else{
            return new EzeDB(this.url,tableClass);
        }
    }


    /**
     * orm 类
     */
    public class EzeDB {
        private String url;
        private Class tableClass;
//        public static
        private EzeDB(String url,Class tableClass){
            this.url=url;
            this.tableClass=tableClass;




        }

        @Override
        public String toString() {
            return url+tableClass.getName();
        }
    }
}
