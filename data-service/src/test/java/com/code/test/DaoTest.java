package com.code.test;

import com.code.data.DataApplication;
import com.code.data.dao.NewsContentInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataApplication.class})
public class DaoTest {
    @Autowired
    private NewsContentInfoMapper newsContentInfoMapper;


    @Test
    public void test() {
        System.out.println(newsContentInfoMapper == null);
    }
}
