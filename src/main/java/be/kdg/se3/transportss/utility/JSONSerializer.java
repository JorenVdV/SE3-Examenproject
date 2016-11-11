package be.kdg.se3.transportss.utility;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Extension class containing utilities for writing and reading json objects
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class JSONSerializer {
    public static <T> void WriteToFile(List<T> list, String filename){
        try (Writer writer = new FileWriter(filename)) {
            Gson gson = new Gson();
            gson.toJson(list, writer);
        }catch (Exception e){
            throw new SerializationException("Serializer could not write to file", e);
        }
    }

    public static <T> T ReadFromFile(String filename, Type classType) throws SerializationException{
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            Gson gson = new Gson();
            return gson.fromJson(reader, classType);
        } catch (FileNotFoundException e) {
            throw new SerializationException("Serializer could not find the file, filename:"+filename, e);
        } catch (IOException e) {
            throw new SerializationException("Serializer could not read the file, filename:"+filename, e);
        }
    }

    public static <T> T ObjectFromString(String json, Class<T> jsonClass){
        StringReader stringReader = new StringReader(json);
        Gson gson = new Gson();
        return gson.fromJson(stringReader, jsonClass);
    }
}
