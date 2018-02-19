package application;

import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KMLCreation {
	
	FileWriter kmlWriter;
	public static void main(String[] args) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder = docFactory.newDocumentBuilder();

			Document doc = docbuilder.newDocument();
			Element rootElement = doc.createElement("kml");
			doc.appendChild(rootElement);

			Attr attribute1 = doc.createAttribute("xmlns");
			attribute1.setValue("http://www.opengis.net/kml/2.2");
			rootElement.setAttributeNode(attribute1);

			Element document = doc.createElement("Document");
			rootElement.appendChild(document);

			Element name = doc.createElement("name");
			document.appendChild(name);

			Element desc = doc.createElement("description");
			document.appendChild(desc);

			/* ~~~Style 1~~~ */

			Element style1 = doc.createElement("Style");
			document.appendChild(style1);

			Attr style1Attr = doc.createAttribute("id");
			style1Attr.setValue("Flightpath");
			style1.setAttributeNode(style1Attr);

			Element linestyle1 = doc.createElement("LineStyle");
			style1.appendChild(linestyle1);

			Element color1 = doc.createElement("color");
			linestyle1.appendChild(color1);

			Element width1 = doc.createElement("width");
			linestyle1.appendChild(width1);

			Element polyStyle = doc.createElement("PolyStyle");
			style1.appendChild(polyStyle);

			Element colorPoly = doc.createElement("color");
			polyStyle.appendChild(colorPoly);

			/* ~~~Style 2~~~ */

			Element style2 = doc.createElement("Style");
			Attr style2Attr = doc.createAttribute("id");
			style2Attr.setValue("GroundStation");
			style2.setAttributeNode(style2Attr);
			document.appendChild(style2);

			Element linestyle2 = doc.createElement("LineStyle");
			style2.appendChild(linestyle2);

			Element color2 = doc.createElement("color");
			linestyle2.appendChild(color2);

			Element width2 = doc.createElement("width");
			linestyle2.appendChild(width2);

			/* ~~~Style 3~~~ */

			Element style3 = doc.createElement("Style");
			Attr style3Attr = doc.createAttribute("id");
			style3Attr.setValue("starting");
			style3.setAttributeNode(style3Attr);
			document.appendChild(style3);

			Element iconStyle3 = doc.createElement("IconStyle");
			Element icon3 = doc.createElement("Icon");
			Element href3 = doc.createElement("href");
			href3.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/dir_0.png"));
			icon3.appendChild(href3);
			iconStyle3.appendChild(icon3);
			style3.appendChild(iconStyle3);

			/* ~~~Style 4~~~ */

			Element style4 = doc.createElement("Style");
			Attr style4Attr = doc.createAttribute("id");
			style4Attr.setValue("landing");
			style4.setAttributeNode(style4Attr);
			document.appendChild(style4);

			Element iconStyle4 = doc.createElement("IconStyle");
			Element icon4 = doc.createElement("Icon");
			Element href4 = doc.createElement("href");
			href4.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/dir_walk_60.png"));
			icon4.appendChild(href4);
			iconStyle4.appendChild(icon4);
			style4.appendChild(iconStyle4);

			/* ~~~ Placemark 1 ~~~ */

			Element placemark1 = doc.createElement("Placemark");
			Element timeStamp1 = doc.createElement("TimeStamp");
			Element when1 = doc.createElement("when");
			timeStamp1.appendChild(when1);
			placemark1.appendChild(timeStamp1);
			document.appendChild(placemark1);
			Element styleURLStarting = doc.createElement("styleURL");
			styleURLStarting.appendChild(doc.createTextNode("#starting"));
			placemark1.appendChild(styleURLStarting);
			Element point1 = doc.createElement("Point");
			Element coordinates1 = doc.createElement("coordinates");
			point1.appendChild(coordinates1);
			placemark1.appendChild(point1);

			/* ~~~ Placemark 2 ~~~ */

			Element placemark2 = doc.createElement("Placemark");
			document.appendChild(placemark2);
			Element name1 = doc.createElement("name");
			placemark2.appendChild(name1);
			Element desc1 = doc.createElement("description");
			placemark2.appendChild(desc1);
			Element styleURLFlightPath = doc.createElement("styleURL");
			styleURLFlightPath.appendChild(doc.createTextNode("#Flightpath"));
			placemark2.appendChild(styleURLFlightPath);
			Element lineString = doc.createElement("LineString");
			placemark2.appendChild(lineString);
			Element altOffset = doc.createElement("gx:altitudeOffset");
			lineString.appendChild(altOffset);
			Element altMode = doc.createElement("altitudeMode");
			altMode.appendChild(doc.createTextNode("absolute"));
			lineString.appendChild(altMode);
			Element coordinates2 = doc.createElement("coordinates");
			lineString.appendChild(coordinates2);

			/* ~~~ Placemark 3 ~~~ */

			Element placemark3 = doc.createElement("Placemark");
			document.appendChild(placemark3);
			Element timeStamp2 = doc.createElement("TimeStamp");
			placemark3.appendChild(timeStamp2);
			Element when2 = doc.createElement("when");
			timeStamp2.appendChild(when2);
			Element styleURLLanding = doc.createElement("styleURL");
			styleURLLanding.appendChild(doc.createTextNode("#landing"));
			placemark3.appendChild(styleURLLanding);
			Element point2 = doc.createElement("Point");
			placemark3.appendChild(point2);
			Element coordinates3 = doc.createElement("Coordinates");
			point2.appendChild(coordinates3);

			/*
			 * Element products = doc.createElement("products");
			 * rootElement.appendChild(products);
			 * 
			 * Element items = doc.createElement("Items");
			 * products.appendChild(items);
			 * 
			 * Attr attribute2 = doc.createAttribute("Id");
			 * attribute2.setValue("1"); items.setAttributeNode(attribute2);
			 * 
			 * Element item1 = doc.createElement("item1");
			 * item1.appendChild(doc.createTextNode("Eggs"));
			 * items.appendChild(item1);
			 * 
			 * Element item2 = doc.createElement("item2");
			 * item2.appendChild(doc.createTextNode("Bread"));
			 * items.appendChild(item2);
			 */
			TransformerFactory transformFac = TransformerFactory.newInstance();
			try {
				Transformer transform = transformFac.newTransformer();
				transform.setOutputProperty(OutputKeys.INDENT, "yes");
				transform.setOutputProperty(OutputKeys.METHOD, "xml");
				transform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(System.out); // new
																	// File("C://jars//"+name+".kml")

				transform.transform(source, result);

				System.out.println("Saved!!");
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
