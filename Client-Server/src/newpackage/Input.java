/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.math.*;
import java.util.*;
import javax.swing.JOptionPane;
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


public class Input {

    public int K;
    public BigDecimal Sums[];
    public int Muls[];

    // Конструктор для рандомной генерации значений
    Input() { 
        Random random = new Random();
        
        K = random.nextInt(11) + 1;
        
        int n1 = random.nextInt(3) + 2;
        Sums = new BigDecimal[n1];
        
        int n2 = random.nextInt(3) + 2;
        Muls = new int[n2];
        
        for (int i = 0; i < n1; i++) {
            Sums[i] = BigDecimal.valueOf(random.nextInt(10));
        }
        
        for (int i = 0; i < n2; i++) {
            Muls[i] = random.nextInt(10);
        }
    }

    // Конструктор для создания объекта из переданных параметров
    Input(int k1, BigDecimal S1[], int[] M1) {
        K = k1;
        
        if (S1.length > 0) {
            Sums = new BigDecimal[S1.length];
            
            for (int i = 0; i < S1.length; i++) {
                Sums[i] = S1[i];
            }
        } else {
            JOptionPane.showMessageDialog(null, "Размер массива сумм нулевой !");
        }

        if (M1.length > 0) {
            Muls = new int[M1.length];
            
            for (int i = 0; i < M1.length; i++) {
                Muls[i] = M1[i];
            }
        } else {
            JOptionPane.showMessageDialog(null, "Размер массива произведений нулевой !");
        }
    }

    // Конструктор для создания объекта из строки (xml\json)
    Input(String serializationType, String raw) throws IOException, SAXException, ParserConfigurationException {
        // Разбор xml
        if (serializationType.equalsIgnoreCase("xml")) {
            try {
                //Подготовка к разбору xml
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                // Создается построитель документа
                DocumentBuilder builder = factory.newDocumentBuilder();

                // Создается дерево DOM документа из нашей строки
                Document document = builder.parse(new InputSource(new StringReader(raw)));

                // Получаем корневой элемент
                Node root = document.getDocumentElement();

                //NodeList nList = document.getElementsByTagName("K"); // Переходим к ноде с коэфф.
                NodeList nList = document.getElementsByTagName(document.getDocumentElement().getNodeName());

                // Просматриваем все подэлементы корневого - данные класса
                NodeList input = root.getChildNodes();

                List<BigDecimal> x1 = new ArrayList();
                List<String> x2 = new ArrayList();

                for (int i = 0; i < input.getLength(); i++) { // Идем по всем нодам документа
                    Node nodeX = input.item(i);
                    System.out.println("\nCurrent Element :" + nodeX.getNodeName());
                    //if(!nodeX.hasChildNodes()){                           //Если не имеет дочерние ноды
                    if (nodeX.getNodeName().equalsIgnoreCase("K")) {    // Если это нода К-коэффициент
                        this.K = Integer.parseInt(nodeX.getTextContent());     //getNodeValue()
                    }
                    // }
                    // if (nodeX.hasChildNodes()) { //Если имеет дочерние ноды
                    if (nodeX.getNodeName().equalsIgnoreCase("Sums")) { //Это массив сумм
                        for (int j = 0; j < nodeX.getChildNodes().getLength(); j++) { //Идем по массиву Summ
                            if (nodeX.getChildNodes().item(j).getNodeName().equalsIgnoreCase("decimal")) { //Если это число типа decimal  
                                // JOptionPane.showMessageDialog(null,"Значение Sums["+j+"] в xml= "+nodeX.getChildNodes().item(j).getTextContent());
                                x1.add(BigDecimal.valueOf(Double.parseDouble(nodeX.getChildNodes().item(j).getTextContent())));
                            }
                        }
                    }
                    //   }
                    //  if (nodeX.hasChildNodes()) { //Если имеет дочерние ноды
                    if (nodeX.getNodeName().equalsIgnoreCase("Muls")) { //Это массив произведений
                        for (int j = 0; j < nodeX.getChildNodes().getLength(); j++) { //Идем по массиву Muls
                            if (nodeX.getChildNodes().item(j).getNodeName().equalsIgnoreCase("int")) { //Если это число типа int
                                x2.add(nodeX.getChildNodes().item(j).getTextContent()); //Integer.parseInt()
                            }
                        }
                    }
                }
                this.Sums = new BigDecimal[x1.size()]; //
                for (int i = 0; i < x1.size(); i++) {
                    Sums[i] = x1.get(i);
                }
                this.Muls = new int[x2.size()];
                for (int i = 0; i < x2.size(); i++) {
                    Muls[i] = Integer.parseInt(x2.get(i));
                }
                //   }
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка ParserConfigurationException разбора xml !");
                //   JOptionPane.showMessageDialog(null, "Ошибка ParserConfigurationException разбора xml !");
            } catch (SAXException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка SAXException разбора xml !");
                //   JOptionPane.showMessageDialog(null, "Ошибка SAXException разбора xml !");
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Ошибка IOException разбора xml !");
                //   JOptionPane.showMessageDialog(null, "Ошибка IOException разбора xml !");
            }
        }
        
        //Разбор json
        if (serializationType.equalsIgnoreCase("json")) { 
            JsonParser parser = new JsonParser();
            Gson parser1 = new Gson();
            Gson parser2 = new Gson();
            JsonElement jsonElement = parser.parse(raw); //"{\"message\":\"Hi\",\"place\":{\"name\":\"World!\"}}"

            JsonObject rootObject = jsonElement.getAsJsonObject();
            K = rootObject.get("K").getAsInt();

            // double[] cc = parser1.fromJson(rootObject.get("Sums").getAsString(), double[].class);
            List<Double> list_s = new ArrayList(parser1.fromJson(rootObject.get("Sums").getAsString(), List.class)); // prints [1.0, 2.0, 3.0, 4.0], not integer values
            this.Sums = new BigDecimal[list_s.size()];
            for (int i = 0; i < list_s.size(); i++) {
                Sums[i] = BigDecimal.valueOf(list_s.get(i));
            }

            double[] mul = parser2.fromJson(rootObject.get("Muls").getAsString(), double[].class);
            //  List<Integer> list_m=new ArrayList(parser2.fromJson(rootObject.get("Muls").getAsString(), List.class )); // prints [1.0, 2.0, 3.0, 4.0], not integer values
            this.Muls = new int[mul.length]; //list_m.size()
            for (int j = 0; j < mul.length; j++) { //list_m.size()
                double y = mul[j];
                Muls[j] = (int) y;
            }
        }
    }

