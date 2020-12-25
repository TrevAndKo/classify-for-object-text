package ru.classificator.classificatorweka;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Classify {

    private Instances dataModel;
    private HashMap <String, String> listFilesOfModel = new HashMap<>();
    private HashMap <String, Classifier> listModels = new HashMap<>();
    private String modelsFile = "config.txt";


    public Classify() {
        try {
            setListModels();
            for (Map.Entry<String, String> entry : listFilesOfModel.entrySet()) {
                InputStream classModelStream = new FileInputStream(entry.getValue());
                listModels.put(entry.getKey(),(Classifier) SerializationHelper.read(classModelStream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        listModels = null;
        listFilesOfModel = null;
    }

    public String classifyObject(Map<String, String> measures, String chooseClass) {
        ArrayList dataClasses = new ArrayList();
        ArrayList dataAttribs = new ArrayList();
        Attribute classObject;
        double values[] = new double[measures.size() + 1];
        int i = 0, maxIndex = 0;

        dataClasses.add("person");
        dataClasses.add("object");
        dataClasses.add("something");
        classObject = new Attribute("classObject", dataClasses);

        for (Map.Entry<String, String> element : measures.entrySet()) {
            double val = Double.parseDouble(element.getValue());
            dataAttribs.add(new Attribute(element.getKey()));
            values[i++] = val;
        }
        dataAttribs.add(classObject);
        dataModel = new Instances("classify", dataAttribs, 0);
        dataModel.setClass(classObject);
        dataModel.add(new DenseInstance(1, values));
        dataModel.instance(0).setClassMissing();

        //  Find the class
        double cl[] = new double[0];
        try {
            cl = listModels.get(chooseClass).distributionForInstance(dataModel.instance(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(i = 0; i < cl.length; i++)
            if(cl[i] > cl[maxIndex])
                maxIndex = i;

        return dataModel.classAttribute().value(maxIndex);
    }

    public String classifyObject(String measures, String chooseClass) {

        String [] vector = measures.split(",");
        Map<String, String> values = new HashMap<String, String>();
        values.put("isName", vector[0]);
        values.put("gender", vector[1]);
        values.put("animate", vector[2]);
        values.put("frequency", vector[3]);
        values.put("mainWord", vector[4]);
        values.put("dependentVerbs", vector[5]);
        values.put("dependenceOfVerb", vector[6]);
        values.put("dependentNoun", vector[7]);
        values.put("dependenceOfNoun", vector[8]);
        values.put("dependentAdjective", vector[9]);
        values.put("dependenceOfAdjective", vector[10]);

        return classifyObject(values, chooseClass);
    }


    // Устанавливает соответствие между названием модели и названием файла модели из файла "config.txt"
    private void setListModels () {
        try {
            File file = new File(modelsFile);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.split(":");
                listFilesOfModel.put(splitLine[0], splitLine[1]);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
