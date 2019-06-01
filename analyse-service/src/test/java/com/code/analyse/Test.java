package com.code.analyse;

import com.alibaba.fastjson.JSON;
import com.code.analyse.utils.IndexerUtils;
import com.code.common.utils.IOUtils;
import com.code.common.utils.JsonPathUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class Test {
    private String page = null;

    @org.junit.Test
    public void test111() throws Exception {
//        System.out.println(page);
        page = JsonPathUtils.getValue(page, "$.historyTravelList[0].content");
        System.out.println(page);

        IndexWriter indexWriter = IndexerUtils.initIndexWrite();
        System.out.println(indexWriter == null);

        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(false);// 原始字符串全部被保存在索引中
        type.setStoreTermVectors(true);// 存储词项量
        type.setTokenized(true);// 词条化
        Document document = new Document();
        document.add(new Field("content", page, type));
        indexWriter.addDocument(document);
        indexWriter.close();

        IndexReader indexReader = IndexerUtils.getIndexReader();
        Terms terms = indexReader.getTermVector(0, "content");
        TermsEnum termsEnum = terms.iterator();
        Map<String, Integer> map = new HashMap<String, Integer>();
        BytesRef br = null;
        while ((br = termsEnum.next()) != null) {
            String termText = br.utf8ToString();
            // 通过totalTermFreq()方法获取词项频率
            map.put(termText, (int) termsEnum.totalTermFreq());
        }

        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(sortedMap, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        System.out.println(JSON.toJSONString(sortedMap));
    }

    @Before
    public void before() throws IOException {
        page = IOUtils.stringByResource("test/page.json", Charset.defaultCharset());

    }
}
