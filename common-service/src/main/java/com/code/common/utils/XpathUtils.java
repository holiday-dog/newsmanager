package com.code.common.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class XpathUtils {
    private static SAXReader reader = new SAXReader();

    public static List<String> evaluate(String original, String xpath) throws DocumentException {
        Document document = reader.read(new ByteArrayInputStream(original.getBytes()));
        List<Node> nodeList = document.selectNodes(xpath);

        if (CollectionUtils.isEmpty(nodeList)) {
            return null;
        }
        List<String> texts = new ArrayList<>();
        for (Node node : nodeList) {
            texts.add(node.getText());
        }
        return texts;
    }

    public static List<String> evaluateElementsText(String original, String xpath) throws DocumentException {
        Document document = reader.read(new ByteArrayInputStream(original.getBytes()));
        List<Node> nodeList = document.selectNodes(xpath);

        if (CollectionUtils.isEmpty(nodeList)) {
            return null;
        }
        List<String> texts = new ArrayList<>();
        for (Node node : nodeList) {
            texts.add(node.asXML());
        }
        return texts;
    }

    public static List<Node> evaluateElements(String original, String xpath) throws DocumentException {
        Document document = reader.read(new ByteArrayInputStream(original.getBytes()));
        List<Node> nodeList = document.selectNodes(xpath);

        return nodeList;
    }
}
