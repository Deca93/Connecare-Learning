package learning;

import learning.interfaces.IMerger;
import smile.data.AttributeDataset;

/**
 * Created by Andrea De Castri on 11/11/2017.
 *
 */
public class DataModelMerger implements IMerger {

    private static final String MERGED_DATASET = "Merged Dataset";

    public DataModelMerger(){
        super();
    }

    @Override
    public AttributeDataset merge(AttributeDataset first, AttributeDataset second) {
        AttributeDataset result = new AttributeDataset(MERGED_DATASET, first.attributes(), first.response().attribute());

        double[][] xFirst = first.toArray(new double[first.size()][first.attributes().length]);
        int[] yFirst = first.toArray(new int[first.size()]);

        for(int i=0; i<first.size(); i++){
            result.add(xFirst[i], yFirst[i]);
        }

        double[][] xSecond = first.toArray(new double[second.size()][]);
        int[] ySecond = first.toArray(new int[second.size()]);

        for(int j=0; j<second.size(); j++){
            result.add(xSecond[j], ySecond[j]);
        }

        return result;
    }

}
