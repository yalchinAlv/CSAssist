package com.codeletes.csassist.classCodes;

import android.content.Context;
import android.widget.CheckBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The class to read xml files
 * @author Codeletes G2A
 * @version 1.0
 *
 */
public class Parser {

	// The method that reads student list xml files
	public static ArrayList<Student> getStudentList(InputStream file) {

		Document dom = null;
		ArrayList<Student> students = new ArrayList<>();

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(file);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();

		//get a nodelist of <student> elements
		NodeList nl = docEle.getElementsByTagName("student");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the student element
				Element el = (Element)nl.item(i);

				//get the Student object
				Student student = getStudent(el);

				//add it to list
				students.add( student);
			}
		}

		return students;
	}

	private static Student getStudent(Element std) {

		//for each <student> element get text or int values of
		//name ,id, age and name
		String name = getTextValue(std,"name");
		String surname = getTextValue(std,"surname");
		int id = getIntValue(std,"studentID");


		//Create a new Student with the value read from the xml nodes
		Student student = new Student(name, surname, id);

		return student;
	}

	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);

		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private static int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	// The method that reads a criteria xml file
	public static ArrayList<Criterion> getCriteriaList( InputStream file) {

		Document dom = null;
		ArrayList<Criterion> criteria = new ArrayList<>();

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(file);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}

		System.out.println( dom == null);
		Element docEle = dom.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("criterion");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the criterion element
				Element el = (Element)nl.item(i);

				//get the Criterion object
				Criterion criterion = getCriterion(el);

				//add it to list
				criteria.add( criterion);
			}
		}
		return criteria;
	}

	private static Criterion getCriterion( Element el) {

		String description = getTextValue(el, "description");

		Criterion criterion = new Criterion(description);
		return criterion;
	}
}
