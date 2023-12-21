package ipl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.String;

public class Main {
    public  static void main(String args[]) throws IOException {
        ArrayList<Match> matches = getMatchesData();
        ArrayList<Delivery> deliveries = getDeliveriesData();

        numberOfMatchesPlayedPerYear(matches);
        numberOfMatchesWonPerTeamOverAllYears(matches);
        extraRunsConcededPerTeamIn2016(matches, deliveries);
        topEconomicalBowlersInYear2015(matches, deliveries);
    }

    static ArrayList<Match> getMatchesData() throws IOException {
        ArrayList<Match> matches = new ArrayList<Match>();
        String line = "";
        String splitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader("data/matches.csv"));
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] data=line.split(splitBy, -1);

            Match match = new Match();
            match.setId(data[0]);
            match.setSeason(data[1]);
            match.setCity(data[2]);
            match.setDate(data[3]);
            match.setTeam1(data[4]);
            match.setTeam2(data[5]);
            match.setToss_winner(data[6]);
            match.setToss_decision(data[7]);
            match.setResult(data[8]);
            match.setDl_applied(data[9]);
            match.setWinner(data[10]);
            match.setWin_by_runs(data[11]);
            match.setWin_by_wickets(data[12]);
            match.setPlayer_of_match(data[13]);
            match.setVenue(data[14]);
            match.setUmpire1(data[15]);
            match.setUmpire2(data[16]);

            matches.add(match);
        }
        return matches;
    }

    static ArrayList<Delivery> getDeliveriesData() throws IOException {
        ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
        String line = "";
        String splitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader("data/deliveries.csv"));
        while ((line = br.readLine()) != null) {
            String[] data=line.split(splitBy, -1);

            Delivery delivery = new Delivery();
            delivery.setMatch_id(data[0]);
            delivery.setInning(data[1]);
            delivery.setBatting_team(data[2]);
            delivery.setBowling_team(data[3]);
            delivery.setOver(data[4]);
            delivery.setBall(data[5]);
            delivery.setBatsman(data[6]);
            delivery.setNon_striker(data[7]);
            delivery.setBowler(data[8]);
            delivery.setIs_super_over(data[9]);
            delivery.setWide_runs(data[10]);
            delivery.setBye_runs(data[11]);
            delivery.setLegbye_runs(data[12]);
            delivery.setNoball_runs(data[13]);
            delivery.setPenalty_runs(data[14]);
            delivery.setBatsman_runs(data[15]);
            delivery.setExtra_runs(data[16]);
            delivery.setTotal_runs(data[17]);
            delivery.setPlayer_dismissed(data[18]);
            delivery.setDismissal_kind(data[19]);
            delivery.setFielder(data[20]);

            deliveries.add(delivery);
        }
        return deliveries;
    }

    static void numberOfMatchesPlayedPerYear(ArrayList<Match> matches) {
        HashMap<String,Integer>matchesPerYear=new HashMap<>();
        for(Match match: matches) {
            int noOfMatch = matchesPerYear.getOrDefault(match.getSeason(),0)+1;
            matchesPerYear.put(match.getSeason(),noOfMatch);
        }
        System.out.println("Question 1 : Number of matches played per year of all the years in IPL.");
        for(Map.Entry<String,Integer>entry:matchesPerYear.entrySet()) {
            System.out.println("Year "+entry.getKey()+" : "+entry.getValue());
        }
        System.out.println();
    }

    static void numberOfMatchesWonPerTeamOverAllYears(ArrayList<Match> matches) {
        HashMap<String,Integer>numberOfMatchesWonByTeamAllYear=new HashMap<>();
        for(Match match: matches) {
            int matchesWon = numberOfMatchesWonByTeamAllYear.getOrDefault(match.getWinner(), 0)+1;
            numberOfMatchesWonByTeamAllYear.put(match.getWinner(), matchesWon);
        }
        System.out.println("Question 2 : Number of matches won of all teams over all the years of IPL.");
        for(Map.Entry<String,Integer>entry:numberOfMatchesWonByTeamAllYear.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println();
    }

    static void extraRunsConcededPerTeamIn2016(ArrayList<Match> matches, ArrayList<Delivery> deliveries) {
        Set<String>matchIds = new HashSet<>();
        for(Match match:matches){
            if(match.getSeason().equals("2016")){
                matchIds.add(match.getId());
            }
        }

        HashMap<String,Integer>extraRunsConcededPerTeam = new HashMap<>();

        for(Delivery delivery:deliveries){
            if(matchIds.contains(delivery.getMatch_id())){
                int extraRuns = extraRunsConcededPerTeam.getOrDefault(delivery.getBatting_team(),0) + Integer.parseInt(delivery.getExtra_runs());
                extraRunsConcededPerTeam.put(delivery.getBatting_team(), extraRuns);
            }
        }

        System.out.println("Question 3 : For the year 2016 get the extra runs conceded per team.");
        for(Map.Entry<String,Integer>entry:extraRunsConcededPerTeam.entrySet()) {
            System.out.println("Team- "+entry.getKey()+" "+"extra run :"+entry.getValue());
        }
        System.out.println();
    }

    static void topEconomicalBowlersInYear2015(ArrayList<Match> matches, ArrayList<Delivery> deliveries) {
        Set<String>matchIds = new HashSet<>();
        for(Match match:matches){
            if(match.getSeason().equals("2015")){
                matchIds.add(match.getId());
            }
        }

        HashMap<String,Integer> runsConcededByBowlers = new HashMap<>();
        HashMap<String,Integer> ballsBowledByBowlers = new HashMap<>();
        HashMap<String,Double> economyOfBowlers = new HashMap<>();

        for(Delivery delivery:deliveries){
            if(matchIds.contains(delivery.getMatch_id())){
                int totalRunsConcededByBowler = Integer.parseInt(delivery.getTotal_runs()) -  Integer.parseInt( delivery.getLegbye_runs())
                        -  Integer.parseInt(delivery.getBye_runs()) -  Integer.parseInt(delivery.getBye_runs());
                int totalRuns = runsConcededByBowlers.getOrDefault(delivery.getBowler(),0)
                        +totalRunsConcededByBowler;
                runsConcededByBowlers.put(delivery.getBowler(), totalRuns);

                int totalBallsBowledByBowler=ballsBowledByBowlers.getOrDefault(delivery.getBowler(),0)+1;
                totalBallsBowledByBowler -= Integer.parseInt(delivery.getNoball_runs()) + Integer.parseInt(delivery.getWide_runs());
                ballsBowledByBowlers.put(delivery.getBowler(),totalBallsBowledByBowler);

                for(Map.Entry<String,Integer>entry: runsConcededByBowlers.entrySet()){
                    String bowler= entry.getKey();
                    int run= entry.getValue();
                    int ball = ballsBowledByBowlers.get(bowler);
                    double economyRate=(double) run/((double) ball/6);
                    economyOfBowlers.put(bowler,economyRate);
                }
            }
        }

        List<Map.Entry<String, Double>> sortBowlerByEconomy = new ArrayList<>(economyOfBowlers.entrySet());
        sortBowlerByEconomy.sort(Comparator.comparingDouble(Map.Entry::getValue));

        Map.Entry<String, Double> lowestEconomyBowler = sortBowlerByEconomy.get(0);

        String bowlerName = lowestEconomyBowler.getKey();
        Double economyData = lowestEconomyBowler.getValue();
        System.out.println("Question 4 : For the year 2015 get the top economical bowlers.");
        System.out.println(bowlerName+"  "+ economyData);
        System.out.println();
    }

}
