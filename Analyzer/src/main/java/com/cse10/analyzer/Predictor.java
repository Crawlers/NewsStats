package com.cse10.analyzer;

import mltk.core.*;
import mltk.core.io.InstancesReader;
import mltk.predictor.glm.ElasticNetLearner;
import mltk.predictor.glm.GLM;
import mltk.util.MathUtils;
import org.apache.commons.math.stat.regression.SimpleRegression;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

//useful theorems
//least square regression method
//equation Y=aX+b
//Linear regression = Y=aS+bX+cK
//The case of one explanatory variable is called simple linear regression. For more than one explanatory
//variable, the process is called multiple linear regression*/
//Nonlinear Regression = ln(X)=ln(Y)+bZ
//Linear regression tend to over fit. to improve that we need to regularise parameters. for that we can use methods like lasso, ridge or elastic net ( combination of lasso or ridge)
/**
 * perform regression analysis for predicting crimes
 *
 * @author chamath
 */
public class Predictor {

    /**
     * perform linear regression,use SimpleRegression in apache commons
     *
     * @param value
     * @return
     */
    public double predictUsingLR(double value) {

        SimpleRegression simpleRegression = new SimpleRegression();
        simpleRegression.clear();
        simpleRegression.addData(10, 20);
        simpleRegression.addData(20, 10);
        simpleRegression.addData(30, 50);
        simpleRegression.addData(40, 60);
        double intercept = simpleRegression.getIntercept();
        double slope = simpleRegression.getSlope();

        System.out.println(intercept);
        System.out.println(slope);
        double prediction = simpleRegression.predict(value);
        return prediction;


    }

    /**
     * perform elastic net regression,input is taken from a file
     * ElasticNetLearner is from mltk
     *
     * @return
     */
    public double predictUsingENL() {

        ElasticNetLearner elasticNetLearner = new ElasticNetLearner();
        double prediction = 0.0;
        try {
            Instances instances = InstancesReader.read("C:\\Users\\hp\\Desktop\\PredictorIm\\dataFile.txt", 1, " ");
            System.out.println("ddd");
            for (Instance i : instances) {
                System.out.print(i.getValue(0) + " ");
                System.out.println(i.getTarget());
            }
            //build regressor
            GLM glm = elasticNetLearner.buildRegressor(instances, 100, 0.0, 0.0);

            //create new instance for prediction
            int[] indices = {0, 1};
            double[] values = {100, 0};
            Instance i = new Instance(indices, values);

            //perform prediction
            prediction = glm.regress(i);

        } catch (IOException e) {
        }
        return prediction;

    }

    /**
     * perform elastic net regression,input is taken from a db ElasticNetLearner
     * is from mltk
     *
     * @return
     */
    public double predictUsingENLDataFromDB() {


        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "newsarticles";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";
        ResultSet res = null;

        List<Attribute> attributes = new ArrayList<>();
        Instances instances = new Instances(attributes);
        int classIndex = 1;
        ElasticNetLearner elasticNetLearner = new ElasticNetLearner();
        double prediction = 0.0;

        try {
            //create instances from the data in database
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbName, userName, password);
            Statement st = conn.createStatement();
            res = st.executeQuery("SELECT * FROM `prediction_values");
            while (res.next()) {
                double x = res.getDouble("x_value");
                double y = res.getDouble("y_value");
                System.out.println(x + "\t\t" + y);
                String[] data = {Double.toString(x), Double.toString(y)};
                Instance instance = parseDenseInstance(data, classIndex);
                instances.add(instance);

            }
            conn.close();

            int numAttributes = instances.get(0).getValues().length;
            for (int i = 0; i < numAttributes; i++) {
                Attribute att = new NumericalAttribute("f" + i);
                att.setIndex(i);
                attributes.add(att);
            }

            if (classIndex >= 0) {
                assignTargetAttribute(instances);
            }

            //end of instance creation
            //build regressor
            GLM glm = elasticNetLearner.buildRegressor(instances, 100, 0.0, 0.0);

            //create new instance for prediction
            int[] indices = {0, 1};
            double[] values = {100, 0};
            Instance i = new Instance(indices, values);

            //predict the value
            prediction = glm.regress(i);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
        }

        return prediction;

    }

    private Instance parseDenseInstance(String[] data, int classIndex) {
        if (classIndex < 0) {
            double[] vector = new double[data.length];
            double classValue = Double.NaN;
            for (int i = 0; i < data.length; i++) {
                vector[i] = Double.parseDouble(data[i]);
            }
            return new Instance(vector, classValue);
        } else {
            double[] vector = new double[data.length - 1];
            double classValue = Double.NaN;
            for (int i = 0; i < data.length; i++) {
                double value = Double.parseDouble(data[i]);
                if (i < classIndex) {
                    vector[i] = value;
                } else if (i > classIndex) {
                    vector[i - 1] = value;
                } else {
                    classValue = value;
                }
            }
            return new Instance(vector, classValue);
        }
    }

    private void assignTargetAttribute(Instances instances) {
        boolean isInteger = true;
        for (Instance instance : instances) {
            if (!MathUtils.isInteger(instance.getTarget())) {
                isInteger = false;
                break;
            }
        }
        if (isInteger) {
            TreeSet<Integer> set = new TreeSet<>();
            for (Instance instance : instances) {
                double target = instance.getTarget();
                set.add((int) target);
            }
            String[] states = new String[set.size()];
            int i = 0;
            for (Integer v : set) {
                states[i++] = v.toString();
            }
            instances.setTargetAttribute(new NominalAttribute("target", states));
        } else {
            instances.setTargetAttribute(new NumericalAttribute("target"));
        }
    }
}
