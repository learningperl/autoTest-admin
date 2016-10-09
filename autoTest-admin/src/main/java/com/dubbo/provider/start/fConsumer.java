package com.dubbo.provider.start;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dubbo.provider.article_Center;
import com.dubbo.provider.memberCenter;

public class fConsumer {
    
    public static void main(String[] args) {
        ClassPathXmlApplicationContext  context=new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
         
        
        Map<String,String> mapret = new HashMap<String,String>();
        //获取bean
        /*
        memberCenter demoService=(memberCenter)context.getBean("memberCenter");
        
        mapret.put("nickname", "tester");
        mapret.put("loginName", "tester1");
        mapret.put("password", "tester");
         
        System.out.println(demoService.register(mapret));//显示调用结果
        
        System.out.println(demoService.login("tester1", "tester"));
        
        mapret.clear();
        mapret.put("password", "tester");
        mapret.put("loginName", "tester1");
        mapret.put("newpwd", "tester2' #");
        System.out.println(demoService.changePwd(mapret));
        keyWordCenter demoService1=(keyWordCenter)context.getBean("keyWordCenter");
        System.out.println(demoService1.serviceKey().toString());
        System.out.println(demoService1.serviceKey("get").toString());
        System.out.println(demoService1.serviceKey("中").toString());
        System.out.println(demoService1.delById("24").toString());
        System.out.println(demoService1.delById("204").toString());
        System.out.println(demoService1.delById("aa").toString());
        System.out.println(demoService1.getById("20").toString());
        System.out.println(demoService1.delById("204").toString());
        System.out.println(demoService1.delById("2 ' or 1=1 #").toString());
        mapret.clear();
        mapret.put("id", "25");
        mapret.put("type", "1");
        mapret.put("keyName", "test");
        mapret.put("describes", "2");
        mapret.put("useCase", "2");
        System.out.println(demoService1.updateKeyWords(mapret).toString());
        mapret.clear();
        mapret.put("id", "0");
        mapret.put("type", "1");
        mapret.put("keyName", "test");
        mapret.put("describes", "2");
        mapret.put("useCase", "2");
        System.out.println(demoService1.updateKeyWords(mapret).toString());
        mapret.clear();
        mapret.put("id", "25");
        mapret.put("type", "1");
        mapret.put("keyName", "test");
        mapret.put("describes", "2");
        mapret.put("useCase", "aaa ' #");
        System.out.println(demoService1.updateKeyWords(mapret).toString());
        */
        
        //mapret.clear();
        article_Center demoService1=(article_Center)context.getBean("article_Center");
        System.out.println(demoService1.getCommentsById("2", 0));
        
        
        //Map<String, String> l_all = new HashMap<String, String>();
        
        //System.out.println(demoService1.getEmojis());
        /*
        System.out.println(demoService1.setAgree("2","9").toString());
        System.out.println(demoService1.setAgree("3","2 ' or 1=1 #").toString());
        
        l_all = demoService1.getArticleById("2");
		System.out.println(l_all.toString());
		
        System.out.println(demoService1.SearchArticle(0).toString());
        System.out.println(demoService1.SearchArticle("1",0).toString());
        System.out.println(demoService1.SearchArticle("1",1).toString());
        
        System.out.println(demoService1.delArticleById("1").toString());
        System.out.println(demoService1.delArticleById("204").toString());
        System.out.println(demoService1.delArticleById("aa").toString());
        
        System.out.println(demoService1.getArticleById("4").toString());
        System.out.println(demoService1.getArticleById("204").toString());
        System.out.println(demoService1.getArticleById("2 ' or 1=1 #").toString());

        mapret.clear();
        mapret.put("id", "4");
        mapret.put("userid", "2");
        mapret.put("author", "test");
        mapret.put("title", "5");
        mapret.put("agree", "0");
        mapret.put("content", "5");
        System.out.println(demoService1.updateArticle(mapret).toString());
        
        mapret.clear();
        mapret.put("id", "0");
        mapret.put("userid", "2");
        mapret.put("author", "test");
        mapret.put("title", "55");
        mapret.put("agree", "0");
        mapret.put("content", "55");
        System.out.println(demoService1.updateArticle(mapret).toString());
        
        mapret.clear();
        mapret.put("id", "4");
        mapret.put("userid", "2");
        mapret.put("articleid", "5");
        mapret.put("content", "222222223");
        System.out.println(demoService1.updateComment(mapret).toString());
        
        mapret.clear();
        mapret.put("id", "4");
        mapret.put("userid", "2");
        mapret.put("articleid", "111");
        mapret.put("content", "22222222");
        System.out.println(demoService1.updateComment(mapret).toString());
                */
      
    }
}