    // Возвращает строку (json/xml) созданую из полей класса
    String Request(String serialize) throws TransformerConfigurationException, TransformerException {
        String request = new String();
        if (serialize.equalsIgnoreCase("xml")) {
            DocumentBuilder builder;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);

            try {
                builder = factory.newDocumentBuilder();
                // создаем пустой объект Document, в котором будем 
                // создавать наш xml-файл
                Document doc = builder.newDocument();

                doc.setXmlStandalone(false);
                doc.normalizeDocument();
                doc.normalize();

                // создаем корневой элемент
                Element rootElement = doc.createElement("Input");
                // добавляем корневой элемент в объект Document
                doc.appendChild(rootElement);

                // добавляем первый дочерний элемент к корневому
                Element NameElementTitle = doc.createElement("K");
                NameElementTitle.appendChild(doc.createTextNode(Integer.toString(K)));
                rootElement.appendChild(NameElementTitle);

                //добавляем второй дочерний элемент к корневому
                Element NameElementCompile = doc.createElement("Sums");
                for (int i = 0; i < Sums.length; i++) {
                    Element node = doc.createElement("decimal");
                    node.appendChild(doc.createTextNode(Sums[i].toString()));
                    NameElementCompile.appendChild(node);
                }
                rootElement.appendChild(NameElementCompile);

                //добавляем второй дочерний элемент к корневому
                Element NameElementRuns = doc.createElement("Muls");
                for (int i = 0; i < Muls.length; i++) {
                    Element node = doc.createElement("int");
                    node.appendChild(doc.createTextNode(Integer.toString(Muls[i])));
                    NameElementRuns.appendChild(node);
                }
                rootElement.appendChild(NameElementRuns);

                doc.normalizeDocument();
                doc.normalize();

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

                request = new String(sw.toString());

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (serialize.equalsIgnoreCase("json")) {

            JsonObject rootObject = new JsonObject();
            rootObject.addProperty("K", Integer.toString(K));

            Gson gson = new Gson();
            String json = gson.toJson(Sums);
            rootObject.addProperty("Sums", json);

            Gson gsons = new Gson();
            String jsons = gsons.toJson(Muls);
            rootObject.addProperty("Muls", jsons);

            request = new String(gson.toJson(rootObject));

            System.out.println(request);
        }
        return request;
    }
    
}
