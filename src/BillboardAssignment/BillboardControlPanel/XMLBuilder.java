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

        if ((backgroundR.length() > 0) && (backgroundG.length() > 0) && (backgroundB.length() > 0)) {
            // Convert title colours to ints
            int backgroundRInt = Integer.parseInt(backgroundR);
            int backgroundGInt = Integer.parseInt(backgroundG);
            int backgroundBInt = Integer.parseInt(backgroundB);

            // Convert ints to hex strings
            String backgroundRHex = String.format("%02X", backgroundRInt);
            String backgroundGHex = String.format("%02X", backgroundGInt);
            String backgroundBHex = String.format("%02X", backgroundBInt);

            Attr colour = document.createAttribute("background");
            colour.setValue("#" + backgroundRHex + backgroundGHex + backgroundBHex);
            billboardElement.setAttributeNode(colour);
        }

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
                colour.setValue("#" + titleRHex + titleGHex + titleBHex);
                titleElement.setAttributeNode(colour);
            }
        }

        if (imagePath.length() > 0) {

            Element imagePathElement;
            imagePathElement = document.createElement("picture");
            billboardElement.appendChild(imagePathElement);

            if (URL == true) {

                Attr urlAttr = document.createAttribute("url");
                urlAttr.setValue(imagePath);
                imagePathElement.setAttributeNode(urlAttr);
            } else {
                Attr dataAttr = document.createAttribute("data");
                dataAttr.setValue(imagePath);
                imagePathElement.setAttributeNode(dataAttr);
            }
        }

        if (subtext.length() > 0) {

            Element subtextElement;
            subtextElement = document.createElement("information");
            billboardElement.appendChild(subtextElement);
            subtextElement.appendChild(document.createTextNode(subtext));

            if ((subtextR.length() > 0) && (subtextG.length() > 0) && (subtextB.length() > 0)) {
                // Convert title colours to ints
                int subtextRInt = Integer.parseInt(subtextR);
                int subtextGInt = Integer.parseInt(subtextG);
                int subtextBInt = Integer.parseInt(subtextB);

                // Convert ints to hex strings
                String subtextRHex = String.format("%02X", subtextRInt);
                String subtextGHex = String.format("%02X", subtextGInt);
                String subtextBHex = String.format("%02X", subtextBInt);

                Attr colour = document.createAttribute("colour");
                colour.setValue("#" + subtextRHex + subtextGHex + subtextBHex);
                subtextElement.setAttributeNode(colour);
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
