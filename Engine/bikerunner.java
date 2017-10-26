import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class bikerunner{
	private ArrayList<biker> bikers = new ArrayList<biker>();
	private ArrayList<biker> bikerTeam = new ArrayList<biker>();
	private double draftingRangeUpper = 1;
	private double draftingRangeLower = .5;
	private boolean m_debug = false;

	private int randomPlayercount = 200;

	public void bikerunner(int exp_number){
		
		for (int i = 0; i < randomPlayercount;i++)
			bikers.add(new biker());
		//bikers.add(new biker(0,1,1,0,"sam"));
		//add biker teams here
		bikerTeam.add(new biker(0,1,1,"servee0"));
		bikerTeam.add(new biker(0,1,1.7,"servee1",true,"sam"));
		bikerTeam.add(new biker(0,1,2.4,"servee2",true,"sam"));
		bikerTeam.add(new biker(0,1,3.1,"servee3",true,"sam"));
		//bikerTeam.add(new biker(0,1,3.8,"servee4",true,"sam"));


		while(!hasAllBikerFinished(bikers) || !hasAllBikerFinished(bikerTeam)){
			if(m_debug)
				System.out.println("########################################################################################");
			for(biker i : bikers){
				if(m_debug)
					System.out.println(i.getName() + "isDrafting: " + isBikerDrafting(i));
				i.preAdvance(isBikerDrafting(i));
			}
			for(biker i : bikers){
				if(m_debug)
					System.out.println(i.getName() + "isDrafting: " + isBikerDrafting(i));
				i.advance();
			}

			// add some specific methods to actually incororate team rocket stage style launch effect
			//team play will form a rocket stage syle race where each player will form a slipstream for the next player until
			//energy runs out
			for(biker i : bikerTeam){
				i.preAdvance(isBikerDrafting(i));
			}
			bikerTeam.get(0).advance();
			for (int i = 1; i < bikerTeam.size(); i++){
				bikerTeam.get(i).advance(bikerTeam.get(0).getAdvancerate());
			}

		}
		
		try {
			PrintStream out = new PrintStream(new FileOutputStream(exp_number + " - Normal.txt"));
			PrintStream outTeam = new PrintStream(new FileOutputStream(exp_number + " - Team.txt"));

			for(biker i: bikers)
				if(i.hasCompleteRace())
					out.println(i.getStats());
				for(biker i: bikerTeam)
					if(i.hasCompleteRace())
						outTeam.println(i.getStats());

					out.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}

			private boolean isBikerDrafting(biker _biker){
				boolean isDrafting = false;
				double distance = 0;

				for(biker i : bikers){
					if(!i.getName().equals(_biker.getName())){
						distance = i.getPosition() - _biker.getPosition();
						if(m_debug){
							System.out.println("distance between " + i.getName() + " and " + _biker.getName());
							System.out.println(distance);
						}
						if ((distance <= _biker.draftingRangeUpper) && (distance >= _biker.draftingRangeLower)) {
							return true;
						}
					}
				}

				for(biker i : bikerTeam){
					if(!i.getName().equals(_biker.getName())){
						distance = i.getPosition() - _biker.getPosition();
						if(m_debug){
							System.out.println("distance between " + i.getName() + " and " + _biker.getName());
							System.out.println(distance);
						}
						if ((distance <= _biker.draftingRangeUpper) && (distance >= _biker.draftingRangeLower)) {
							return true;
						}
					}
				}

				return isDrafting;
			}

			private boolean hasAllBikerFinished(ArrayList<biker> _bikers){
				boolean hasfinished = true;
				for(biker i : _bikers){
					if(!i.hasFinished())
						return false;
				}
				return hasfinished;
			}
		}
