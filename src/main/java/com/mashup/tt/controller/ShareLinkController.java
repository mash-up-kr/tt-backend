package com.mashup.tt.controller;

import com.mashup.tt.dto.everytime.Subject;
import com.mashup.tt.dto.everytime.Time;
import com.mashup.tt.dto.everytime.TimeTable;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

@RestController
public class ShareLinkController {

    private static final String EVERY_TIME_TIMETABLE_API = "https://everytime.kr/find/timetable/table/friend";

    @PostMapping("/shareLink")
    public TimeTable addShareLink(@RequestParam("share_link") String shareLink) throws IOException {
        URL url = new URL(shareLink);
        String identifierKey = url.getPath().replace("/@", "");
        TimeTable timeTable = readEveryTimeTimeTable(identifierKey);
        //TODO : 파싱된 시간표 정보를 db에 추가해야함.
        return timeTable;
    }

    private TimeTable readEveryTimeTimeTable(String identifier) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(EVERY_TIME_TIMETABLE_API);

        ArrayList<BasicNameValuePair> entityList = new ArrayList<>();
        entityList.add(new BasicNameValuePair("identifier", identifier));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(entityList, Consts.UTF_8);
        postRequest.setEntity(entity);

        HttpResponse response = client.execute(postRequest);
        InputStream inputStream = response.getEntity().getContent();
        try {
            return parseXml(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("xml 파싱 실패");
        }
    }

    private TimeTable parseXml(InputStream inputStream) throws Exception {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(inputStream);

        Element tableNode = (Element) xPath.evaluate("response/table", document, XPathConstants.NODE);
        NodeList tableNodeList = (NodeList) xPath.evaluate("//response/table/subject", document, XPathConstants.NODESET);

        ArrayList<Subject> subjectList = new ArrayList<>();
        for (int i = 0; i < tableNodeList.getLength(); i++) {
            Element subjectElement = (Element) tableNodeList.item(i);
            subjectList.add(parseSubject(subjectElement));
        }
        return new TimeTable(
                Integer.parseInt(tableNode.getAttribute("year")),
                Integer.parseInt(tableNode.getAttribute("semester")),
                subjectList
        );
    }

    private Subject parseSubject(Element subjectElement) {
        int subjectId = Integer.parseInt(subjectElement.getAttribute("id"));

        Element nameElement = getElementByTag(subjectElement, "name");
        Element professorElement = getElementByTag(subjectElement, "professor");
        Element timeElement = getElementByTag(subjectElement, "time");
        Element timeDataElement = getElementByTag(timeElement, "data");

        return new Subject(
                subjectId,
                nameElement.getAttribute("value"),
                professorElement.getAttribute("value"),
                new Time(
                        getAttribute(timeElement, "value"),
                        getIntAttribute(timeDataElement, "day"),
                        getIntAttribute(timeDataElement, "starttime"),
                        getIntAttribute(timeDataElement, "endtime"),
                        getAttribute(timeDataElement, "place")
                )
        );
    }

    private Element getElementByTag(Element element, String tag) {
        return (Element) element.getElementsByTagName(tag).item(0);
    }

    private String getAttribute(Element element, String tag) {
        return element.getAttribute(tag);
    }

    private Integer getIntAttribute(Element element, String tag) {
        return Integer.parseInt(element.getAttribute(tag));
    }
}
