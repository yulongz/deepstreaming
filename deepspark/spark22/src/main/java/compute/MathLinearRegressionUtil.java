package compute;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * @author zhangyulong
 */

public class MathLinearRegressionUtil {

    public static SimpleRegression getMathLinearRegression(double[][] doubles){
        // creating regression object, passing true to have intercept term
        SimpleRegression simpleRegression = new SimpleRegression(true);

        // passing data to the model
        // model will be fitted automatically by the class
        simpleRegression.addData(doubles);

        // querying for model parameters
        //System.out.println("slope = " + simpleRegression.getSlope());
        //System.out.println("intercept = " + simpleRegression.getIntercept());

        // trying to run model for unknown data
        //System.out.println("prediction for 1.5 = "+ simpleRegression.predict(1.5));

        return simpleRegression;
    }

    public static double getMathLinearRegressionPredict(SimpleRegression simpleRegression,double doubleValue){
        return  simpleRegression.predict(doubleValue);
    }

}
