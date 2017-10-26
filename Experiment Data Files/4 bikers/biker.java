import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Double;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import java.lang.StringBuilder;
import java.util.Random;
import java.lang.Math;

public class biker{

	private String biker_id = "tobeinitialized";
	private String teammate_id = "tobeinitialized";

	private double energy = 0;
	private double energy_use_rate = 1;
	private double energy_recovery_rate = 0;
	private double position = 0;
	private double timeElapsed= 0;
	private double timeElapsedRate = 1;
	private double energy_spent = 0;

	private double default_energy = 0;
	private double default_advancerate = 1;
	private double default_energy_use_rate = 1;
	private double default_position = 0;
//personalize each rider
	private double advancerate_mulltiplier=1;
	private double energy_use_rate_multiplier=1;
	private double draftingadvancemultiplier = 1;
	private double draftingEnergyUseMultiplier = .6; //https://www.quora.com/Road-Cycling-How-much-wind-drag-is-reduced-by-riding-in-the-peloton-If-I-normally-ride-at-15-mph-in-training-what-would-I-be-able-to-ride-while-in-a-peloton

	private double distance_to_race = 200000;
	private double distance_competing = distance_to_race+5;
	private boolean m_isDrafting = false;
//team behavior
	private boolean m_teamBehavior = false;
	private double m_teammate_rate = 1;


	public final double draftingRangeUpper = 1;
	public final double draftingRangeLower = .5;


	//debug information
	private final boolean m_debug = false;
//constructor for default biker
	public biker(){
		energy = distance_competing ;
		energy_use_rate = default_energy_use_rate;
		position = default_position;
		biker_id = getSaltString();
		randomizeStats();
	}
	//constructor for a biker who needs some pre defined values
	public biker(double _extra_energy, 
		double _energy_use_rate, 
		double _position, 
		String _id){

		energy = distance_competing + _extra_energy;
		energy_use_rate = _energy_use_rate;
		position = _position;
		biker_id = _id;
	}
	// ocnstructor used to define some default characteristics 
	//and define team behavior
	public biker(double _extra_energy,
		double _energy_use_rate, 
		double _position, 
		String _id,
		boolean _teambehavior,
		String _teamate_id){
		energy = distance_competing + _extra_energy;

		energy_use_rate = _energy_use_rate;
		position = _position;
		biker_id = _id;
		m_teamBehavior = _teambehavior;
		teammate_id = _teamate_id;
	}
	//the radomizeStats function generates default cyclists who have some 
	//predefined energy expenditure characteristics. This function randomizs
	// the energy efficienty of the cyclists according to a gaussian distribution
	private void randomizeStats(){
		Random rn = new Random();
		double random_energy = rn.nextGaussian()*15/100;
		energy_use_rate += random_energy;
	}
	//genertates random names for drafting availiblity comparison
	private String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
        	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
        	salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
//sets whether the biker is drafting or not before he advances every time
    public void preAdvance(boolean _isDrafting){
    	m_isDrafting = _isDrafting;
    }
//advance function, advances the biker, updates position and energy accodring to
    // whether the biker is drafting or not, and also checks for whether the biker has finished
    public void advance(){		
    	if(m_debug)
    		getStatsNow();
    	//System.out.println(position);
    	if(!hasFinished()){
    		updatePosition();
    		updateEnergy();
    		updateTime();

    	}

    }
// an alternate version of the advance function which advances a biker 
    //exhibiting team behavior, instead of using optimal speed,
    // this person uses the speed that would allow for the highest time
    // drafting by his fellow teammate (his teammate's speed)
    public void advance(double _teammate_speed){
    	m_teammate_rate = _teammate_speed;
    	if(m_debug)
    		getStatsNow();
    	if(!hasFinished()){
    		updatePosition();
    		updateEnergy();
    		updateTime();

    	}

    }


    private void updatePosition(){
    	position += getAdvancerate();
		//System.out.println(getAdvancerate(_isDrafting));
    }
    private void updateEnergy(){
    	energy -= getEenrgyUseRate();
		//System.out.println("energy:" + energy);
    }
    private void updateTime(){
    	timeElapsed += timeElapsedRate;
    }


    public double getAdvancerate(){
    	if(!m_teamBehavior){
    		return getOptimalRate();
    	}
    	return m_teammate_rate;
    }
    private double getEenrgyUseRate(){
    	if(m_isDrafting){
    		return Math.pow(getAdvancerate(),3) * draftingEnergyUseMultiplier* energy_use_rate;
    	}
    	return Math.pow(getAdvancerate(),3) * energy_use_rate;
    }


    public boolean hasFinished(){
    	return position >= distance_to_race || energy < 0.1;
    }
    public boolean hasCompleteRace(){
    	return position >= distance_to_race;
    }

    public double getPosition(){return position;}


    public String getStats(){
    	return biker_id+"\'s time: " + Double.toString(timeElapsed)+
    	" Energy left: " + energy + "distance traveled: " + position + "efficiency: " + energy_use_rate;

    }
    public String getName(){return biker_id;}


    private double getOptimalRate(){
    	double optimalRateMultiplier = Math.sqrt(energy / (distance_competing - position));
    	return optimalRateMultiplier;
    }

    private void getStatsNow(){
    	System.out.println("################");
    	System.out.println("Name: " + biker_id);
    	System.out.println("energy: " + energy);
    	System.out.println("optimalRateMultiplier: " + getOptimalRate());
    	System.out.println("getEenrgyUseRate: " + getEenrgyUseRate());
    	System.out.println("getAdvancerate: " + getAdvancerate());
    	System.out.println("energy efficiency: " + energy_use_rate);
    	System.out.println("position: " + position);
    	System.out.println("isDrafting: " + m_isDrafting);

    }


}
