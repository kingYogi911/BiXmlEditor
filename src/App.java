import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class App {
    public static Document destDocument;
    public static void main(String[] args) throws Exception {
        System.out.println("\t\t\t\t\t!!!BiXmlParser!!!!");
        File sourceFile =new File("./xml/test.xml");
        File destinationFile =new File("./xml/testDest.xml");
        File testResult =new File("./xml/testResult.xml");
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document sourceDocument = builder.parse(sourceFile);
        destDocument =builder.parse(destinationFile);
        sourceDocument.getDocumentElement().normalize();
        
        Node nodes=sourceDocument.getDocumentElement();
        //System.out.println(sourceDocument.getChildNodes().item(0).getChildNodes());
        traverseNodes(nodes);
        Element data;
        NodeList nodelist=sourceDocument.getDocumentElement().getChildNodes();
        for(int i=0;i<nodelist.getLength();i++){
            if(nodelist.item(i).getNodeType()==Node.ELEMENT_NODE){
                if(((Element)nodelist.item(i)).getTagName().equals("data")){
                    data=(Element)nodelist.item(i);
                   // destDocument.appendChild(data);
                }
            }
        }
        
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        //transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        transformer.setOutputProperty(OutputKeys.METHOD,"xml");
        DOMSource source=new DOMSource( destDocument);
        StreamResult result =new StreamResult(testResult);
        transformer.transform(source,result);
        System.out.println("Success");
    }

    public static void traverseNodes(Node node){
        //System.out.println(node);
        String tagName=((Element)node).getTagName();
        System.out.println(tagName);
        NamedNodeMap x=node.getAttributes();
        boolean reqChange=false;
        if(x!=null){
        for(int a=0;a<x.getLength();a++){
            String name = x.item(a).getNodeName();
            String value =x.item(a).getNodeValue();
            
            if(value.startsWith("@=")||value.startsWith("@{")){
                System.out.println("\t"+name+"=\""+value+"\"");
                Node indicatorAttr=x.getNamedItem("android:id");
                System.out.println("indicator attribute->"+indicatorAttr);
                //destDocument.getElementById("tv1").setAttribute(name, value);
                Boolean result=putinDestination(destDocument.getDocumentElement(),name, value, tagName, indicatorAttr);
                //System.out.println("Result->"+result);
                //System.out.println("count->"+count);
            }
        }

    }
        if(node.hasChildNodes()){
            NodeList children=node.getChildNodes();
            for(int i=0;i<children.getLength();i++){
                if(children.item(i).getNodeType()==Node.ELEMENT_NODE)
                traverseNodes(children.item(i));
            }
        }
    }
    public static int count=0;
    public static boolean putinDestination(Element e,String name,String value,String tagName,Node indicatorAttr){
        count++;
        if(e.getTagName()==tagName){
            Node attr=e.getAttributes().getNamedItem(indicatorAttr.getNodeName());
            //System.out.println("FoundAttr->"+attr);
            if(attr!=null){
                if(attr.getNodeValue().equals(indicatorAttr.getNodeValue())){
                    //Current element is required element
                    System.out.println("Added to Destination");
                    e.setAttribute(name, value);
                    return true;
                }
            }
        }
        if(e.hasChildNodes()){
            NodeList children=e.getChildNodes();
            for(int i=0;i<children.getLength();i++){
                if(children.item(i).getNodeType()==Node.ELEMENT_NODE){
                    Boolean result=putinDestination((Element)children.item(i), name, value, tagName, indicatorAttr);
                    if(result==true)
                    return true;
                }
            }
        }
        return false;
    }
 
}
