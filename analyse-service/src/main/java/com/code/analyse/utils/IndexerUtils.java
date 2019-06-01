package com.code.analyse.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class IndexerUtils {
    private static final String indexFile = "src/main/resources/lucene/index";
    private static IndexWriter writer = null;

    public static IndexWriter initIndexWrite() throws Exception {
//        try {
//            Directory dir = FSDirectory.open(Paths.get(getURI()));
        Directory dir = FSDirectory.open(Paths.get(indexFile));
        // 实例化分析器
        Analyzer analyzer = new SmartChineseAnalyzer();
        // 实例化IndexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        // 实例化IndexWriter
        writer = new IndexWriter(dir, config);
        writer.commit();
//        } catch (Exception e) {
//        }
        return writer;
    }

    public static IndexReader getIndexReader() throws Exception {
        //得到读取索引文件的路径
        Directory dir = FSDirectory.open(Paths.get(indexFile));
        //通过dir得到的路径下的所有的文件
        IndexReader reader = DirectoryReader.open(dir);

        return reader;
    }

    public static IndexSearcher getIndexSearch() throws Exception {
        //建立索引查询器
        IndexSearcher is = new IndexSearcher(getIndexReader());

        return is;
    }

    public void close() throws Exception {
        writer.close();
    }

    private static URI getURI() throws URISyntaxException {
        URL url = IndexerUtils.class.getClassLoader().getResource(indexFile);
        return url.toURI();
    }

}
