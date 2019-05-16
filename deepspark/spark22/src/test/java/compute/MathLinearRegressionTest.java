package compute;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class MathLinearRegressionTest {

    //@Test
    public void mathLinearRegressionTest() {
        simpleRegression();
        multipleRegression();
    }

    private static void multipleRegression() {
        System.out.println("multipleRegression");
        final OLSMultipleLinearRegression regression2 = new OLSMultipleLinearRegression();
        double[] y = { 2, 3, 4, 5, 6 };
        double[][] x2 = { { 1 }, { 2 }, { 3 }, { 4 }, { 5 }, };

        regression2.newSampleData(y, x2);
        double[] beta = regression2.estimateRegressionParameters();
        for (double d : beta) {
            System.out.println("D: " + d);
        }
        System.out.println("prediction for 1.5 = " + predict(new double[] { 1.5, 1 }, beta));
    }

    private static double predict(double[] data, double[] beta) {
        double result = 0;
        for (int i = 0; i < data.length; i++) {
            result += data[i] * beta[i];
        }
        return result;
    }

    private static void simpleRegression() {
        System.out.println("simpleRegression");
        // creating regression object, passing true to have intercept term
        SimpleRegression simpleRegression = new SimpleRegression(true);

        // passing data to the model
        // model will be fitted automatically by the class
        simpleRegression.addData(new double[][] { { 1, 2 }, { 2, 3 }, { 3, 4 },
                { 4, 5 }, { 5, 6 } });

        // querying for model parameters
        System.out.println("slope = " + simpleRegression.getSlope());
        System.out.println("intercept = " + simpleRegression.getIntercept());

        // trying to run model for unknown data
        System.out.println("prediction for 1.5 = "
                + simpleRegression.predict(1.5));
    }

    @Test
    public void mathLinearTest(){
        double[][] doubles = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}};
        SimpleRegression mathLinearRegressionUtil = MathLinearRegressionUtil.getMathLinearRegression(doubles);
        System.out.println(mathLinearRegressionUtil.predict(6));

    }

}