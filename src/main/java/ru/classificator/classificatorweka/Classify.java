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
        int i = 0;

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

        if (chooseClass.equals("EnsembleClassifier")) {
            return classifyWithUseMoreClassify();
        }

        return findTheClass(listModels.get(chooseClass));
    }

    public String classifyObject(String measures, String chooseClass) {

        String [] vector = measures.split(",");
        Map<String, String> values = new HashMap<String, String>();
        values.put("isName", vector[0]);
        values.put("animate", vector[1]);
        values.put("frequency", vector[2]);
        values.put("mainWord", vector[3]);
        values.put("dependentVerbs", vector[4]);
        values.put("dependenceOfVerb", vector[5]);
        values.put("dependentNoun", vector[6]);
        values.put("dependenceOfNoun", vector[7]);
        values.put("dependentAdjective", vector[8]);

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

    private String classifyWithUseMoreClassify () {

        ArrayList <Integer> cl = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cl.add(i, 0);
        }
        ArrayList <String> classOfObject = new ArrayList<>();

        for (Map.Entry <String, Classifier> model: listModels.entrySet()) {
            if (model.getKey().contains("TheCaptain'sDaughterChapter")) {
                classOfObject.add(findTheClass(model.getValue()));
            }
        }


        for (String classOdWord: classOfObject) {
            switch (classOdWord) {
                case "person":
                    cl.set(0, cl.get(0) + 1);
                    break;

                case "object":
                    cl.set(1, cl.get(1) + 1);
                    break;

                case "something":
                    cl.set(1, cl.get(1) + 1);
                    break;
            }
        }

        int maxIndex = 0;

        for(int i = 0; i < cl.size(); i++)
            if(cl.get(i)> cl.get(maxIndex))
                maxIndex = i;

        return dataModel.classAttribute().value(maxIndex);

    }

    private String findTheClass (Classifier model) {
        double cl[] = new double[0];
        int maxIndex = 0;
        try {
            cl = model.distributionForInstance(dataModel.instance(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i < cl.length; i++)
            if(cl[i] > cl[maxIndex])
                maxIndex = i;

        return dataModel.classAttribute().value(maxIndex);
    }

}
