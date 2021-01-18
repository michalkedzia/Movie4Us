package data;

import data.WatchProviderData;
import java.util.ArrayList;

public class MovieWatchProviderData {
    public int id;
    public ArrayList<WatchProviderData> PL;
    public ArrayList<WatchProviderData> US;

    @Override
    public String toString() {
        StringBuilder jsonPL = new StringBuilder();
        if (this.PL != null) {
            for (int i = 0; i < PL.size() - 1; i++) {
                jsonPL.append(this.PL.get(i).toString());
                jsonPL.append(", \n");
            }
            jsonPL.append(this.PL.get(this.PL.size() - 1).toString());
        } else {
            jsonPL.append("");
        }

        StringBuilder jsonUS = new StringBuilder();
        if (this.US != null) {
            for (int i = 0; i < this.US.size() - 1; i++) {
                jsonUS.append(this.US.get(i).toString());
                jsonUS.append(",\n");
            }
            jsonUS.append(this.US.get(this.US.size() - 1).toString());
        } else {
            jsonUS.append("");
        }

        return "MovieWatchProviderData{"
                + "\nid="
                + id
                + "\nPL="
                + jsonPL.toString()
                + ", \nUS="
                + jsonUS.toString()
                + '}';
    }

    public ArrayList<String> getPLProvider(){
        ArrayList<String> providers = new ArrayList<>();
        ArrayList<WatchProviderData> setProvider = US;
        if(setProvider == null) return providers;

        for(int i=0; i<setProvider.size(); i++){
            providers.add(setProvider.get(i).getLogo_path());
        }
        return providers;
    }

}
