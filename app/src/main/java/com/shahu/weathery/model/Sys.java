package com.shahu.weathery.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Sys{

	@SerializedName("country")
	private String country;

	@SerializedName("sunrise")
	private long sunrise;

	@SerializedName("sunset")
	private long sunset;

	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private int type;

	@SerializedName("message")
	private double message;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setSunrise(long sunrise){
		this.sunrise = sunrise;
	}

	public long getSunrise(){
		return sunrise;
	}

	public void setSunset(long sunset){
		this.sunset = sunset;
	}

	public long getSunset(){
		return sunset;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setMessage(double message){
		this.message = message;
	}

	public double getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Sys{" + 
			"country = '" + country + '\'' + 
			",sunrise = '" + sunrise + '\'' + 
			",sunset = '" + sunset + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}