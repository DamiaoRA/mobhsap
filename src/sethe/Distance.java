package sethe;

import java.lang.reflect.Method;

import sethe.util.Constants;

/**
 * Class encapsulates distance functions.
 *
 */
public class Distance {

	public static double calc(String distance, Object value, Object ideal,
			double weight, double limit) {

		if(ideal.equals(Constants.ANY_VALUE))
			return weight;

		if(distance.equals("proportion")) {
			return proportion(Double.parseDouble(value.toString()), 
					Double.parseDouble(ideal.toString()), 
					weight, limit);
		} else if(distance.equals("equality")) {
			return equality((String)value, (String) ideal, weight);
		}
		return 0d;
	}

	public static double proportion(Double value, Double ideal, Double weight, double limit) {
		if(value > limit)
			return 0;

		double diff = Math.abs(value-ideal);

		double v = ((weight * diff) / limit) - weight;
		return Math.abs(v);
	}

	public static double equality (String value, String ideal, Double weight) {
		if(value.trim().equals(ideal.trim()))
			return weight;
		else 
			return 0; 
	}

	public static double proximity (int positionP2, int positionP1, Double weight) {
		double result = 1/Math.abs(positionP2-positionP1);
		return result;
	}

	public static double jaccard(Double vector[], Double vectorCoef[]) {
		double sumMin = 0;
		double sumMax = 0;
		for(int i = 0; i < vector.length; i++) {
			sumMin += vector[i] <= vectorCoef[i] ? vector[i] : vectorCoef[i];
			sumMax += vector[i] <= vectorCoef[i] ? vectorCoef[i] : vector[i];
		}
		return sumMin/sumMax;
	}
	
	public static Double similarity(Double[] vec1, Double[] vec2, String distanceFunction) throws Exception {
		Class<Distance> clazz = Distance.class;
	    Method method = clazz.getMethod(distanceFunction, Double[].class, Double[].class);

	    Object result = method.invoke(null, vec1, vec2);
	    return (Double)result;
	}
}