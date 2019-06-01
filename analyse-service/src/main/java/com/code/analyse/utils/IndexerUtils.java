package com.code.analyse.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class IndexerUtils {
    private static final String indexFile = "src/main/resources/lucene/index";

    public static IndexWriter initIndexWrite() throws Exception {
        IndexWriter writer = null;
        try {
            Directory dir = FSDirectory.open(Paths.get(indexFile));
            // 实例化分析器
            Analyzer analyzer = null;
            analyzer = new IKAnalyzer(true);
            // 实例化IndexWriterConfig
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            // 实例化IndexWriter
            writer = new IndexWriter(dir, config);
            writer.commit();
        } catch (Exception e) {
        }
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

    public static FieldType initField(IndexOptions indexOptions, boolean store, boolean tokenize, boolean storeTerm) {
        FieldType type = new FieldType();
        type.setIndexOptions(indexOptions);
        type.setStored(store);// 原始字符串全部被保存在索引中
        type.setStoreTermVectors(storeTerm);// 存储词项量
        type.setTokenized(tokenize);// 词条化
        return type;
    }

    public static void close(IndexWriter writer) throws Exception {
        if (writer == null) {
            return;
        }
        writer.deleteAll();
        writer.close();
    }

}
