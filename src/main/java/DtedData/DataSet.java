package DtedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSet {
    protected List<String> filenames;
    protected List<BorderGrid> borders;
    protected List<ParseDTED> datasets;
    protected int lastAddedFile;    //If some files added by Dted.addData holds last parsed files to just parsing new added ones

    DataSet(){
        filenames = new ArrayList<String>();
        datasets = new ArrayList<ParseDTED>();
        borders = new ArrayList<BorderGrid>();
        lastAddedFile = 0;
    }

    DataSet(List<String> filenames){
        this.filenames = filenames;
        borders = new ArrayList<BorderGrid>();
        datasets = new ArrayList<ParseDTED>();
        lastAddedFile = 0;
    }

    void setFilenames(List<String> files){
        filenames = files;
    }

    public List<BorderGrid> getBorders() {
        return borders;
    }

    int[][] getGrid(int order){
        return datasets.get(order).getGrid();
    }

    void arrange() throws IOException {     //defines new file,add list and parse,
        for(int i = lastAddedFile; i<filenames.size();i++){
            ParseDTED temp = new ParseDTED(filenames.get(i));
            datasets.add(temp);
        }

        for(int i=lastAddedFile; i<datasets.size();i++){        //for new added files
            datasets.get(i).parse();
            BorderGrid curr = new BorderGrid(datasets.get(i).getSw_long(),datasets.get(i).getSw_lat(),
                    datasets.get(i).getNe_long(),datasets.get(i).getNe_lat(),
                    datasets.get(i).total_long_line,datasets.get(i).total_lat_line);
            borders.add(curr);
        }
        lastAddedFile = filenames.size();
    }

    int query(int longitude, int latitude){        //checks wanted lat,long and if it's in sets, return elevation value
        for (int iter = datasets.size()-1;iter>=0;iter--){
            if(datasets.get(iter).checkInterval(longitude, latitude)){
                return datasets.get(iter).getElevation(longitude,latitude);
            }
        }
        return Dted.nullvalue;
    }

}
