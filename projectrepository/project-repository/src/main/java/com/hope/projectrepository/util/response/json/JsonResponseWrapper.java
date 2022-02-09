package com.hope.projectrepository.util.response.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.util.global.Result;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonResponseWrapper {
    private JsonObject jsonObj;

    public JsonResponseWrapper(){
        this.jsonObj = new JsonObject();
        jsonObj.addProperty("code", Result.OK.getCode());
        jsonObj.add("data", new JsonObject());
    }
    public JsonObject getJsonObj(){
        return jsonObj;
    }

    public void setResponseCode(String code){
        jsonObj.addProperty("code", code);
    }

    private JsonObject getDataJsonObj(){
        return (JsonObject)jsonObj.get("data");
    }

    public void addData(String key, String value){
        JsonObject obj = getDataJsonObj();
        obj.addProperty(key,value);
    }

    public void addData(String key, ProjectOverview po){
        JsonObject dataObj = getDataJsonObj();
        JsonObject json = createProjectOverviewJsonObject(po);
        dataObj.add(key, json);
    }

    public void addData(String key, List<?> objList){
        JsonObject dataObj = getDataJsonObj();

        JsonArray arr = new JsonArray();
        for(Object obj: objList){
            if(obj instanceof String)
                arr.add((String)obj);
            else if(obj instanceof ProjectOverview)
                arr.add(createProjectOverviewJsonObject((ProjectOverview) obj));
        }

        dataObj.add(key,arr);
    }

    private JsonObject createProjectOverviewJsonObject(ProjectOverview po){
        JsonObject json = new JsonObject();



        json.addProperty("projectId", nullAble(po.getProjectId()).toString());

        User user = po.getUser();
        if(user != null)
            json.addProperty("user", nullAble(po.getUser().getLoginId()).toString());
        else
            json.addProperty("user", "null");

        json.addProperty("mainTitle", nullAble(po.getMainTitle()).toString());
        json.addProperty("subject", nullAble(po.getSubject()).toString());
        json.addProperty("scale", nullAble(po.getTechStack()).toString());
        json.addProperty("projectStartDate", nullAble(po.getProjectStartDate()).toString());
        json.addProperty("projectEndDate", nullAble(po.getProjectEndDate()).toString());
        json.addProperty("createDate",nullAble(po.getCreateDate()).toString());
        json.addProperty("updateDate",nullAble(po.getUpdateDate()).toString());
        return json;
    }

    private Object nullAble(Object object){
        if(object == null)
            return "null";
        else
            return object;
    }
}
