/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.math.*;
import java.util.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.Gson;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import java.io.*;
import org.xml.sax.InputSource;

/**
 *
 * @author Пользователь
 */
public class Output {

    public BigDecimal SumResult;
    public int MulResult;
    public BigDecimal[] SortedInputs;

    // Конструктор класса от первоначального объекта
    Output(Input vx) {
        SumResult = BigDecimal.valueOf(0.0);
        for (int i = 0; i < vx.Sums.length; i++) {
            SumResult = SumResult.add(vx.Sums[i]);
        }
        SumResult = SumResult.multiply(BigDecimal.valueOf(Double.parseDouble(Integer.toString(vx.K))));
        MulResult = 1;

        for (int i = 0; i < vx.Muls.length; i++) {
            MulResult *= vx.Muls[i];
        }

        int k = vx.Sums.length;
        SortedInputs = new BigDecimal[(vx.Sums.length + vx.Muls.length)];

        for (int i = 0; i < vx.Sums.length; i++) {
            SortedInputs[i] = vx.Sums[i];
        }

        int h = 0;
        for (int j = k; j < (vx.Sums.length + vx.Muls.length); j++) {
            SortedInputs[j] = BigDecimal.valueOf(Double.parseDouble(Integer.toString(vx.Muls[h])));
            h++;
        }
        Arrays.sort(SortedInputs);
    }

    Output(String serialize, String raw) throws IOException, SAXException, ParserConfigurationException { // Конструктор для создания объекта из строки xml\json
        if (serialize.equalsIgnoreCase("xml")) { // Разбор xml
            try {
                //Подготовка к разбору xml
                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                f.setValidating(false);
                // Создается построитель документа
                DocumentBuilder builder = f.newDocumentBuilder();

                // Создается дерево DOM документа из нашей строки
                Document document = builder.parse(new InputSource(new StringReader(raw))); // ЗДЕСЬ ОШИБКА БЫЛА !!!! ПИЗДЕЦ КАКАЯ !  

                // Получаем корневой элемент
                Node root = document.getDocumentElement();

                NodeList nList = document.getElementsByTagName(document.getDocumentElement().getNodeName());

                // Просматриваем все подэлементы корневого - данные класса
                NodeList input = root.getChildNodes();

                List<BigDecimal> x1 = new ArrayList();

                for (int i = 0; i < input.getLength(); i++) { // Идем по всем нодам документа
                    Node nodeX = input.item(i);
                    System.out.println("\nCurrent Element :" + nodeX.getNodeName());

                    if (nodeX.getNodeName().equalsIgnoreCase("SumResult")) {    // Если это нода сумма
                        String xv = new String(nodeX.getTextContent());
                        this.SumResult = BigDecimal.valueOf(Double.parseDouble(nodeX.getTextContent()));
                    }

                    if (nodeX.getNodeName().equalsIgnoreCase("MulResult")) { //Это нода произведения 
                        this.MulResult = Integer.parseInt(nodeX.getTextContent());
                    }

                    if (nodeX.getNodeName().equalsIgnoreCase("SortedInputs")) { //Это соединенный сортированный массив 
                        for (int j = 0; j < nodeX.getChildNodes().getLength(); j++) { //Идем по массиву Muls
                            if (nodeX.getChildNodes().item(j).getNodeName().equalsIgnoreCase("decimal")) { //Если это число типа decimal
                                x1.add(BigDecimal.valueOf(Double.parseDouble(nodeX.getChildNodes().item(j).getTextContent()))); //Integer.parseInt();
                            }
                        }
                    }
                }
                SortedInputs = new BigDecimal[x1.size()];
                for (int i = 0; i < x1.size(); i++) {
                    SortedInputs[i] = x1.get(i);
                }
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка ParserConfigurationException разбора xml !");
            } catch (SAXException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка SAXException разбора xml !");
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка IOException разбора xml !");
            }
        }

        //Разбор json
        if (serialize.equalsIgnoreCase("json")) {
            JsonParser parser = new JsonParser();
            Gson parser1 = new Gson();
            JsonElement jsonElement = parser.parse(raw);
            JsonObject rootObject = jsonElement.getAsJsonObject();
            SumResult = BigDecimal.valueOf(rootObject.get("SumResult").getAsDouble());
            MulResult = rootObject.get("MulResult").getAsInt();

            List<Double> list_s = new ArrayList(parser1.fromJson(rootObject.get("SortedInputs").getAsString(), List.class)); // prints [1.0, 2.0, 3.0, 4.0], not integer values
            this.SortedInputs = new BigDecimal[list_s.size()];
            for (int i = 0; i < list_s.size(); i++) {
                SortedInputs[i] = BigDecimal.valueOf(list_s.get(i));
            }

            System.out.println("SumResult= " + SumResult + "  MulResult= " + MulResult + "  SortedInputs= " + SortedInputs.toString());
        }
    }

    String Respect(String serialize) throws TransformerConfigurationException, TransformerException {
        String respect = new String();
        if (serialize.equalsIgnoreCase("xml")) {
            DocumentBuilder builder;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                builder = factory.newDocumentBuilder();
                // создаем пустой объект Document, в котором будем 
                // создавать наш xml-файл
                Document doc = builder.newDocument();
                // создаем корневой элемент
                Element rootElement = doc.createElement("Output");
                // добавляем корневой элемент в объект Document
                doc.appendChild(rootElement);

                // добавляем первый дочерний элемент к корневому
                Element NameElementTitle = doc.createElement("SumResult");
                NameElementTitle.appendChild(doc.createTextNode(Double.toString(SumResult.doubleValue())));
                rootElement.appendChild(NameElementTitle);

                //добавляем второй дочерний элемент к корневому
                Element NameElementCompile = doc.createElement("MulResult");
                NameElementCompile.appendChild(doc.createTextNode(Integer.toString(MulResult)));
                rootElement.appendChild(NameElementCompile);

                //добавляем второй дочерний элемент к корневому
                Element NameElementRuns = doc.createElement("SortedInputs");
                for (int i = 0; i < SortedInputs.length; i++) {
                    Element node = doc.createElement("decimal");
                    node.appendChild(doc.createTextNode(Double.toString(SortedInputs[i].doubleValue())));
                    NameElementRuns.appendChild(node);
                }
                rootElement.appendChild(NameElementRuns);

                //создаем объект TransformerFactory для печати в консоль 
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();

                // для красивого вывода в консоль
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);

                StringWriter sw = new StringWriter();
                StreamResult sr = new StreamResult(sw);
                transformer.transform(source, sr);
                System.out.println(sw.toString());

                respect = new String(sw.toString());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (serialize.equalsIgnoreCase("json")) {

            JsonObject rootObject = new JsonObject();
            rootObject.addProperty("SumResult", SumResult);

            rootObject.addProperty("MulResult", MulResult);

            Gson gson = new Gson();
            String json = gson.toJson(SortedInputs);
            rootObject.addProperty("SortedInputs", json);

            respect = new String(gson.toJson(rootObject));

            System.out.println(respect);
        }

        return respect;
    }
}
