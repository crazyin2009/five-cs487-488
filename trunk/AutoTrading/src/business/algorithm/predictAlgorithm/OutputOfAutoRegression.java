package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import business.algorithm.predictAlgorithm.PredictionAlgorithmEntity.Entry;
import Utility.ParamList;

public class OutputOfAutoRegression extends ParamList {
	private ArrayList<Double> predictionPrice;
	private double lambda;
	public OutputOfAutoRegression(ArrayList<Double> predictionPrice, Double lambda) {
		// TODO Auto-generated constructor stub
		this.predictionPrice = predictionPrice;
		this.lambda = lambda;
		this.numOfParam = 2;
	}
	public ArrayList<Double> getPredictionPrice() {
		return predictionPrice;
	}
	public void setPredictionPrice(ArrayList<Double> predictionPrice) {
		this.predictionPrice = predictionPrice;
	}
	public double getLambda() {
		return lambda;
	}
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	public PredictionAlgorithmEntity convertThis(Date startPredictingDate) {
		PredictionAlgorithmEntity entity = new PredictionAlgorithmEntity();
		entity.list = new ArrayList<PredictionAlgorithmEntity.Entry>();
		Date currentDate = startPredictingDate;
		for (double d : predictionPrice) {
			currentDate = increaseDate(currentDate);
			Entry entry = null;
			{
				entry = new Entry();
				entry.date = currentDate;
				entry.midValue = d;
				entry.lowValue = d - Math.abs(lambda);
				entry.highValue = d + Math.abs(lambda);
			}
			entry.date = currentDate;
		}
		return entity;
	}
	public static Date increaseDate(Date currentDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
}