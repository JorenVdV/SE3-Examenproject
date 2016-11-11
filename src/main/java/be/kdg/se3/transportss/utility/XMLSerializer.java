package be.kdg.se3.transportss.utility;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Extension class containing XML serialization en deserialization utilities
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class XMLSerializer {

    public static <T> String XMLSerialize(T obj){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(obj, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
           throw new SerializationException("Could not convert object to XML", e);
        }

    }

    public static <T> T XMLDeserialize(String xml, Class<T> jaxbClass){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(jaxbClass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            return (T)unmarshaller.unmarshal(stringReader);
        } catch (JAXBException e) {
            throw new SerializationException("Could not convert object to XML", e);
        }
    }
}
