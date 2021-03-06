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
    /**
     * Public method that takes required inputs for the fields that define a billboard in the editor and writes them
     * embedded in an XML string
     *
     * @param title - a string that defines the title of the billboard
     * @param titleR - a string containing an integer representation of the red value of the title colour in RGB integer format
     * @param titleG - a string containing an integer representation of the green value of the title colour in RGB integer format
     * @param titleB - a string containing an integer representation of the blue value of the title colour  in RGB integer format
     * @param imagePath - a string containing the file path of the image or a Base64 byte representation
     * @param URL - a boolean value that determines whether the imagePath content represents a URL or Data
     * @param subtext - a string that defines the subtext of the billboard
     * @param subtextR - a string containing an integer representation of the red value of the subtext colour in RGB integer format
     * @param subtextG - a string containing an integer representation of the green value of the subtext colour in RGB integer format
     * @param subtextB - a string containing an integer representation of the blue value of the subtext colour in RGB integer format
     * @param backgroundR - a string containing an integer representation of the red value of the background colour in RGB integer format
     * @param backgroundG - a string containing an integer representation of the green value of the background colour in RGB integer format
     * @param backgroundB - a string containing an integer representation of the blue value of the background colour in RGB integer format     *
     * @return n/a
     */

    public static String WriteXML (String title, String titleR, String titleG, String titleB,
                              String imagePath, boolean URL,
                              String subtext, String subtextR, String subtextG, String subtextB,
                              String backgroundR, String backgroundG, String backgroundB) throws ParserConfigurationException, TransformerException {

        // Construct document builder objects
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Read the billboard element
        Element billboardElement = document.createElement("billboard");
        document.appendChild(billboardElement);

        // Get the colours of the billboard if they all exist
        if ((backgroundR.length() > 0) && (backgroundG.length() > 0) && (backgroundB.length() > 0)) {
            // Convert title colours to ints
            int backgroundRInt = Integer.parseInt(backgroundR);
            int backgroundGInt = Integer.parseInt(backgroundG);
            int backgroundBInt = Integer.parseInt(backgroundB);

            // Convert ints to hex strings
            String backgroundRHex = String.format("%02X", backgroundRInt);
            String backgroundGHex = String.format("%02X", backgroundGInt);
            String backgroundBHex = String.format("%02X", backgroundBInt);

            // Add combined hex string to document
            Attr colour = document.createAttribute("background");
            colour.setValue("#" + backgroundRHex + backgroundGHex + backgroundBHex);
            billboardElement.setAttributeNode(colour);
        }

        if (title.length() > 0) {

            // Get the title element
            Element titleElement;
            titleElement = document.createElement("message");
            billboardElement.appendChild(titleElement);
            titleElement.appendChild(document.createTextNode(title));

            // Check that the title colours are present
            if ((titleR.length() > 0) && (titleG.length() > 0) && (titleB.length() > 0)) {
                // Convert title colours to ints
                int titleRInt = Integer.parseInt(titleR);
                int titleGInt = Integer.parseInt(titleG);
                int titleBInt = Integer.parseInt(titleB);

                // Convert ints to hex strings
                String titleRHex = String.format("%02X", titleRInt);
                String titleGHex = String.format("%02X", titleGInt);
                String titleBHex = String.format("%02X", titleBInt);

                // Add hex colour to document
                Attr colour = document.createAttribute("colour");
                colour.setValue("#" + titleRHex + titleGHex + titleBHex);
                titleElement.setAttributeNode(colour);
            }
        }

        // Check that the image path is present
        if (imagePath.length() > 0) {

            // Get the page element
            Element imagePathElement;
            imagePathElement = document.createElement("picture");
            billboardElement.appendChild(imagePathElement);

            // Check that the image path is a url or data
            if (URL == true) {

                // Create URL element in picture tag
                Attr urlAttr = document.createAttribute("url");
                urlAttr.setValue(imagePath);
                imagePathElement.setAttributeNode(urlAttr);
            } else {
                // Create data attribute in picture tag
                Attr dataAttr = document.createAttribute("data");
                dataAttr.setValue(imagePath);
                imagePathElement.setAttributeNode(dataAttr);
            }
        }

        // Check that the subtext exists
        if (subtext.length() > 0) {

            // Create an information tag for the subtext
            Element subtextElement;
            subtextElement = document.createElement("information");
            billboardElement.appendChild(subtextElement);
            subtextElement.appendChild(document.createTextNode(subtext));

            // Check that the subtext coloiurs exist
            if ((subtextR.length() > 0) && (subtextG.length() > 0) && (subtextB.length() > 0)) {
                // Convert title colours to ints
                int subtextRInt = Integer.parseInt(subtextR);
                int subtextGInt = Integer.parseInt(subtextG);
                int subtextBInt = Integer.parseInt(subtextB);

                // Convert ints to hex strings
                String subtextRHex = String.format("%02X", subtextRInt);
                String subtextGHex = String.format("%02X", subtextGInt);
                String subtextBHex = String.format("%02X", subtextBInt);

                // Set attribute of the information tag for the colour
                Attr colour = document.createAttribute("colour");
                colour.setValue("#" + subtextRHex + subtextGHex + subtextBHex);
                subtextElement.setAttributeNode(colour);
            }
        }

        // Build the XML document, then return it as a string
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

}
