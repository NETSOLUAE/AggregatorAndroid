package com.rmsllcoman.agg.Model;

/**
 * Created by macmini on 8/8/17.
 */

public class VehicleModel {
    private String makeId;
    private String makeName;
    private String modelId;
    private String modelName;

    public VehicleModel() {

    }

    public VehicleModel(String makeId, String makeName, String modelId, String modelName){
        this.makeId = makeId;
        this.makeName = makeName;
        this.modelId = modelId;
        this.modelName = modelName;
    }

    public void setMakeId (String makeId) {
        this.makeId = makeId;
    }

    public String getMakeId () {
        return makeId;
    }

    public void setMakeName (String makeName) {
        this.makeName = makeName;
    }
    public String getMakeName () {
        return makeName;
    }

    public void setModelId (String modelId) {
        this.modelId = modelId;
    }
    public String getModelId () {
        return modelId;
    }

    public void setModelName (String modelName) {
        this.modelName = modelName;
    }
    public String getModelName () {
        return modelName;
    }
}
