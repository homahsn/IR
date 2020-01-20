package lucene_project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class SOQuestion
{
	private String title;
	private String question;
	private String fileName;
	private String[] tags;
	private String[] answers;
	
	SOQuestion(File xml_file) throws ParserConfigurationException, SAXException, IOException
	{
		fileName = xml_file.getName();
		
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setIgnoringElementContentWhitespace(false);
		DocumentBuilder b = fact.newDocumentBuilder();
		String content = "<root>"+Utils.readFile(xml_file.getAbsolutePath())+"</root>";
		InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		Document d =  b.parse(stream);
		
		Element qu = (Element) d.getElementsByTagName("question").item(0);
		title = qu.getElementsByTagName("Title").item(0).getTextContent();
		question = qu.getElementsByTagName("Body").item(0).getTextContent();
		tags = qu.getElementsByTagName("Tags").item(0).getTextContent().split(",");
		
		
		NodeList ans = d.getElementsByTagName("answer");
		
		answers = new String[ans.getLength()];
		
		for(int i=0;i<ans.getLength();i++)
		{
			Element a = (Element)ans.item(i);
			answers[i] = a.getElementsByTagName("Body").item(0).getTextContent();
		}
	}
	public String getTitle()
	{
		return title;
	}
	public String getQuestion()
	{
		return question;
	}
	public String getFileName()
	{
		return fileName;
	}
	public String[] getTags()
	{
		return tags;
	}
	public String[] getAnswers()
	{
		return answers;
	}
	public String getQuestionAndAnswers()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(question+"\n");
		for(String ans:answers)
			sb.append(ans+"\n");
		return sb.toString();
	}
	
	
	
	
}
