package jaxb;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class XSDVaildater {
	public static void main(String[] args) throws SAXException, IOException {
		SchemaFactory schemaFactory=SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema=schemaFactory.newSchema(new File("d:/student.xsd"));
		Validator validator=schema.newValidator();
		validator.validate(new DOMSource(new Node() {
			
			@Override
			public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setTextContent(String arg0) throws DOMException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setPrefix(String arg0) throws DOMException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setNodeValue(String arg0) throws DOMException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Node replaceChild(Node arg0, Node arg1) throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node removeChild(Node arg0) throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void normalize() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String lookupPrefix(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String lookupNamespaceURI(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isSupported(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isSameNode(Node arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isEqualNode(Node arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isDefaultNamespace(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Node insertBefore(Node arg0, Node arg1) throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean hasChildNodes() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasAttributes() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object getUserData(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getTextContent() throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node getPreviousSibling() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPrefix() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node getParentNode() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Document getOwnerDocument() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNodeValue() throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public short getNodeType() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getNodeName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node getNextSibling() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNamespaceURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getLocalName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node getLastChild() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node getFirstChild() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getFeature(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public NodeList getChildNodes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getBaseURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public NamedNodeMap getAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public short compareDocumentPosition(Node arg0) throws DOMException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Node cloneNode(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Node appendChild(Node arg0) throws DOMException {
				// TODO Auto-generated method stub
				return null;
			}
		}));
		System.out.println(true);
	}

}
