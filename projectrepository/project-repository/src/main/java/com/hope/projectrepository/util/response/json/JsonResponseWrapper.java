package com.hope.projectrepository.util.response.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hope.projectrepository.util.dto.ProjectContentAndFileInfoDTO;
import com.hope.projectrepository.domain.entity.FileInfo;
import com.hope.projectrepository.domain.entity.ProjectContent;
import com.hope.projectrepository.domain.entity.ProjectOverview;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.util.global.Result;

import java.util.List;

public class JsonResponseWrapper {
    private JsonObject jsonObj;
    private static final String NULL = "null";

    public JsonResponseWrapper(){
        this.jsonObj = new JsonObject();
        jsonObj.addProperty("code", Result.OK.getCode());
        jsonObj.add("data", new JsonObject());
    }
    private JsonObject getJsonObj(){
        return jsonObj;
    }

    public String getResponse(){
        return getJsonObj().toString();
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

    public void addData(String key, Boolean bool){
        JsonObject obj = getDataJsonObj();
        obj.addProperty(key, bool.toString());
    }

    public void addData(String key, ProjectOverview po){
        JsonObject dataObj = getDataJsonObj();
        JsonObject json = createProjectOverviewJsonObject(po);
        dataObj.add(key, json);
    }

    public void addData(String key, User user){
        JsonObject dataObj = getDataJsonObj();
        JsonObject json = createUserJsonObject(user);
        dataObj.add(key,json);
    }

    public void addData(String key, ProjectContentAndFileInfoDTO pcafid){
        JsonObject dataObj = getDataJsonObj();
        JsonObject json = createContentAndFileJsonObject(pcafid);
        dataObj.add(key,json);
    }

    public void addData(String key, List<?> objList){
        JsonObject dataObj = getDataJsonObj();

        if(objList == null){
            dataObj.addProperty(key, "null");
        }else{
            JsonArray arr = new JsonArray();
            for(Object obj: objList){
                if(obj instanceof String)
                    arr.add((String)obj);
                else if(obj instanceof ProjectOverview)
                    arr.add(createProjectOverviewJsonObject((ProjectOverview) obj));
                else if(obj instanceof User)
                    arr.add(createUserJsonObject((User)obj));
                else if(obj instanceof ProjectContentAndFileInfoDTO)
                    arr.add(createContentAndFileJsonObject((ProjectContentAndFileInfoDTO) obj));
            }

            dataObj.add(key,arr);
        }
    }

    private JsonObject createProjectOverviewJsonObject(ProjectOverview po){
        JsonObject json = new JsonObject();

        json.addProperty("project_id", nullAble(po.getProjectId()));

        User user = po.getUser();
        if(user != null)
            json.add("user", createUserJsonObject(user));

        json.addProperty("main_title", nullAble(po.getMainTitle()));
        json.addProperty("subject", nullAble(po.getSubject()));
        json.addProperty("scale", nullAble(po.getScale()));
        json.addProperty("techstack", nullAble(po.getTechStack()));
        json.addProperty("project_start_date", nullAble(po.getProjectStartDate()));
        json.addProperty("project_end_date", nullAble(po.getProjectEndDate()));
        json.addProperty("create_date",nullAble(po.getCreateDate()));
        json.addProperty("update_date",nullAble(po.getUpdateDate()));
        return json;
    }

    private JsonObject createUserJsonObject(User user){
        JsonObject json = new JsonObject();
        json.addProperty("user_id", nullAble(user.getUserId()));
        json.addProperty("login_id", nullAble(user.getLoginId()));
        json.addProperty("nickname", nullAble(user.getNickname()));
        json.addProperty("email", nullAble(user.getEmail()));

        return json;
    }

    private JsonObject createContentAndFileJsonObject(ProjectContentAndFileInfoDTO projectContentAndFileInfoDTO){
        JsonObject json = new JsonObject();
        ProjectContent projectContent = projectContentAndFileInfoDTO.getContent();
        FileInfo fileInfo = projectContentAndFileInfoDTO.getFile();

        json.addProperty("content_id", nullAble(projectContent.getContentId()));
        json.addProperty("sub_title", nullAble(projectContent.getSubTitle()));
        json.addProperty("content", nullAble(projectContent.getContent()));

        if(fileInfo != null){
            json.addProperty("file_id", nullAble(fileInfo.getFileId()));
            json.addProperty("file_name", nullAble(fileInfo.getFileName()));
        }else{
            json.addProperty("file_id", NULL);
            json.addProperty("file_name", NULL);
        }

        return json;
    }

    private String nullAble(Object object){
        if(object == null)
            return NULL;
        else
            return object.toString();
    }
}
