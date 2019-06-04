package com.code.analyse.handler;

import com.code.analyse.utils.IndexerUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;

import java.util.*;
import java.util.stream.Collectors;

public class KeyWordExtractor {
    private static final Integer keyWordCount = 5;
    private static final String regex = "[\\w\\-\\.\\,\\!\\?\\:\\{\\}]+";

    public List analyse(String content) throws Exception {
        IndexWriter indexWriter = IndexerUtils.initIndexWrite();
        Document document = new Document();
        document.add(new Field("content", content, IndexerUtils.initField(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, false, true, true)));
        indexWriter.addDocument(document);
        indexWriter.commit();

        IndexReader indexReader = IndexerUtils.getIndexReader();
        Terms terms = indexReader.getTermVector(indexReader.maxDoc() - 1, "content");
        TermsEnum termsEnum = terms.iterator();
        Map<String, Integer> map = new HashMap<String, Integer>();
        BytesRef br = null;
        while ((br = termsEnum.next()) != null) {
            String termText = br.utf8ToString();
            // 通过totalTermFreq()方法获取词项频率
            map.put(termText, (int) termsEnum.totalTermFreq());
        }

        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        List<Map.Entry> result = sortedMap.stream().sorted(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        }).filter((entry) -> {
            return entry.getKey().length() > 1;
        }).filter((entry) -> {
            return !entry.getKey().matches(regex);
        }).limit(keyWordCount).collect(Collectors.toList());

        IndexerUtils.close(indexWriter);
        return result;
    }
}
