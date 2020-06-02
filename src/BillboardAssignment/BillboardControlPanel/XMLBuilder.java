package BillboardAssignment.BillboardControlPanel;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


public class XMLBuilder {
    public static String WriteXML (String title, String titleR, String titleG, String titleB,
                              String imagePath, boolean URL,
                              String subtext, String subtextR, String subtextG, String subtextB,
                              String backgroundR, String backgroundG, String backgroundB) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.newDocument();

        Element billboardElement = document.createElement("billboard");
        document.appendChild(billboardElement);

        if (title.length() > 0) {

            Element titleElement;
            titleElement = document.createElement("message");
            billboardElement.appendChild(titleElement);
            titleElement.appendChild(document.createTextNode(title));

            if ((titleR.length() > 0) && (titleG.length() > 0) && (titleB.length() > 0)) {
                // Convert title colours to ints
                int titleRInt = Integer.parseInt(titleR);
                int titleGInt = Integer.parseInt(titleG);
                int titleBInt = Integer.parseInt(titleB);

                // Convert ints to hex strings
                String titleRHex = String.format("%02X", titleRInt);
                String titleGHex = String.format("%02X", titleGInt);
                String titleBHex = String.format("%02X", titleBInt);

                Attr colour = document.createAttribute("colour");
                colour.setValue("\"#" + titleRHex + titleGHex + titleBHex + "\"");
                titleElement.setAttributeNode(colour);
            }




        }

        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

}
